package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.IsFollowingRequest;
import service.response.IsFollowingResponse;

import java.io.IOException;

public interface IIsFollowingService {
    IsFollowingResponse isFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException;
}
