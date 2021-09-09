package domain;

import java.util.List;

public class FollowerQueueBatch {
    private String posterAlias;
    private String date;
    private String time;
    private String content;
    private List<String> followers;

    public FollowerQueueBatch(String posterAlias, String date, String time, String content, List<String> followers) {
        this.posterAlias = posterAlias;
        this.date = date;
        this.time = time;
        this.content = content;
        this.followers = followers;
    }

    public String getPosterAlias() {
        return posterAlias;
    }

    public void setPosterAlias(String posterAlias) {
        this.posterAlias = posterAlias;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
