package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.FollowButtonRequest;
import service.response.FollowButtonResponse;
import java.io.IOException;

public interface IFollowButtonService {
    FollowButtonResponse follow(FollowButtonRequest request) throws IOException, TweeterRemoteException;
}
