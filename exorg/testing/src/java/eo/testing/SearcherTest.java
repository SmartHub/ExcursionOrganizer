package eo.testing;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/30/11
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SearcherTest {

    @Test
    public void testQueryById()
    {
        try {
            assertEquals(eo.frontend.httpserver.Searcher.queryById(1).name, "Исаакиевский собор");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryTypes()
    {
        try {
            assertEquals(eo.frontend.httpserver.Searcher.queryTypes().length, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
