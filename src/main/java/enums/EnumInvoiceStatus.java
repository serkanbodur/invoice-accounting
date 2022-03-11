package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter

public enum EnumInvoiceStatus {
    APPROVED("approved"),
    REJECT("reject");

    private String invoiceStatus;

    @Override
    public String toString() {
        return invoiceStatus;
    }
}
