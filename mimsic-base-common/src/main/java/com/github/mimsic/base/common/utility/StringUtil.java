package com.github.mimsic.base.common.utility;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public StringUtil() {
    }

    public static String[] splitByCharArray(String data, char delimiter) {

        return splitByCharArray(data, delimiter, true);
    }

    public static String[] splitByCharArray(String data, char delimiter, boolean allowEmpty) {

        List<String> result = new ArrayList<>();
        StringBuilder buff = new StringBuilder();
        char[] dataArray = data.toCharArray();
        for (char c : dataArray) {
            if (c == delimiter) {
                if (allowEmpty || buff.length() > 0) {
                    result.add(buff.toString());
                    buff = new StringBuilder();
                }
            } else {
                buff.append(c);
            }
        }
        if (buff.length() > 0) {
            result.add(buff.toString());
        }
        return result.toArray(new String[0]);
    }

    public static String[] splitByIndex(String data, String separator) {

        List<String> parts = new ArrayList<>();
        String part = data;
        int i;
        int j;
        while (true) {
            i = part.indexOf(separator);
            if (i == -1) {
                parts.add(part);
                break;
            }
            j = part.length();
            parts.add(part.substring(0, i));
            part = part.substring(i + 1, j);
        }
        return parts.toArray(new String[0]);
    }
}
