package courage.library.authserver.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity

@Table(name="verification_token")
@Data
@NoArgsConstructor
public class VerificationTokenEntity {

    @Id
    private String id;

    private String token;
    private String tokenType;

    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    private Date expiryDate;
    private Boolean verified;

    public VerificationTokenEntity(String token, UserEntity user, String tokenType) {
        this.id = UUID.randomUUID().toString();
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
        this.expiryDate = calculateExpiryDate(60);
        this.verified = false;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
