package ssafy.eat.demo.dto.restaurant;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


//   * RestaurantDto.java
//   * 목적: 맛집 목록을 조회할 때 사용하는 간략한 정보의 DTO
//   * 필드: id, name, address, cuisineType, naverRating, kakaoRating, googleRating, latitude, longitude
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private Double naverRating;
    private Double kakaoRating;
    private Double googleRating;
    private Double latitude;
    private Double longitude;
}
