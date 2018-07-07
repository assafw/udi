package com.assafw.tests.udi.utils;

import com.assafw.udi.utils.CollectionUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


@RunWith(DataProviderRunner.class)
public class CollectionUtilsTests {

    @DataProvider
    public static Object[][] getColData() {
        return new Object[][]{
                {null, true},
                {new ArrayList<>(), true},
                {new ArrayList<>(), true},
                {Arrays.asList("Test"), false},
                {Arrays.asList(1.2, 2.3), false}
        };
    }

    @DataProvider
    public static Object[][] getArrayData() {
        return new Object[][]{
                {new String[0], true},
                {new String[1], false},
                {null, true},
                {new Integer[2], false}
        };
    }

    @Test
    @UseDataProvider("getColData")
    public <T> void testIsNullOrEmptyCollection(Collection<T> col, boolean expectedResult) {
        assertThat(CollectionUtils.isNullOrEmpty(col), is(expectedResult));
    }

    @Test
    @UseDataProvider("getArrayData")
    public <T> void testIsNullOrEmptyCollection(T[] col, boolean expectedResult) {
        assertThat(CollectionUtils.isNullOrEmpty(col), is(expectedResult));
    }
}