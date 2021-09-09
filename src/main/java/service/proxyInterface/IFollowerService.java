package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.FollowerRequest;
import service.response.FollowerResponse;

import java.io.IOException;

public interface IFollowerService {
    FollowerResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException;
}
