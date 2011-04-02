package eo.frontend.httpserver;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class InitJDBC {
        public InitJDBC(SimpleJdbcTemplate conn) {
            Searcher.ops = conn.getJdbcOperations();
        }
    }