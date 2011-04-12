package eo.common.util;

import java.lang.Exception;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.xml.DOMConfigurator;

// ================================================================================

public class Log {
    public static void init(String path) {
        String log_conf_path = path + "/common/script/log4j.xml";
        DOMConfigurator.configure(log_conf_path);
    }

    public static String getCallStack(final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }
}

// ================================================================================