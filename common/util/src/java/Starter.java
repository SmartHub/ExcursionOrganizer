package eo.common;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import eo.common.Logger;

// ================================================================================

public class Starter {
    public static void main(String[] args) {
        Logger.init();

        new FileSystemXmlApplicationContext(new String[]{args[0]});
    }
}