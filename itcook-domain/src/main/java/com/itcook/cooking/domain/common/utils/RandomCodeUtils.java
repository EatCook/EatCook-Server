package com.itcook.cooking.domain.common.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomCodeUtils {

    private static final int PASSWORD_LENGTH = 10;

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

    public static String generateTemporaryPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

}
