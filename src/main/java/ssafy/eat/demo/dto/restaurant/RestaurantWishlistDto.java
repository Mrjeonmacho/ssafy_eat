package ssafy.eat.demo.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantWishlistDto {
    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private Double latitude;
    private Double longitude;
}
