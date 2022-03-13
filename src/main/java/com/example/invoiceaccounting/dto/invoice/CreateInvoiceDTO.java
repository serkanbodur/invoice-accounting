package com.example.invoiceaccounting.dto.invoice;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateInvoiceDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Double amount;
    private String productName;
    private String invoiceNumber;
}
