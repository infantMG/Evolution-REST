package evolution.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import evolution.module.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by Infant on 08.11.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class FeedDTO {

    private Long id;

    private String content;

    private Date date;

    private UserDTO sender;

    private UserDTO toUser;

    private List<String> tags;

    private boolean isActive;

    public FeedDTO(Long id) {
        this.id = id;
    }
}
