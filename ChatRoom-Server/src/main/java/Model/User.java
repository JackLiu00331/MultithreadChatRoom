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
    private String password;
    private LocalDateTime registeredTime;
    private LocalDateTime lastLoginTime;

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.registeredTime = LocalDateTime.now();
    }
}
