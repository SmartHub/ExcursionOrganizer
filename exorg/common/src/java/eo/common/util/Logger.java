package eo.common.util;

import org.apache.log4j.xml.DOMConfigurator;

import java.io.PrintWriter;
import java.io.StringWriter;

// ================================================================================

public class Logger {
    public static void init(String path) {
        DOMConfigurator.configure(path + "/common/script/log4j.xml");
    }

    public static String getCallStack(final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return pw.toString();
    }
}

// ================================================================================