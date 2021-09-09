package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.UserInfoRequest;
import service.response.UserInfoResponse;

import java.io.IOException;

public interface IUserInfoService {
    UserInfoResponse getUserInfo(UserInfoRequest request) throws IOException, TweeterRemoteException;
}
