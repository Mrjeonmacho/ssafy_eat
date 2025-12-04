package ssafy.eat.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.eat.demo.domain.restaurant.Restaurant;
import ssafy.eat.demo.dto.restaurant.RestaurantDetailDto;
import ssafy.eat.demo.dto.restaurant.RestaurantDto;
import ssafy.eat.demo.dto.restaurant.RatingUpdateRequest;
import ssafy.eat.demo.repository.RestaurantRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToSimpleDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RestaurantDetailDto> getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .map(this::mapToDetailDto);
    }

    @Override
    @Transactional
    public RestaurantDetailDto addRating(Long restaurantId, RatingUpdateRequest request) {
        // 맛집 정보를 찾습니다.
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));

        // 평점 추가 로직은 나중에 구체화합니다.
        System.out.println("addRating is not fully implemented yet. It requires rating repositories.");

        // 업데이트된 DTO를 반환합니다.
        return mapToDetailDto(restaurant);
    }
    
    private RestaurantDto mapToSimpleDto(Restaurant r) {
        // 단순화된 매핑: 현재는 평점을 0.0으로 반환합니다.
        // 향후 r.getNaverRatings() 등을 통해 평균을 계산하는 로직 추가 가능
        return new RestaurantDto(r.getId(), r.getName(), r.getAddress(), r.getCuisineType(), 0.0, 0.0, 0.0, r.getLatitude(), r.getLongitude());
    }
    
    private RestaurantDetailDto mapToDetailDto(Restaurant r) {
        // 단순화된 매핑: 현재는 평점을 0.0으로 반환합니다.
        return new RestaurantDetailDto(r.getId(), r.getName(), r.getAddress(), r.getPhone(), r.getDescription(), r.getCuisineType(), 0.0, 0.0, 0.0, r.getLatitude(), r.getLongitude());
    }
}
