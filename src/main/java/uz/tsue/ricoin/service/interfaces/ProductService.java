package uz.tsue.ricoin.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import uz.tsue.ricoin.dto.ProductDto;
import uz.tsue.ricoin.entity.Product;

import java.util.List;

public interface ProductService {

//    ProductDto get(Long id);

    ProductDto get(Long id, HttpServletRequest request);

    List<ProductDto> getAll();

//    Product findById(Long id);

    ProductDto create(ProductDto productDto,  MultipartFile file);

//    void update(Long id, ProductDto productDto);

    void update(Long id, ProductDto productDto, HttpServletRequest request);

//    void remove(Long id);

    void remove(Long id, HttpServletRequest request);

    boolean isStockAvailable(Product product, int requiredQuantity);

    void addProductQuantity(Product product, int inputQuantity);

    Product findById(Long id, HttpServletRequest request);
}
