package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Status implements Serializable {
    private User user;
    private String date;
    private String time;
    private String content;
    private List<String> mentions;

    public Status() {}

    public Status(User user, String date, String time, String content, ArrayList<String> mentions) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.content = content;
        this.mentions = mentions;
    }

    public String getDate(){ return date; }

    public String getTime(){ return time; }

    public String getContent(){ return content; }

    public User getUser() { return user; }

    public List<String> getMentions() {
        return this.mentions;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status that = (Status) o;
        return (Objects.equals(user, that.user) &&
                Objects.equals(this.getDate(), that.getDate()) &&
                Objects.equals(this.getTime(), that.getTime()) &&
                Objects.equals(this.getContent(), that.getContent()));
    }
}