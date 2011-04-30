package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;

public class SidDemo {
    public static class TestSid1 implements Yalet {
        public void process(InternalRequest req, InternalResponse res) {
            req.getHttpServletRequest().getSession().setAttribute("saved", "Session works!");
            res.addWrapped("sid", req.getHttpServletRequest().getSession().getId());
        }
    }

    public static class TestSid2 implements Yalet {
       public void process(InternalRequest req, InternalResponse res) {
            res.addWrapped("sid", req.getHttpServletRequest().getSession().getId());
            res.addWrapped("param", (String)req.getHttpServletRequest().getSession().getAttribute("saved"));
        }
    }
}