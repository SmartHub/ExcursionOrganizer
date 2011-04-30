package ru.exorg.miner;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;
/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Apr 20, 2011
 * Time: 6:36:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MinerTest {
    @Test
    public void testBeautify() throws Exception {
        assertEquals(Miner.beautify("this is\r\n\r\n\r\nmy\ttest\tstring"), "this is\nmy test string");        
    }
}
