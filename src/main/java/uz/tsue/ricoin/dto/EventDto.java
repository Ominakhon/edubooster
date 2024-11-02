package uz.tsue.ricoin.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link uz.tsue.ricoin.entity.Event}
 */
@Builder
public record EventDto(Long id, String name, String description, String place, Integer price,
                       boolean isActive, String promoCode, String dateTime) implements Serializable {
}