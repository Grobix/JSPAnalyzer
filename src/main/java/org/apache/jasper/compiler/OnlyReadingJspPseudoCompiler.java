package org.apache.jasper.compiler;

import java.io.FileNotFoundException;

import org.apache.jasper.JasperException;

public class OnlyReadingJspPseudoCompiler extends Compiler {

    /** We're never compiling .java to .class. */
    @Override protected void generateClass(String[] smap) throws FileNotFoundException,
            JasperException, Exception {
        return;
    }

    /** Copied from {@link Compiler#generateJava()} and adjusted */
    @Override protected String[] generateJava() throws Exception {

        // Setup page info area
        pageInfo = new PageInfo(new BeanRepository(ctxt.getClassLoader(),
                errDispatcher), ctxt.getJspFile());

        // JH: Skipped processing of jsp-property-group in web.xml for the current page

        if (ctxt.isTagFile()) {
            try {
                double libraryVersion = Double.parseDouble(ctxt.getTagInfo()
                        .getTagLibrary().getRequiredVersion());
                if (libraryVersion < 2.0) {
                    pageInfo.setIsELIgnored("true", null, errDispatcher, true);
                }
                if (libraryVersion < 2.1) {
                    pageInfo.setDeferredSyntaxAllowedAsLiteral("true", null,
                            errDispatcher, true);
                }
            } catch (NumberFormatException ex) {
                errDispatcher.jspError(ex);
            }
        }

        ctxt.checkOutputDir();

        try {
            // Parse the file
            ParserController parserCtl = new ParserController(ctxt, this);

            // Pass 1 - the directives
            Node.Nodes directives =
                parserCtl.parseDirectives(ctxt.getJspFile());
            Validator.validateDirectives(this, directives);

            // Pass 2 - the whole translation unit
            pageNodes = parserCtl.parse(ctxt.getJspFile());

            // Validate and process attributes - don't re-validate the
            // directives we validated in pass 1
            /**
             * JH: The code above has been copied from Compiler#generateJava() with some
             * omissions and with using our own Visitor.
             * The code that used to follow was just deleted.
             * Note: The JSP's name is in ctxt.getJspFile()
             */
            pageNodes.visit(new JspPrinter());

        } finally {}

        return null;
    }

    /**
     * The parent's implementation, in our case, checks whether the target file
     * exists and returns true if it doesn't. However it is expensive so
     * we skip it by returning true directly.
     * @see org.apache.jasper.JspCompilationContext#getServletJavaFileName()
     */
    @Override public boolean isOutDated(boolean checkClass) {
        return true;
    }

}
