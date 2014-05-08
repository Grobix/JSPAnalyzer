package org.apache.jasper.compiler;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;

public class Executer extends JspC {
	
    @Override public String getCompilerClassName() {
        return OnlyReadingJspPseudoCompiler.class.getName();
    }
 
    public static void main(String[] args) {
    	Executer jspc = new Executer();
    	
    	jspc.setWebXml("D:/dev/bsh/integration/bin/ext-bsh/bshweb/web/webroot/WEB-INF");
        
    	jspc.setUriroot("D:/dev/bsh/integration/bin/ext-bsh/bshweb/web/webroot/WEB-INF/templates/bosch2013/pages/account/registration"); // where to search for JSPs
        jspc.setVerbose(1);     // 0 = false, 1 = true
        jspc.setJspFiles("myaccountRegistrationPage.jsp"); // leave unset to process all; comma-separated
 
        try {
            jspc.execute();
        } catch (JasperException e) {
            throw new RuntimeException(e);
        }
    }
}
