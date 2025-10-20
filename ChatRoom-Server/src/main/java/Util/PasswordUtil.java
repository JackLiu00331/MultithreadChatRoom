package Util;

import cn.hutool.crypto.digest.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainPassword) {
        // Simple hashing example using SHA-256
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
