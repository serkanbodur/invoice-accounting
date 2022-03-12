package com.example.invoiceaccounting.service;

import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import com.example.invoiceaccounting.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    ResponseInvoiceDTO save(CreateInvoiceDTO createInvoiceDTO);
    List<ResponseInvoiceDTO> findAllApproved();
    List<ResponseInvoiceDTO> findAllRejected();
    Boolean isEmailInUseByDifferentUser(CreateInvoiceDTO createInvoiceDTO);
    Double sumAmount(List<Invoice> invoices, CreateInvoiceDTO createInvoiceDTO);
}
