package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.FeedRequest;
import service.response.FeedResponse;

import java.io.IOException;

public interface IFeedService {
    FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException;

}
