package com.eotieno.auto.user.dto;

import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    @Pattern(
            regexp = "^(?:\\+254|254|0)7\\d{8}$",
            message = "Invalid Kenyan phone number"
    )
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    private Set<RoleType> roles;
}