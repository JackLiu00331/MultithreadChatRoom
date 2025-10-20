package Model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(LocalDateTime registeredTime) {
        this.registeredTime = registeredTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
