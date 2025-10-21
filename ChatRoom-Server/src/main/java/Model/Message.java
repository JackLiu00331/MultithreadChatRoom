package Model;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        LOGIN,
        LOGIN_SUCCESS,
        LOGIN_FAILURE,
        REGISTER,
        REGISTER_SUCCESS,
        REGISTER_FAILURE,
        CHAT,
        USER_LIST,
        REQUEST_USER_LIST,
        USER_JOIN,
        USER_LEAVE,
        LOGOUT,
        CHAT_ROOM_READY
    }

    private MessageType type;
    private String content;
    private User sender;
    private Object extraData;

    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public Message(MessageType type, User sender) {
        this.type = type;
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
