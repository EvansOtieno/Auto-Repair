package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetailsDto {
    private Long userId;
    private String name;
    private String phone;
    private String email;
}
