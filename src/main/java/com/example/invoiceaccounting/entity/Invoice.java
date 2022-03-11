package com.example.invoiceaccounting.entity;

import com.example.invoiceaccounting.enums.EnumInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice implements Serializable {

    @SequenceGenerator(name = "generator", sequenceName = "invoice_id_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_status")
    private EnumInvoiceStatus invoiceStatus;
}
