package com.elfec.ssc.helpers.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 1/9/2016.
 * utils for collections
 */
public class CollectionUtils {
    /**
     * Maps a collection to another type
     *
     * @param list   list
     * @param mapper mapper
     * @param <U>    final type
     * @param <T>    initial type
     * @return collection of final type
     */
    public static <U, T> List<U> map(List<T> list, TypeMapper<U, T> mapper) {
        if (list == null)
            return null;
        List<U> mapList = new ArrayList<>();
        for (T item : list) {
            mapList.add(mapper.map(item));
        }
        return mapList;
    }

    /**
     * Finds an item with a finder method
     *
     * @param list   list to search
     * @param finder finder, if it returns true for an item it is the item we were looking for
     * @param <T>    type
     * @return the item found, null if no item could be found
     */
    public static <T> T find(List<T> list, Finder<T> finder) {
        if (list == null)
            return null;
        for (T item : list) {
            if (finder.isItem(item))
                return item;
        }
        return null;
    }

    public interface TypeMapper<U, T> {
        U map(T obj);
    }

    public interface Finder<T> {
        boolean isItem(T item);
    }
}
