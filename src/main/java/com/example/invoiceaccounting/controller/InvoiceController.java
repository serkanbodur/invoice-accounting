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

    @PostMapping(value ="")
    public ResponseEntity<ResponseInvoiceDTO> save(@RequestBody CreateInvoiceDTO createInvoiceDTO) {
        var responseInvoiceDTO = invoiceService.save(createInvoiceDTO);
        return new ResponseEntity<>(responseInvoiceDTO, HttpStatus.CREATED);
    }

}
