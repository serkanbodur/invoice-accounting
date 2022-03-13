package com.example.invoiceaccounting.controller;

import com.example.invoiceaccounting.dto.CustomResponseMessage;
import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import com.example.invoiceaccounting.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Value("${constants.message.accept}")
    private String ACCEPTEDMESSAGE;

    @Value("${constants.message.reject}")
    private String REJECTEDMESSAGE;

    @Value("${constants.message.fail}")
    private String FAILEDMESSAGE;


    @PostMapping()
    public ResponseEntity<CustomResponseMessage> save(@RequestBody CreateInvoiceDTO createInvoiceDTO) {

        var customResponseMessage = new CustomResponseMessage();

        if (invoiceService.isEmailInUseByDifferentUser(createInvoiceDTO)) {
            customResponseMessage.setMessage(FAILEDMESSAGE);
            customResponseMessage.setHttpStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(customResponseMessage, HttpStatus.BAD_REQUEST);
        }

        var responseInvoiceDTO = invoiceService.save(createInvoiceDTO);
        
        if(responseInvoiceDTO.getInvoiceStatus().equals(EnumInvoiceStatus.APPROVED)) {
            customResponseMessage.setMessage(ACCEPTEDMESSAGE);
            customResponseMessage.setHttpStatus(HttpStatus.ACCEPTED);
        }
        else {
            customResponseMessage.setMessage(REJECTEDMESSAGE);
            customResponseMessage.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
        }
        customResponseMessage.setContent(responseInvoiceDTO);
        return new ResponseEntity<>(customResponseMessage, HttpStatus.OK);
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
