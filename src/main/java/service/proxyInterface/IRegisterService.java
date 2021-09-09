package service.proxyInterface;

import java.io.IOException;

import net.TweeterRemoteException;
import service.request.RegisterRequest;
import service.response.LoginResponse;

public interface IRegisterService {
    LoginResponse register(RegisterRequest request) throws IOException, TweeterRemoteException;
}