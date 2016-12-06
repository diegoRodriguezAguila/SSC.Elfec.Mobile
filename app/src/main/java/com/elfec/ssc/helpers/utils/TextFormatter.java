package com.elfec.ssc.helpers.utils;

public class TextFormatter {

    public static String capitalize(String line) {
        if (line == null) return null;
        StringBuilder result = new StringBuilder();
        String[] words = line.split(" ");
        for (String word : words) {
            if (word.length() > 0)
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
        }
        return result.toString();
    }
}
