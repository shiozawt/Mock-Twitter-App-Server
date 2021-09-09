package service.proxyInterface;

import java.io.IOException;

import net.TweeterRemoteException;
import service.request.LoginRequest;
import service.response.LoginResponse;

public interface ILoginService {
    LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException;
}