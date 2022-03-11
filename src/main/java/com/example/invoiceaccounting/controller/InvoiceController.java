package com.example.invoiceaccounting.controller;

import com.example.invoiceaccounting.dto.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.ResponseInvoiceDTO;
import com.example.invoiceaccounting.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping()
    public ResponseEntity<ResponseInvoiceDTO> save(@RequestBody CreateInvoiceDTO createInvoiceDTO) {
        var responseInvoiceDTO = invoiceService.save(createInvoiceDTO);
        return new ResponseEntity<>(responseInvoiceDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/approved")
    public ResponseEntity<List<ResponseInvoiceDTO>> getApprovedInvoices() {
        var approvedInvoiceDTOs = invoiceService.findAllApproved();
        return new ResponseEntity<>(approvedInvoiceDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/rejected")
    public ResponseEntity<List<ResponseInvoiceDTO>> getRejectedInvoices(){
        var rejectedInvoiceDTOs = invoiceService.findAllRejected();
        return new ResponseEntity<>(rejectedInvoiceDTOs, HttpStatus.OK);
    }
}
