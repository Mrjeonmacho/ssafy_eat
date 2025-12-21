package ssafy.eat.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.eat.demo.domain.user.User;
import ssafy.eat.demo.dto.restaurant.RestaurantDetailDto;
import ssafy.eat.demo.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RestaurantService restaurantService;

    private final Map<Long, Set<RestaurantDetailDto>> userWishlists = new ConcurrentHashMap<>();

    public UserService(UserRepository userRepository, RestaurantService restaurantService) {
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
    }

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();
        User savedUser = userRepository.save(newUser);
        userWishlists.put(savedUser.getId(), new HashSet<>());
        return savedUser;
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                userWishlists.putIfAbsent(user.getId(), new HashSet<>());
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void addRestaurantToWishlist(Long userId, Long restaurantId) {
        userWishlists.putIfAbsent(userId, new HashSet<>());

        Optional<RestaurantDetailDto> restaurantOptional = restaurantService.getRestaurantById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            throw new IllegalArgumentException("Restaurant not found with ID: " + restaurantId);
        }
        RestaurantDetailDto restaurant = restaurantOptional.get();

        boolean added = userWishlists.get(userId).add(restaurant);
        if (!added) {
            throw new IllegalArgumentException("이미 찜한 식당입니다.");
        }
    }

    @Transactional
    public Set<RestaurantDetailDto> removeRestaurantFromWishlist(Long userId, Long restaurantId) {
        Set<RestaurantDetailDto> wishlist = userWishlists.get(userId);
        if (wishlist == null) {
            throw new IllegalArgumentException("User or wishlist not found for ID: " + userId);
        }

        wishlist.removeIf(r -> r.getId().equals(restaurantId));
        return wishlist;
    }

    public Set<RestaurantDetailDto> getUserWishlist(Long userId) {
        Set<RestaurantDetailDto> wishlist = userWishlists.get(userId);
        if (wishlist == null) {
            throw new IllegalArgumentException("User or wishlist not found for ID: " + userId);
        }
        return wishlist;
    }
}
