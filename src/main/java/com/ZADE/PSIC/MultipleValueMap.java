package com.ZADE.PSIC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MultipleValueMap {
    public static void addValueToKey(Map<String, List<String>> map, String key, String value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
