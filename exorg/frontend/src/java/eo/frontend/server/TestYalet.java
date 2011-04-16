package eo.frontend.server;

import net.sf.xfresh.core.*;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 16.04.11
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public class TestYalet implements Yalet {
    public static class Test {
        public String field1;
        public String field2;

        final public String getField1() {
            return field1;
        }

        final public String getField2() {
            return field2;
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        System.out.println("Yalet was called!");

        Test t = new Test();
        t.field1 = "Text1";
        t.field2 = "Text2";

        res.add(t);
    }
}
