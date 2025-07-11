package com.eotieno.auto.user.dto.serviceProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedHoursDTO {
    private List<String> days;
    private String openTime;
    private String closeTime;
}