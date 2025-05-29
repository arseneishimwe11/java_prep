package rw.arsene.erp.v1.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecretGenerator {
    
    private final JwtSecretProvider jwtSecretProvider;
    
    public void generateAndPrintSecret() {
        String secret = jwtSecretProvider.generateSecret();
        log.info("Generated JWT Secret: {}", secret);
        log.info("Add this to your application.properties:");
        log.info("app.jwtSecret={}", secret);
    }
    
    public static void main(String[] args) {
        JwtSecretProvider provider = new JwtSecretProvider();
        String secret = provider.generateSecret();
        System.out.println("Generated JWT Secret: " + secret);
        System.out.println("Add this to your application.properties:");
        System.out.println("app.jwtSecret=" + secret);
    }
}