package com.eotieno.auto.user.model.mechanic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {
    private String day;
    private String openTime;
    private String closeTime;
    private Boolean isOpen;
}