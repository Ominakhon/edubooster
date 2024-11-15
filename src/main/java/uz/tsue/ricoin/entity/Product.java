package uz.tsue.ricoin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer price;
    private int availableQuantity;
    private String imgUrl;

}
