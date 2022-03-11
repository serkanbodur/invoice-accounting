package converter;

import dto.CreateInvoiceDTO;
import dto.ResponseInvoiceDTO;
import entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceConverter {

    InvoiceConverter INSTANCE = Mappers.getMapper(InvoiceConverter.class);

    CreateInvoiceDTO convertInvoiceToCreateInvoiceDTO(Invoice invoice);

    Invoice convertCreateInvoiceDTOToInvoice(CreateInvoiceDTO createInvoiceDTO);

    ResponseInvoiceDTO convertInvoiceToResponseInvoiceDTO(Invoice invoice);

    Invoice convertResponseInvoiceDTOToInvoice(ResponseInvoiceDTO responseInvoiceDTO);
}
