package com.example.invoiceaccounting.service.impl;

import com.example.invoiceaccounting.converter.InvoiceConverter;
import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import com.example.invoiceaccounting.entity.Invoice;
import com.example.invoiceaccounting.exception.EmailIsAlreadyInUseException;
import com.example.invoiceaccounting.repository.InvoiceRepository;
import com.example.invoiceaccounting.service.InvoiceService;
import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Value("${constants.limit}")
    private int LIMIT;

    @Override
    public ResponseInvoiceDTO save(CreateInvoiceDTO createInvoiceDTO) {

        if (isEmailInUseByDifferentUser(createInvoiceDTO)) {
            throw new EmailIsAlreadyInUseException("This email is already in use by different user");
        }

        var invoice = InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO);
        var approvedList = invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED);
        var sumAmount = sumAmount(approvedList, createInvoiceDTO);

        if (sumAmount > LIMIT) {
            invoice.setInvoiceStatus(EnumInvoiceStatus.REJECT);

        } else {
            invoice.setInvoiceStatus(EnumInvoiceStatus.APPROVED);
        }

        invoiceRepository.save(invoice);
        return InvoiceConverter.INSTANCE.convertInvoiceToResponseInvoiceDTO(invoice);
    }

    @Override
    public List<ResponseInvoiceDTO> findAllApproved() {
        var approvedInvoiceList = invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED);
        return InvoiceConverter.INSTANCE.convertInvoiceToResponseInvoiceDTOs(approvedInvoiceList);
    }

    @Override
    public List<ResponseInvoiceDTO> findAllRejected() {
        var rejectedInvoiceList = invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.REJECT);
        return InvoiceConverter.INSTANCE.convertInvoiceToResponseInvoiceDTOs(rejectedInvoiceList);
    }


    @Override
    public Boolean isEmailInUseByDifferentUser(CreateInvoiceDTO createInvoiceDTO) {
        var foundInvoice = invoiceRepository.findAllByEmail(createInvoiceDTO.getEmail());
        if(foundInvoice.isEmpty()) {
            return false;
        }
        var invoice = InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO);

        return !invoice.compareTo(foundInvoice.get(0));
    }

    @Override
    public Double sumAmount(List<Invoice> invoices, CreateInvoiceDTO createInvoiceDTO) {
        return invoices.stream()
                .filter(i -> i.getEmail().equals(createInvoiceDTO.getEmail()))
                .mapToDouble(i -> i.getAmount()).sum() + createInvoiceDTO.getAmount();
    }

}
