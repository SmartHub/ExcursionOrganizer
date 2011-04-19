package ru.exorg.frontend.yalets;

import net.sf.xfresh.core.*;
import org.eclipse.jetty.server.*;

// ================================================================================

public class IndexYalet implements Yalet {
    private SessionManager sm;

    final public void setSessionManager(SessionManager sm) {
        this.sm = sm;
    }

    public static class Test {
        public String s;

        final public String getSid() {
            return s;
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        Test t = new Test();
        t.s = req.getHttpServletRequest().getSession().getId();

        res.add(t);
    }
}
