package ssafy.eat.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.eat.demo.domain.user.User; // Corrected: Added back User import
import ssafy.eat.demo.dto.user.LoginResponse;
import ssafy.eat.demo.dto.restaurant.RestaurantDetailDto; // Corrected: Added RestaurantDetailDto import
import ssafy.eat.demo.dto.restaurant.RestaurantWishlistDto;
import ssafy.eat.demo.dto.user.UserLoginRequest;
import ssafy.eat.demo.dto.user.UserRegisterRequest;
import ssafy.eat.demo.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request.getUsername(), request.getPassword());
            return new ResponseEntity<>(new LoginResponse(registeredUser.getId(), registeredUser.getUsername()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        Optional<User> userOptional = userService.login(request.getUsername(), request.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(new LoginResponse(user.getId(), user.getUsername()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/{userId}/wishlist/{restaurantId}")
    public ResponseEntity<?> addRestaurantToWishlist(@PathVariable Long userId, @PathVariable Long restaurantId) {
        try {
            userService.addRestaurantToWishlist(userId, restaurantId);
            return new ResponseEntity<>("Restaurant added to wishlist successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}/wishlist/{restaurantId}")
    public ResponseEntity<?> removeRestaurantFromWishlist(@PathVariable Long userId, @PathVariable Long restaurantId) {
        try {
            userService.removeRestaurantFromWishlist(userId, restaurantId);
            return new ResponseEntity<>("Restaurant removed from wishlist successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<?> getUserWishlist(@PathVariable Long userId) {
        try {
            Set<RestaurantDetailDto> wishlist = userService.getUserWishlist(userId);
            // Convert RestaurantDetailDto objects to RestaurantWishlistDto for API response
            Set<RestaurantWishlistDto> wishlistDtos = wishlist.stream()
                    .map(this::convertToRestaurantWishlistDto)
                    .collect(Collectors.toSet());
            return new ResponseEntity<>(wishlistDtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Helper method to convert RestaurantDetailDto to RestaurantWishlistDto
    private RestaurantWishlistDto convertToRestaurantWishlistDto(RestaurantDetailDto restaurantDetailDto) {
        return new RestaurantWishlistDto(
            restaurantDetailDto.getId(),
            restaurantDetailDto.getName(),
            restaurantDetailDto.getAddress(),
            restaurantDetailDto.getCuisineType(),
            restaurantDetailDto.getLatitude(),
            restaurantDetailDto.getLongitude()
        );
    }
}
