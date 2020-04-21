package clientModule.clientService;


import commonModule.domain.Sale;
import commonModule.service.SaleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SaleServiceClient implements SaleServiceInterface{
    @Autowired
    private SaleServiceInterface saleService;

    @Override
    public Boolean addSale(Sale sale) {
        return saleService.addSale(sale);
    }

    @Override
    public Boolean removeSale(Long id) {
        return null;
    }

    @Override
    public List<Sale> getSales() {
        return saleService.getSales();
    }
}
