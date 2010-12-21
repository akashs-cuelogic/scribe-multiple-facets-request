package org.scribe.utils;

import java.util.*;

import org.scribe.model.ParamInfo;

/**
 * Utils for {@link Map} manipulation
 * 
 * @author Pablo Fernandez
 */
public class MapUtils {

    /**
     * Sorts a Map
     * 
     * @param map unsorted map
     * @return sorted map
     */
    public static final List<ParamInfo> sort(List<ParamInfo> map) {
        Preconditions.checkNotNull(map, "Cannot sort a null object.");

        List<ParamInfo> sorted = new ArrayList<ParamInfo>();
        List<String> keys = new ArrayList<String>();
        for (ParamInfo paramInfo : map) {
            keys.add(paramInfo.getKey());
        }
        keys = getSortedKeys(keys);

        for (String key : keys) {
            for (ParamInfo paramInfo : map) {
                if (key.equals(paramInfo.getKey()) && (paramInfo.getIsUsed() == false)) {
                    sorted.add(paramInfo);
                    paramInfo.setIsUsed(true);
                }
            }
        }
        //    for (String key : getSortedKeys(map))
        //    {
        //      sorted.put(key, map.get(key));
        //    }
        return sorted;
    }

    private static List<String> getSortedKeys(List<String> map) {
        Collections.sort(map);
        return map;
    }
}
