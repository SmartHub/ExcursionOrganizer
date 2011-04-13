package eo.common.util;

import org.springframework.context.support.FileSystemXmlApplicationContext;

// ================================================================================

public class Starter {
    public static void main(String[] args) {
        Log.init(args[1]);

        String[] a = new String[1];
        a[0] = args[0];
        new FileSystemXmlApplicationContext(a);
    }
}