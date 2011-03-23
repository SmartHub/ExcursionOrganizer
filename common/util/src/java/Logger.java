package eo.common;

import org.apache.log4j.xml.DOMConfigurator;

// ================================================================================

public class Logger {
    public static void init() {
        DOMConfigurator.configure("log4j.xml");
    }
}

// ================================================================================