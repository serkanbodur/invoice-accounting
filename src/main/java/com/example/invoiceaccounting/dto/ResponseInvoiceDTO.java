package com.example.invoiceaccounting.dto;

import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseInvoiceDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal amount;
    private String productName;
    private String invoiceNumber;
    private EnumInvoiceStatus invoiceStatus;
}
