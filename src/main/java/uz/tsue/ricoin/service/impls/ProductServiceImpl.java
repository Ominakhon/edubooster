package uz.tsue.ricoin.service.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;
import uz.tsue.ricoin.dto.ProductDto;
import uz.tsue.ricoin.entity.Product;
import uz.tsue.ricoin.exceptions.InsufficientStockException;
import uz.tsue.ricoin.repository.ProductRepository;
import uz.tsue.ricoin.service.interfaces.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MessageSource messageSource;

    @Override
    public ProductDto get(Long id, HttpServletRequest request) {
        Product product = findById(id, request);
        return getProductDtoFromProduct(product);
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> all = productRepository.findAll();
        List<ProductDto> products = new ArrayList<>();
        for (Product product : all) {
            products.add(getProductDtoFromProduct(product));
        }
        return products;
    }

    @Override
    public ProductDto create(ProductDto productDto, MultipartFile file) {
        Product product = getProductFromProductDto(productDto);
        productRepository.save(product);
        return getProductDtoFromProduct(product);
    }

    @Override
    public void update(Long id, ProductDto productDto, HttpServletRequest request) {
        Product product = findById(id, request);
        Optional.ofNullable(productDto.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
        if (productDto.getPrice() != 0)
            product.setPrice(productDto.getPrice());
//        Optional.ofNullable(productDto.getImageUrl()).ifPresent(product::setImageUrl);

        productRepository.save(product);
    }

    @Override
    public void remove(Long id, HttpServletRequest request) {
        productRepository.delete(findById(id, request));
    }

    @Override
    public boolean isStockAvailable(Product product, int requiredQuantity) {
        return product.getAvailableQuantity() >= requiredQuantity;
    }

    @Override
    public void addProductQuantity(Product product, int inputQuantity) {
        product.setAvailableQuantity(product.getAvailableQuantity() + inputQuantity);
        productRepository.save(product);
    }

    @Override
    public Product findById(Long id, HttpServletRequest request) {

        return productRepository.findById(id).orElseThrow(() ->
                new InsufficientStockException(
                        messageSource.getMessage(
                                "application.exception.notification.ProductNotFound",
                                null,
                                RequestContextUtils.getLocale(request)
                        )
                ));
    }

    private ProductDto getProductDtoFromProduct(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImgUrl())
                .price(product.getPrice())
                .availableQuantity(product.getAvailableQuantity())
                .build();
    }


    private Product getProductFromProductDto(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .imgUrl(productDto.getImageUrl())
                .availableQuantity(productDto.getAvailableQuantity())
                .build();
    }


}
