package ssafy.eat.demo.dto.restaurant;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingUpdateRequest {
    private Integer rating; // Rating value, e.g., 1 to 5
    private String comment;
}
