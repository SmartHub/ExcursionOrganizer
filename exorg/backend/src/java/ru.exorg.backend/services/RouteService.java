package ru.exorg.backend.services;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.exorg.backend.model.Route;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 3:18
 * To change this template use File | Settings | File Templates.
 */
public class RouteService {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Route getUserRoute(long userId)
    {
        Route route = null;
        return route;
    }

    public Route addPointInUserRoute(long userId, long pointId)
    {
        Route route = null;
        return route;

    }

    public Route deletePointFromUserRoute(long userId, long pointId)
    {
        Route route = null;
        return route;

    }
}
