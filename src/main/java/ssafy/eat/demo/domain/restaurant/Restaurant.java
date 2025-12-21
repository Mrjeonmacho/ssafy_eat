package ssafy.eat.demo.domain.restaurant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Add Builder annotation
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
    private String phone;

    @Column(length = 1000) // Adjust length as needed
    private String description;

    //음식점 타입
    private String cuisineType;

    //위치
    private Double latitude;
    private Double longitude;



    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "naver_rating_id")
    private NaverRating naverRating;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "kakao_rating_id")
    private KakaoRating kakaoRating;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "google_rating_id")
    private GoogleRating googleRating;

    public NaverRating getNaverRating() {
        return naverRating;
    }

    public KakaoRating getKakaoRating() {
        return kakaoRating;
    }

    public GoogleRating getGoogleRating() {
        return googleRating;
    }
}
