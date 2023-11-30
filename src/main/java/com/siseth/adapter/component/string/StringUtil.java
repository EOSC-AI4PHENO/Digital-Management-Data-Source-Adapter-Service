package com.siseth.adapter.component.string;

import java.util.Random;

public final class StringUtil {


    public static String generateLetters(int length) {
        return generate(97, 122, length);
    }

    public static String generate(int left, int right, int length) {

        Random random = new Random();
        return random.ints(left, right + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
