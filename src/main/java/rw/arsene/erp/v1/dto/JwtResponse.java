package rw.arsene.erp.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String code;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String code, String email, List<String> roles) {
        this.token = accessToken;
        this.code = code;
        this.email = email;
        this.roles = roles;
    }
}
