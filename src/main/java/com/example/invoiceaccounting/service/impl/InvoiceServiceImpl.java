package com.example.invoiceaccounting.service.impl;

import com.example.invoiceaccounting.converter.InvoiceConverter;
import com.example.invoiceaccounting.dto.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.ResponseInvoiceDTO;
import com.example.invoiceaccounting.repository.InvoiceRepository;
import com.example.invoiceaccounting.service.InvoiceService;
import enums.EnumInvoiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Value("${constants.limit}")
    private int LIMIT;

    @Override
    public ResponseInvoiceDTO save(CreateInvoiceDTO createInvoiceDTO) {

        var invoice = InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO);
        var approvedList = invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED);
        var sumAmount = approvedList.stream()
                .filter(i -> i.getEmail().equals(createInvoiceDTO.getEmail()))
                .mapToInt(i -> i.getAmount().intValue()+createInvoiceDTO.getAmount().intValue()).sum();

        if(sumAmount > LIMIT) {
            invoice.setInvoiceStatus(EnumInvoiceStatus.REJECT);
        }

        else {
            invoice.setInvoiceStatus(EnumInvoiceStatus.APPROVED);
        }

        invoiceRepository.save(invoice);
        return InvoiceConverter.INSTANCE.convertInvoiceToResponseInvoiceDTO(invoice);
    }

}
