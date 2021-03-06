package evolution.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import evolution.common.GenderEnum;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Infant on 03.10.2017.
 */
@Entity
@Table(name = "user_additional_data")
@Data
@EqualsAndHashCode(exclude = "user")
public class UserAdditionalData {

    @Id
    @Column(columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_additional_data")
    @SequenceGenerator(name = "seq_user_additional_data", sequenceName = "seq_user_additional_data_id", allocationSize = 1)
    private Long id;

    //Additional data
    //Additional data
    //Additional data
    //Additional data

    @Column(name = "username", unique = true, nullable = false, columnDefinition = "varchar(255)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "registration_date", columnDefinition = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(columnDefinition = "varchar(255)")
    private String country;

    @Column(columnDefinition = "varchar(255)")
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "is_block", columnDefinition = "boolean default false")
    private boolean isBlock;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name = "secret_key", columnDefinition = "varchar(255)", unique = true)
    private String secretKey;

    @OneToOne(mappedBy = "userAdditionalData", fetch = FetchType.LAZY)
    private User user;

    //Additional data
    //Additional data
    //Additional data
    //Additional data

    @Version
    @Column(columnDefinition = "bigint")
    private Long version;

    @JsonGetter
    public User getUser() {
        if (user != null && user.getUserAdditionalData() != null) {
            User u = new User(user.getId());
            user.getUserAdditionalData().setUser(u);
        }
        return user;
    }

    @Override
    public String toString() {
        return "UserAdditionalData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", registrationDate=" + registrationDate +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", gender=" + gender +
                ", isBlock=" + isBlock +
                ", isActive=" + isActive +
                ", secretKey='" + secretKey + '\'' +
                ", version=" + version +
                '}';
    }
}
