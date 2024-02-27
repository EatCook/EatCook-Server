package com.itcook.cooking.domain.common.utils;

import java.util.Random;

public class RandomCodeUtils {

    public static String generateRandomCode() {
        // 랜덤 코드를 생성할 문자열
        String characters = "0123456789";

        // 랜덤 코드를 저장할 StringBuilder
        StringBuilder randomCodeBuilder = new StringBuilder(6);

        // Random 객체 생성
        Random random = new Random();

        // 6자리 랜덤 코드 생성
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomCodeBuilder.append(randomChar);
        }

        return randomCodeBuilder.toString();
    }

}
