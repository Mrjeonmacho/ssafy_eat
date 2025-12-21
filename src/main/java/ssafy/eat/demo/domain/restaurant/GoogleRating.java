package ssafy.eat.demo.domain.restaurant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "google_ratings")
@Getter
@Setter
@NoArgsConstructor
public class GoogleRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating;

    @Column(updatable = false)
    private LocalDateTime ratedAt;

    public GoogleRating(Double rating) {
        this.rating = rating;
    }

    @PrePersist
    protected void onCreate() {
        ratedAt = LocalDateTime.now();
    }
}
