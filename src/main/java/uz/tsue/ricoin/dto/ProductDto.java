package uz.tsue.ricoin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private int availableQuantity;
}
