package com.assafw.udi.utils;

import java.util.Collection;

public interface CollectionUtils {

    static <T> boolean isNullOrEmpty(Collection<T> col) {
        return col == null || col.isEmpty();
    }

    static <T> boolean isNullOrEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }
}
