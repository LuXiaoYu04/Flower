package com.flowers.shopping.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EmailVerificationUtil {
    private static final Map<String, String> verificationCodes = new HashMap<>();

    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    public static void storeCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    public static boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }
}
