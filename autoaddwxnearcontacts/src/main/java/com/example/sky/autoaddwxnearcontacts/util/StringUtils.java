package com.example.sky.autoaddwxnearcontacts.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sky on 3/7/2017.
 */

public final class StringUtils {
    public StringUtils() {
    }

    public static int obtainNumbers(String content) {
        if (!TextUtils.isEmpty(getNumbers(content)))
            return Integer.parseInt(getNumbers(content));
        return 0;
    }

    private static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
