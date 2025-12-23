package ssafy.eat.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ssafy.eat.demo.domain.restaurant.Restaurant;
import ssafy.eat.demo.domain.user.User;
import ssafy.eat.demo.repository.RestaurantRepository;
import ssafy.eat.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        initializeData();
    }

    private void initializeData() {
        // Initialize Restaurants if DB is empty
        if (restaurantRepository.count() == 0) {
            List<Restaurant> restaurants = new ArrayList<>();

            restaurants.add(Restaurant.builder()
                    .name("봉우리 역삼점")
                    .address("서울 강남구 논현로94길 25-3")
                    .description("정갈한 한정식과 한식")
                    .cuisineType("한식")
                    .latitude(37.5021)
                    .longitude(127.0389)
                    .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.5))
                    .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.4))
                    .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.6))
                    .build());

            // ... (all other restaurant additions)
            restaurantRepository.saveAll(restaurants);
            System.out.println("데이터베이스에 초기 맛집 데이터를 추가했습니다.");
        }

        // Initialize a default user if DB is empty
        if (userRepository.count() == 0) {
            User testUser = User.builder()
                    .username("root")
                    .password("root")
                    .build();
            userRepository.save(testUser);
            System.out.println("데이터베이스에 테스트 사용자를 추가했습니다: user / password");
        }
    }
}
