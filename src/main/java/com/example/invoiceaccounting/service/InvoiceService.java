package com.example.invoiceaccounting.service;

import com.example.invoiceaccounting.dto.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.ResponseInvoiceDTO;

import java.util.List;

public interface InvoiceService {
    ResponseInvoiceDTO save(CreateInvoiceDTO createInvoiceDTO);
    List<ResponseInvoiceDTO> findAllApproved();
    List<ResponseInvoiceDTO> findAllRejected();
    Boolean isEmailInUseByDifferentUser(CreateInvoiceDTO createInvoiceDTO);
}
