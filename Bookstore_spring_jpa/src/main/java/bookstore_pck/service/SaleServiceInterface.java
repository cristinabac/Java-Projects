package bookstore_pck.service;

import bookstore_pck.domain.Sale;

import java.util.List;

public interface SaleServiceInterface {

    List<Sale> getAllSales();

    void buy(Long idBook, Long idClient);


}
