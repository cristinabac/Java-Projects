package serverModule.serverService;

import commonModule.domain.Sale;
import commonModule.service.SaleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import serverModule.entityService.SaleService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SaleServiceServer implements SaleServiceInterface {
    @Autowired
    private SaleService saleService;

    @Override
    public Boolean addSale(Sale sale) {
       return saleService.buy(sale.getBookId(), sale.getClientId());
    }

    @Override
    public Boolean removeSale(Long id) {
        return null;
    }

    @Override
    public List<Sale> getSales() {
        return saleService.getAllSales();
    }
}
