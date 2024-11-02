package uz.tsue.ricoin.dto.response;

import lombok.*;
import uz.tsue.ricoin.entity.Product;
import uz.tsue.ricoin.entity.enums.OrderStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Product product;
    private Integer quantity;
    private Integer price;
    private OrderStatus orderStatus;

}
