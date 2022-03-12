package com.example.invoiceaccounting.converter;

import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import com.example.invoiceaccounting.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceConverter {

    InvoiceConverter INSTANCE = Mappers.getMapper(InvoiceConverter.class);

    CreateInvoiceDTO convertInvoiceToCreateInvoiceDTO(Invoice invoice);

    Invoice convertCreateInvoiceDTOToInvoice(CreateInvoiceDTO createInvoiceDTO);

    ResponseInvoiceDTO convertInvoiceToResponseInvoiceDTO(Invoice invoice);

    Invoice convertResponseInvoiceDTOToInvoice(ResponseInvoiceDTO responseInvoiceDTO);

    List<ResponseInvoiceDTO> convertInvoiceToResponseInvoiceDTOs(List<Invoice> invoices);

}
