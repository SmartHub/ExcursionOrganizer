package ru.exorg.backend.services;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.exorg.core.model.PoiType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:45:41
 * To change this template use File | Settings | File Templates.
 */
public class PoiTypeService {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private PoiType getPoiTypeById (final long id) {
        String q = String.format("SELECT name, guess_rx FROM poi_type WHERE id = %d;", id);
        RowMapper<PoiType> mapper = new RowMapper<PoiType>() {
            public PoiType mapRow(ResultSet rs, int i) throws SQLException {
                PoiType poiType = new PoiType(id, rs.getString("name"), rs.getString("guess_rx"));
                return poiType;
            }
        };
        List<PoiType> list = jdbcTemplate.query(q, mapper);
        if (list != null)
            return list.get(0);
        return null;
    }

    public List<PoiType> getPoiTypes () {
        String q = new String("SELECT id FROM poi_type;");
        List<Integer> typeIds = jdbcTemplate.queryForList(q, Integer.class);
        if(!typeIds.isEmpty())
        {
            List<PoiType> result = new ArrayList<PoiType>();
            for(int i: typeIds)
            {
                 result.add(getPoiTypeById(i));
            }
            return result;
        }
        return null;
    }
}
