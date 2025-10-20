package Model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String nickname;
    private String passwordHash;
    private LocalDateTime registeredTime;
    private LocalDateTime lastLoginTime;

    public User(String username, String nickname, String passwordHash) {
        this.username = username;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.registeredTime = LocalDateTime.now();
    }
}
