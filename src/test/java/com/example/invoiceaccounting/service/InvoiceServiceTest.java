package com.example.invoiceaccounting.service;

import com.example.invoiceaccounting.converter.InvoiceConverter;
import com.example.invoiceaccounting.dto.invoice.CreateInvoiceDTO;
import com.example.invoiceaccounting.dto.invoice.ResponseInvoiceDTO;
import com.example.invoiceaccounting.entity.Invoice;
import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import com.example.invoiceaccounting.repository.InvoiceRepository;
import com.example.invoiceaccounting.service.impl.InvoiceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    private static Double LIMIT = 200.0;

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

    @Test
    public void shouldValidateUserInvoiceStatusIsRejectWhenUserInvoicesStatusApprovedSumAmountGreaterThanLimit() {

        var firstInvoice = new Invoice(1L, "firstName", "lastname", "email", 100.0, "pd1", "1234", EnumInvoiceStatus.APPROVED);
        var secondInvoice = new Invoice(2L, "firstName", "lastname", "email", 100.0, "pd2", "1235", EnumInvoiceStatus.APPROVED);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(firstInvoice);
        invoices.add(secondInvoice);

        when(invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED)).thenReturn(invoices);

        CreateInvoiceDTO createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setEmail(firstInvoice.getEmail());
        createInvoiceDTO.setFirstName(firstInvoice.getFirstName());
        createInvoiceDTO.setLastName(firstInvoice.getLastName());
        createInvoiceDTO.setInvoiceNumber("1111");
        createInvoiceDTO.setAmount(0.01);
        createInvoiceDTO.setProductName("pd2");

        var responseInvoice = InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO);

        var sumAmount = invoiceService.sumAmount(invoices, createInvoiceDTO);

        if (sumAmount > LIMIT) {
            responseInvoice.setInvoiceStatus(EnumInvoiceStatus.REJECT);
        }
        responseInvoice.setId(3L);
        when(invoiceRepository.save(responseInvoice)).thenReturn(responseInvoice);

        assertThat(sumAmount, greaterThan(LIMIT));
        assertEquals(responseInvoice.getInvoiceStatus(), EnumInvoiceStatus.REJECT);

    }

    @Test
    public void shouldValidateUserInvoiceStatusIsApprovedWhenUserInvoicesStatusApprovedSumAmountNotGreaterThanLimit() {

        var firstInvoice = new Invoice(1L, "firstName", "lastname", "email", 100.0, "pd1", "1234", EnumInvoiceStatus.APPROVED);
        var secondInvoice = new Invoice(2L, "firstName", "lastname", "email", 99.0, "pd2", "1235", EnumInvoiceStatus.APPROVED);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(firstInvoice);
        invoices.add(secondInvoice);

        when(invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED)).thenReturn(invoices);

        CreateInvoiceDTO createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setEmail(firstInvoice.getEmail());
        createInvoiceDTO.setFirstName(firstInvoice.getFirstName());
        createInvoiceDTO.setLastName(firstInvoice.getLastName());
        createInvoiceDTO.setInvoiceNumber("1111");
        createInvoiceDTO.setAmount(1.0);
        createInvoiceDTO.setProductName("pd2");

        var responseInvoice = InvoiceConverter.INSTANCE.convertCreateInvoiceDTOToInvoice(createInvoiceDTO);

        var sumAmount = invoiceService.sumAmount(invoices, createInvoiceDTO);

        if (sumAmount <= LIMIT) {
            responseInvoice.setInvoiceStatus(EnumInvoiceStatus.APPROVED);
        }
        responseInvoice.setId(3L);
        when(invoiceRepository.save(responseInvoice)).thenReturn(responseInvoice);

        assertThat(sumAmount, lessThanOrEqualTo(LIMIT));
        assertEquals(responseInvoice.getInvoiceStatus(), EnumInvoiceStatus.APPROVED);
    }

    @Test
    public void shouldValidateFindAllApprovedInvoices() {
        List<Invoice> invoices = new ArrayList<Invoice>();

        var firstInvoice = new Invoice(1L, "firstName", "lastname", "email", 100.0, "pd1", "1234", EnumInvoiceStatus.APPROVED);
        var secondInvoice = new Invoice(2L, "firstName", "lastname", "email", 99.0, "pd2", "1235", EnumInvoiceStatus.APPROVED);
        var rejectedInvoice = new Invoice(3L, "firstName", "lastname", "email", 99.0, "pd3", "1236", EnumInvoiceStatus.REJECT);

        invoices.add(firstInvoice);
        invoices.add(secondInvoice);
        invoices.add(rejectedInvoice);

        when(invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED)).thenReturn(invoices.stream().filter(i -> i.getInvoiceStatus().equals(EnumInvoiceStatus.APPROVED)).collect(Collectors.toList()));

        List<ResponseInvoiceDTO> approveInvoicesDTOs = invoiceService.findAllApproved();
        var approveInvoiceList = InvoiceConverter.INSTANCE.convertResponseInvoiceDTOsToInvoices(approveInvoicesDTOs);

        assertEquals(2, approveInvoiceList.size());

        assertEquals(invoices.get(0), approveInvoiceList.get(0));
        assertEquals(invoices.get(1), approveInvoiceList.get(1));

        verify(invoiceRepository, times(1)).findAllByInvoiceStatus(EnumInvoiceStatus.APPROVED);
    }

    @Test
    public void shouldValidateFindAllRejectedInvoices() {
        List<Invoice> invoices = new ArrayList<Invoice>();

        var firstInvoice = new Invoice(1L, "firstName", "lastname", "email", 300.0, "pd1", "1234", EnumInvoiceStatus.REJECT);
        var secondInvoice = new Invoice(2L, "firstName", "lastname", "email", 400.0, "pd2", "1235", EnumInvoiceStatus.REJECT);
        var approvedInvoice = new Invoice(3L, "firstName", "lastname", "email", 99.0, "pd3", "1236", EnumInvoiceStatus.APPROVED);

        invoices.add(firstInvoice);
        invoices.add(secondInvoice);
        invoices.add(approvedInvoice);

        when(invoiceRepository.findAllByInvoiceStatus(EnumInvoiceStatus.REJECT)).thenReturn(invoices.stream().filter(i -> i.getInvoiceStatus().equals(EnumInvoiceStatus.REJECT)).collect(Collectors.toList()));

        List<ResponseInvoiceDTO> rejectedInvoicesDTOs = invoiceService.findAllRejected();
        var rejectInvoiceList = InvoiceConverter.INSTANCE.convertResponseInvoiceDTOsToInvoices(rejectedInvoicesDTOs);

        assertEquals(2, rejectInvoiceList.size());

        assertEquals(invoices.get(0), rejectInvoiceList.get(0));
        assertEquals(invoices.get(1), rejectInvoiceList.get(1));

        verify(invoiceRepository, times(1)).findAllByInvoiceStatus(EnumInvoiceStatus.REJECT);
    }

}
