package sample;

import java.io.Serializable;

public class Message implements Serializable {

    String text;
    String username;

    public Message(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {
        return this.username + ", " + this.text;
    }

}
