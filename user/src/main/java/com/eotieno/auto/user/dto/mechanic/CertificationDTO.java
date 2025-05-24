package com.eotieno.auto.user.dto.mechanic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {
    private String id;
    @NotBlank(message = "Certification name is required")
    private String name;
    @NotBlank(message = "Issuing organization is required")
    private String issuingOrganization;
    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;
    private LocalDate expirationDate;
    @NotBlank(message = "Certificate number is required")
    private String certificateNumber;
}