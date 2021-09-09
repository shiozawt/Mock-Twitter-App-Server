package service.response;

import java.util.Objects;
import domain.Status;
import java.util.List;

public class FeedResponse extends PagedResponse {
    private List<Status> feed;

    public FeedResponse(String message){
        super(false, message, false);
    }

    public FeedResponse(List<Status> feeds, boolean hasMorePages) {
        super(true, hasMorePages);
        this.feed = feeds;
    }

    public List<Status> getFeed() {
        return feed;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FeedResponse that = (FeedResponse) param;

        return (Objects.equals(feed, that.feed) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(feed);
    }
}
