package eo.common;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import eo.common.Logger;

// ================================================================================

public class Starter {
    public static void main(String[] args) {
        Logger.init();

        String[] a = new String[1];
        a[0] = args[0];
        new FileSystemXmlApplicationContext(a);
    }
}