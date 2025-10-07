package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentNoteDto {
    private Long id;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
}
