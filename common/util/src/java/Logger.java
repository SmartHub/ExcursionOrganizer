package eo.common;

import java.lang.Exception;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.xml.DOMConfigurator;

// ================================================================================

public class Logger {
    public static void init() {
        String log_conf_path = System.getenv("EO_ROOT") + "/common/script/log4j.xml";
        DOMConfigurator.configure(log_conf_path);
    }

    public static String getCallStack(final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return pw.toString();
    }
}

// ================================================================================