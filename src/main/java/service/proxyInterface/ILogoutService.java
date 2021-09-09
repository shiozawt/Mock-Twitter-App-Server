package service.proxyInterface;

import java.io.IOException;
import net.TweeterRemoteException;
import service.request.LogoutRequest;
import service.response.LogoutResponse;

public interface ILogoutService {
    LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException;
}