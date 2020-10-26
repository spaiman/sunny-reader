package com.setiawanpaiman.sunnyreader.testcase.data.db;

import com.setiawanpaiman.sunnyreader.data.db.ItemCache;
import com.setiawanpaiman.sunnyreader.testcase.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ItemCacheTest extends BaseTest {

    @Test
    public void testConstructItemCache() throws Exception {
        ItemCache itemCache = new ItemCache(10L, "response");
        assertEquals(10L, itemCache.getId());
        assertEquals("response", itemCache.getResponse());
    }
}
