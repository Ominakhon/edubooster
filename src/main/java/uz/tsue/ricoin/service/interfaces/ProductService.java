package uz.tsue.ricoin.service.interfaces;

import jdk.dynalink.linker.LinkerServices;
import uz.tsue.ricoin.dto.ProductDto;
import uz.tsue.ricoin.entity.Product;

import java.util.List;

public interface ProductService {

    ProductDto get(Long id);

    List<ProductDto> getAll();

    Product findById(Long id);

    void add(ProductDto productDto);

    void update(Long id, ProductDto productDto);

    void remove(Long id);

    boolean isStockAvailable(Product product, int requiredQuantity);

    void addProductQuantity(Product product, int inputQuantity);

}
