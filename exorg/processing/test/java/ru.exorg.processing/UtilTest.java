package ru.exorg.processing;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/20/11
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */

public class UtilTest{
    @Test
    public void testGetLevenshteinDistance() throws Exception {
        assertEquals(Util.getLevenshteinDistance("abracadabra", "banana"), 7);
    }
}
