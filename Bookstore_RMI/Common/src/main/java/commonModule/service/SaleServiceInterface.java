package commonModule.service;

import commonModule.domain.Sale;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SaleServiceInterface {

    Boolean addSale(Sale sale);

    Boolean removeSale(Long id);

    List<Sale> getSales();
}
