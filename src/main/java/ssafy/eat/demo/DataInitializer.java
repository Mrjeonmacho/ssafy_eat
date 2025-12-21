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
    public void run(String... args) {

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

        restaurants.add(Restaurant.builder()
                .name("사위식당 역삼점")
                .address("서울 강남구 테헤란로19길 7")
                .description("국밥과 수육이 유명한 한식집")
                .cuisineType("한식")
                .latitude(37.5043)
                .longitude(127.0412)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.8))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.7))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.9))
                .build());

        restaurants.add(Restaurant.builder()
                .name("역삼동북어집")
                .address("서울 강남구 테헤란로25길 34")
                .description("북엇국 전문, 점심 직장인 맛집")
                .cuisineType("한식")
                .latitude(37.5038)
                .longitude(127.0401)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.6))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.5))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.7))
                .build());

        restaurants.add(Restaurant.builder()
                .name("불이아 역삼점")
                .address("서울 강남구 테헤란로 205")
                .description("마라 요리와 중식")
                .cuisineType("중식")
                .latitude(37.5029)
                .longitude(127.0394)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.2))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.3))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.1))
                .build());

        restaurants.add(Restaurant.builder()
                .name("후라토식당 역삼점")
                .address("서울 강남구 논현로85길 5-8")
                .description("덮밥과 일본 가정식")
                .cuisineType("일식")
                .latitude(37.5012)
                .longitude(127.0369)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.7))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.6))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.8))
                .build());

        restaurants.add(Restaurant.builder()
                .name("진가와 역삼점")
                .address("서울 강남구 테헤란로8길 22")
                .description("일본식 덮밥 전문점")
                .cuisineType("일식")
                .latitude(37.5040)
                .longitude(127.0406)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.4))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.5))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.3))
                .build());

        restaurants.add(Restaurant.builder()
                .name("하남돼지집 역삼점")
                .address("서울 강남구 언주로93길 29")
                .description("삼겹살, 목살 구이")
                .cuisineType("고기")
                .latitude(37.5026)
                .longitude(127.0391)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.9))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.8))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.9))
                .build());

        restaurants.add(Restaurant.builder()
                .name("호보식당")
                .address("서울 강남구 논현로85길 43")
                .description("고기 정식과 한식")
                .cuisineType("한식")
                .latitude(37.5015)
                .longitude(127.0381)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.1))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.0))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.2))
                .build());

        restaurants.add(Restaurant.builder()
                .name("을지다락 강남점")
                .address("서울 강남구 강남대로96길 22")
                .description("한식 퓨전 덮밥")
                .cuisineType("한식")
                .latitude(37.5036)
                .longitude(127.0408)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.5))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.4))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.6))
                .build());

        restaurants.add(Restaurant.builder()
                .name("육전식당 역삼점")
                .address("서울 강남구 테헤란로8길 11-4")
                .description("삼겹살과 김치")
                .cuisineType("고기")
                .latitude(37.5045)
                .longitude(127.0409)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.8))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.9))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.7))
                .build());

        restaurants.add(Restaurant.builder()
                .name("대우부대찌개 역삼점")
                .address("서울 강남구 테헤란로25길 34")
                .description("부대찌개 전문점")
                .cuisineType("한식")
                .latitude(37.5037)
                .longitude(127.0400)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.3))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.2))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.4))
                .build());

        restaurants.add(Restaurant.builder()
                .name("명동칼국수 역삼점")
                .address("서울 강남구 역삼로 137")
                .description("칼국수와 만두")
                .cuisineType("한식")
                .latitude(37.5019)
                .longitude(127.0372)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.6))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.5))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.7))
                .build());

        restaurants.add(Restaurant.builder()
                .name("역삼 우동가")
                .address("서울 강남구 논현로85길 12")
                .description("우동, 돈카츠")
                .cuisineType("일식")
                .latitude(37.5017)
                .longitude(127.0375)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.4))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.3))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.5))
                .build());

        restaurants.add(Restaurant.builder()
                .name("스시마이우 역삼점")
                .address("서울 강남구 테헤란로19길 13")
                .description("초밥 전문점")
                .cuisineType("일식")
                .latitude(37.5041)
                .longitude(127.0410)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.7))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.8))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.6))
                .build());

        restaurants.add(Restaurant.builder()
                .name("버거파크 역삼점")
                .address("서울 강남구 테헤란로 223")
                .description("수제버거")
                .cuisineType("양식")
                .latitude(37.5051)
                .longitude(127.0420)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.5))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.6))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.4))
                .build());

        restaurants.add(Restaurant.builder()
                .name("바스버거 역삼점")
                .address("서울 강남구 논현로85길 58")
                .description("수제 햄버거")
                .cuisineType("양식")
                .latitude(37.5014)
                .longitude(127.0387)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.6))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.7))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.5))
                .build());

        restaurants.add(Restaurant.builder()
                .name("포메인 역삼점")
                .address("서울 강남구 테헤란로 201")
                .description("베트남 쌀국수")
                .cuisineType("아시아")
                .latitude(37.5024)
                .longitude(127.0383)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.3))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.2))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.4))
                .build());

        restaurants.add(Restaurant.builder()
                .name("미도인 역삼점")
                .address("서울 강남구 강남대로94길 10")
                .description("덮밥과 스테이크")
                .cuisineType("일식")
                .latitude(37.5033)
                .longitude(127.0404)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.7))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.6))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.8))
                .build());

        restaurants.add(Restaurant.builder()
                .name("백암왕순대 역삼점")
                .address("서울 강남구 역삼로 110")
                .description("순대국 전문점")
                .cuisineType("한식")
                .latitude(37.5030)
                .longitude(127.0380)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.8))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.7))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.9))
                .build());

        restaurants.add(Restaurant.builder()
                .name("청년다방 역삼점")
                .address("서울 강남구 논현로85길 13")
                .description("떡볶이와 분식")
                .cuisineType("분식")
                .latitude(37.5018)
                .longitude(127.0371)
                .naverRating(new ssafy.eat.demo.domain.restaurant.NaverRating(4.2))
                .kakaoRating(new ssafy.eat.demo.domain.restaurant.KakaoRating(4.1))
                .googleRating(new ssafy.eat.demo.domain.restaurant.GoogleRating(4.3))
                .build());

        restaurantRepository.saveAll(restaurants);
    }
}
