package commonModule.service;

import commonModule.domain.Sale;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SaleServiceInterface {
    String SERVER_HOST="localhost";
    Integer PORT_NUMBER=1234;

    String GET_SALES="getSales";
    String ADD_SALE="addSale";
    String REMOVE_SALE="removeSale";

    CompletableFuture<Boolean> addSale(Sale sale);

    CompletableFuture<Boolean> removeSale(Long id);

    CompletableFuture<List<Sale>> getSales();
}
