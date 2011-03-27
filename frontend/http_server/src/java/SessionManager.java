package eo.frontend.httpserver;

import java.util.*;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.beans.factory.InitializingBean;

// ================================================================================

class SessionManager implements InitializingBean {
    private JdbcOperations ops_;
    private Random sid_gen_;

    public static class UserRoute {
        public static class Point {
            public int poi_id;
            public int ord_num;

            public Point(int _poi_id, int _ord_num) {
                poi_id = _poi_id;
                ord_num = _ord_num;
            }
        }

        public int sid;
        public Point[] ps;

        public UserRoute(int len) {
            ps = new Point[len];
        }
    }

    public final void setConn(SimpleJdbcTemplate conn) {
        ops_ = conn.getJdbcOperations();
    }

    private final boolean isAvailableSID(int sid) {
        String q = String.format("SELECT COUNT(*) FROM user_session WHERE id=%d;",
                                 sid);

        return ops_.queryForInt(q) == 0;
    }

    public final int createSession() {
        int sid;

        do {
            sid = sid_gen_.nextInt();
        } while (!isAvailableSID(sid));

        String q = String.format("INSERT INTO user_session(id) VALUES (%d);", sid);
        ops_.execute(q);

        return sid;
    }

    public final void addPOI(int sid, int poi_id, int ord_num) {
        String q = String.format("INSERT INTO user_route(sid, poi_id, ord_num) VALUES (%d, %d, %d);",
                                 sid, poi_id, ord_num);

        ops_.execute(q);
    }

    public final void setRoute(UserRoute r) {
        String q = String.format("DELETE FROM user_route WHERE sid=%d;", r.sid);
        ops_.execute(q);

        for (int i = 0; i < r.ps.length; ++i) {
            addPOI(r.sid, r.ps[i].poi_id, r.ps[i].ord_num);
        }
    }

    public final UserRoute getRoute(int sid) {
        String q = String.format("SELECT COUNT(*) FROM user_route WHERE sid=%d;",
                                 sid);
        int route_len = ops_.queryForInt(q);

        UserRoute route = new UserRoute(route_len);
        route.sid = sid;

        q = String.format("SELECT (poi_id, ord_num) FROM user_route WHERE sid=%d;",
                          sid);
        SqlRowSet ur = ops_.queryForRowSet(q);
        ur.first();
        for (int i = 0; i < route_len; ++i) {
            route.ps[i] = new UserRoute.Point(ur.getInt("poi_id"), ur.getInt("ord_num"));
            ur.next();
        }

        return route;
    }
    
    public void afterPropertiesSet() {
        sid_gen_ = new Random();
    }
}