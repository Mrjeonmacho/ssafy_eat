package ssafy.eat.demo.service;

import ssafy.eat.demo.dto.restaurant.RestaurantDto;
import ssafy.eat.demo.dto.restaurant.RestaurantDetailDto;
import ssafy.eat.demo.dto.restaurant.RatingUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<RestaurantDto> getAllRestaurants();
    Optional<RestaurantDetailDto> getRestaurantById(Long id);
    RestaurantDetailDto addRating(Long restaurantId, RatingUpdateRequest request);
}
