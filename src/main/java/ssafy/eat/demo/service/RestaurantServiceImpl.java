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
        double naverRating = Optional.ofNullable(r.getNaverRating()).map(nr -> nr.getRating()).orElse(0.0);
        double kakaoRating = Optional.ofNullable(r.getKakaoRating()).map(kr -> kr.getRating()).orElse(0.0);
        double googleRating = Optional.ofNullable(r.getGoogleRating()).map(gr -> gr.getRating()).orElse(0.0);
        return new RestaurantDto(r.getId(), r.getName(), r.getAddress(), r.getCuisineType(), naverRating, kakaoRating, googleRating, r.getLatitude(), r.getLongitude());
    }
    
    private RestaurantDetailDto mapToDetailDto(Restaurant r) {
        double naverRating = Optional.ofNullable(r.getNaverRating()).map(nr -> nr.getRating()).orElse(0.0);
        double kakaoRating = Optional.ofNullable(r.getKakaoRating()).map(kr -> kr.getRating()).orElse(0.0);
        double googleRating = Optional.ofNullable(r.getGoogleRating()).map(gr -> gr.getRating()).orElse(0.0);
        return new RestaurantDetailDto(r.getId(), r.getName(), r.getAddress(), r.getPhone(), r.getDescription(), r.getCuisineType(), naverRating, kakaoRating, googleRating, r.getLatitude(), r.getLongitude());
    }
}
