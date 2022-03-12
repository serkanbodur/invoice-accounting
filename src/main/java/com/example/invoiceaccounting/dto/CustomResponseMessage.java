package com.example.invoiceaccounting.dto;

import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomResponseMessage {

    private HttpStatus httpStatus;
    private ResponseInvoiceDTO content;
    private String message;

}
