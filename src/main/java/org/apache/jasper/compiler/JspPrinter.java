package org.apache.jasper.compiler;

import java.util.LinkedList;

import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.Node.CustomTag;
import org.apache.jasper.compiler.Node.IncludeDirective;
import org.apache.jasper.compiler.Node.Visitor;
import org.xml.sax.Attributes;

public class JspPrinter extends Visitor {

    private String indent = "";

    @Override
    public void visit(IncludeDirective n) throws JasperException {
        logEntry("IncludeDirective", n, toString(n.getAttributes()));

		super.visit(n);

    }

    
    @Override
    public void visit(CustomTag n) throws JasperException  {
        logEntry("CustomTag", n, "Class: " + n.getTagHandlerClass().getName() + ", attrs: "
                + toString(n.getAttributes()));

        doVisit(n);

        indent += " ";
        visitBody(n);
        indent = indent.substring(0, indent.length() - 1);
    }

    private String toString(Attributes attributes) {
        if (attributes == null || attributes.getLength() == 0) return "";
        LinkedList<String> details = new LinkedList<String>();

        for (int i = 0; i < attributes.getLength(); i++) {
            details.add(attributes.getQName(i) + "=" + attributes.getValue(i));
        }

        return details.toString();
    }

    private void logEntry(String what, Node n, String details) {
        System.out.println(indent + n.getQName() + details);
    }

}

