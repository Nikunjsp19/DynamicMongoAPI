package com.np.DynamicMongoAPI.utils;

public class StringUtils {

    public static String camelCaseToMongo(String camelCase) {

        if (camelCase.matches("^[A-Z0-9_]+$")) {
            return camelCase; //
        }
        StringBuilder result = new StringBuilder();

        for (char ch : camelCase.toCharArray()) {
            // If the character is uppercase and it's not the first character, add an underscore before it
            if (Character.isUpperCase(ch) && !result.isEmpty()) {
                result.append('_');
            }
            result.append(Character.toUpperCase(ch));
        }

        return result.toString();
    }
}
