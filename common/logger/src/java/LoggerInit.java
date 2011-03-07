package eo.common;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

// ================================================================================

public class LoggerInit {
    public LoggerInit() {
        DOMConfigurator.configure("miner/config/log4j.xml");
    }
}

// ================================================================================