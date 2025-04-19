package com.eotieno.auto.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
@Builder
@Data
public class UserResponse {
    private Long id;
    private String email;
    private Set<String> roles;
}
