package rw.arsene.erp.v1.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class JwtSecretProvider {
    
    private static final int SECRET_LENGTH = 64; // 512 bits
    
    public String generateSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretBytes = new byte[SECRET_LENGTH];
        secureRandom.nextBytes(secretBytes);
        return Base64.getEncoder().encodeToString(secretBytes);
    }
    
    public boolean isValidSecret(String secret) {
        if (secret == null || secret.trim().isEmpty()) {
            return false;
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(secret);
            return decoded.length >= 32; // At least 256 bits
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}