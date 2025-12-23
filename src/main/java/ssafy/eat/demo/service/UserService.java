package ssafy.eat.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.eat.demo.domain.restaurant.Restaurant;
import ssafy.eat.demo.domain.user.User;
import ssafy.eat.demo.dto.restaurant.RestaurantDetailDto;
import ssafy.eat.demo.repository.RestaurantRepository;
import ssafy.eat.demo.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository; // RestaurantRepository 주입

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(newUser);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    @Transactional
    public void addRestaurantToWishlist(Long userId, Long restaurantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));

        if (user.getWishlist().contains(restaurant)) {
            throw new IllegalArgumentException("이미 찜한 식당입니다.");
        }

        // Helper method 사용
        user.addRestaurantToWishlist(restaurant);
        // @Transactional에 의해 user의 변경사항이 자동으로 DB에 반영됩니다.
    }

    @Transactional
    public void removeRestaurantFromWishlist(Long userId, Long restaurantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));
        
        // Helper method 사용
        user.removeRestaurantFromWishlist(restaurant);
    }

    public Set<RestaurantDetailDto> getUserWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return user.getWishlist().stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toSet());
    }

    private RestaurantDetailDto convertToDetailDto(Restaurant restaurant) {
        return new RestaurantDetailDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getNaverRating() != null ? restaurant.getNaverRating().getRating() : null,
                restaurant.getKakaoRating() != null ? restaurant.getKakaoRating().getRating() : null,
                restaurant.getGoogleRating() != null ? restaurant.getGoogleRating().getRating() : null,
                restaurant.getLatitude(),
                restaurant.getLongitude()
        );
    }
}
