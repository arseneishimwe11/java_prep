package rw.arsene.erp.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.arsene.erp.v1.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long id;
    private String code; // Employee code
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    public JwtResponse(String accessToken, Long id, String code, String email, 
                      String firstName, String lastName, Role role) {
        this.token = accessToken;
        this.id = id;
        this.code = code;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
