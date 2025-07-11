package com.eotieno.auto.user.model.car_owner;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingInfo {
    private List<String> preferredPaymentMethod;
    private String mpesaNumber;
    private String bankName;
    private Boolean requestInvoices;
    private Boolean allowAutoPayment;
}
