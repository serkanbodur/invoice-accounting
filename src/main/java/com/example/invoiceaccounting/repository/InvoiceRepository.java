package com.example.invoiceaccounting.repository;

import com.example.invoiceaccounting.entity.Invoice;
import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByInvoiceStatus(EnumInvoiceStatus invoiceStatus);
}
