package ssafy.eat.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ssafy.eat.demo.domain.restaurant.Restaurant;
import ssafy.eat.demo.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;



// db연결 안해놔서 임시 맛집 데이터
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create Restaurants using the Builder pattern
        Restaurant r1 = Restaurant.builder()
                .name("싸피반점")
                .address("서울 강남구 테헤란로 212")
                .phone("02-3429-5114")
                .description("정통 중화요리 전문점")
                .cuisineType("중식")
                .latitude(37.5085)
                .longitude(127.0456)
                .naverRatings(new ArrayList<>())
                .kakaoRatings(new ArrayList<>())
                .googleRatings(new ArrayList<>())
                .build();

        Restaurant r2 = Restaurant.builder()
                .name("멀티돈까스")
                .address("서울 강남구 역삼동")
                .phone("02-123-4567")
                .description("바삭바삭한 돈까스 맛집")
                .cuisineType("일식")
                .latitude(37.5015)
                .longitude(127.0373)
                .naverRatings(new ArrayList<>())
                .kakaoRatings(new ArrayList<>())
                .googleRatings(new ArrayList<>())
                .build();
        
        Restaurant r3 = Restaurant.builder()
                .name("알고분식")
                .address("서울 강남구 삼성동")
                .phone("02-987-6543")
                .description("추억의 분식집")
                .cuisineType("분식")
                .latitude(37.5144)
                .longitude(127.0563)
                .naverRatings(new ArrayList<>())
                .kakaoRatings(new ArrayList<>())
                .googleRatings(new ArrayList<>())
                .build();
        
        // Save all restaurants to the database
        restaurantRepository.saveAll(List.of(r1, r2, r3));

        // Note: We are not adding rating data for now to keep it simple.
        // This could be extended to save dummy ratings as well.
    }
}
