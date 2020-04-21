package serverModule.serverService;

import commonModule.domain.Sale;
import commonModule.service.SaleServiceInterface;
import serverModule.entityService.SaleService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SaleServiceServer implements SaleServiceInterface {
    private SaleService saleService;
    private ExecutorService executor;

    public SaleServiceServer(SaleService saleService, ExecutorService executor) {
        this.saleService = saleService;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Boolean> addSale(Sale sale) {
        return CompletableFuture.supplyAsync(()->{
            return this.saleService.buy(sale.getBookId(),sale.getClientId());
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> removeSale(Long id) {
        return CompletableFuture.supplyAsync(()->{
            return null;
        },executor);
    }

    @Override
    public CompletableFuture<List<Sale>> getSales() {
        return CompletableFuture.supplyAsync(()->
                this.saleService.getAllSales(), executor);
    }
}
