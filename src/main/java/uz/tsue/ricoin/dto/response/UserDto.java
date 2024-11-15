package uz.tsue.ricoin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.tsue.ricoin.entity.Order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link uz.tsue.ricoin.entity.User}
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String groupName;
    private String phoneNumber;
    private int balance;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<Order> orders;
}