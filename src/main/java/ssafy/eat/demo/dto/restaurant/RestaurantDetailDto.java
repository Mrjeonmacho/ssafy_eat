package ssafy.eat.demo.dto.restaurant;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;


//   * RestaurantDetailDto.java
//   * 목적: 단일 맛집의 상세 정보를 조회할 때 사용하는 DTO입니다.
//   * 필드: RestaurantDto의 모든 필드 + phone, description
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RestaurantDetailDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private String cuisineType;
    private Double naverRating;
    private Double kakaoRating;
    private Double googleRating;
    private Double latitude;
    private Double longitude;
}
