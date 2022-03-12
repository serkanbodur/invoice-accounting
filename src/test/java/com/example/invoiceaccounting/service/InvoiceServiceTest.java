package com.example.invoiceaccounting.service;

import com.example.invoiceaccounting.converter.InvoiceConverter;
import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.entity.Invoice;
import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import com.example.invoiceaccounting.repository.InvoiceRepository;
import com.example.invoiceaccounting.service.impl.InvoiceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Test
    public void shouldValidateSameUserInvoiceWhenUserEmailInUseWithSameNameAndSurname() {

        var foundInvoice = new Invoice(1L, "firstName", "lastname", "email", 100.0, "pd1", "1234", EnumInvoiceStatus.APPROVED);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(foundInvoice);

        var createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setEmail(foundInvoice.getEmail());
        createInvoiceDTO.setFirstName(foundInvoice.getFirstName());
        createInvoiceDTO.setLastName(foundInvoice.getLastName());
        createInvoiceDTO.setInvoiceNumber("1111");
        createInvoiceDTO.setAmount(50.0);
        createInvoiceDTO.setProductName("pd2");

        when(invoiceRepository.findAllByEmail(createInvoiceDTO.getEmail())).thenReturn(invoices);

        assertEquals(invoices.get(0).getEmail(), createInvoiceDTO.getEmail());
        assertEquals(invoices.get(0).getFirstName(), createInvoiceDTO.getFirstName());
        assertEquals(invoices.get(0).getLastName(), createInvoiceDTO.getLastName());

        assertTrue(invoices.get(0).compareTo(InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO)));
    }

    @Test
    public void shouldNotValidateDifferentUserInvoiceWhenUserEmailInUseWithDifferentNameOrSurname() {

        var foundInvoice = new Invoice(1L, "firstName", "lastname", "email", 100.0, "pd1", "1234", EnumInvoiceStatus.APPROVED);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(foundInvoice);

        var createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setEmail(foundInvoice.getEmail());
        createInvoiceDTO.setFirstName("dumpName");
        createInvoiceDTO.setLastName(foundInvoice.getLastName());
        createInvoiceDTO.setInvoiceNumber("1111");
        createInvoiceDTO.setAmount(50.0);
        createInvoiceDTO.setProductName("pd2");

        when(invoiceRepository.findAllByEmail(createInvoiceDTO.getEmail())).thenReturn(invoices);

        assertEquals(invoices.get(0).getEmail(), createInvoiceDTO.getEmail());
        assertEquals(invoices.get(0).getLastName(), createInvoiceDTO.getLastName());

        assertNotEquals(invoices.get(0).getFirstName(), createInvoiceDTO.getFirstName());

        assertFalse(invoices.get(0).compareTo(InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO)));
    }


}
