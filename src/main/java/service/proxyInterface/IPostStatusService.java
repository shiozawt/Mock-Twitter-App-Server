package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.PostStatusRequest;
import service.response.PostStatusResponse;

import java.io.IOException;

public interface IPostStatusService {
    PostStatusResponse getPostStatus(PostStatusRequest request) throws IOException, TweeterRemoteException;

}
