package com.disney.bmp.util;

import org.apache.commons.lang3.StringUtils;

public final class HttpHelper {

    private HttpHelper() {

    }

    public static String removeCharacters(String input) {
        input = StringUtils.remove(input, "\n");
        input = StringUtils.remove(input, "\t");
        input = StringUtils.remove(input, "\r");
        return input;
    }

}