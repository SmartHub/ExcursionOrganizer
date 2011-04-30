package ru.exorg.core.util;

import org.apache.log4j.xml.DOMConfigurator;

import java.io.PrintWriter;
import java.io.StringWriter;

// ================================================================================

public class Log {
    public static void init(String path) {
        String logConfPath = path + "/core/script/log4j.xml";
        DOMConfigurator.configure(logConfPath);
    }

    public static String getCallStack(final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }
}

// ================================================================================