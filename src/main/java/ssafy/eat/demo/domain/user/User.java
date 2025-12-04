package ssafy.eat.demo.domain.user;

import jakarta.persistence.*;
import lombok.*;
import ssafy.eat.demo.domain.restaurant.Restaurant;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // Table name to avoid conflict with 'user' keyword in some DBs or SQL reserved words
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    // ManyToMany relationship with Restaurant for wishlist
    // FetchType.LAZY는 기본값이지만 명시적으로 지정하여 지연 로딩임을 강조
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_wishlist", // Junction table name (연결 테이블 이름)
            joinColumns = @JoinColumn(name = "user_id"), // Column for User in junction table (User 엔티티의 ID)
            inverseJoinColumns = @JoinColumn(name = "restaurant_id") // Column for Restaurant in junction table (Restaurant 엔티티의 ID)
    )
    @Builder.Default
    private Set<Restaurant> wishlist = new HashSet<>();
    // 찜 목록 관리를 위한 헬퍼 메서드
    // 현재 인메모리로 써서 기능안쓰지만, db연결하면 기능 쓰면 됨
    public void addRestaurantToWishlist(Restaurant restaurant) {
        if (this.wishlist == null) {
            this.wishlist = new HashSet<>();
        }
        this.wishlist.add(restaurant);
    }

    public void removeRestaurantFromWishlist(Restaurant restaurant) {
        if (this.wishlist != null) {
            this.wishlist.remove(restaurant);
        }
    }
}
