package dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateInvoiceDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal amount;
    private String productName;
    private String billNumber;
}
