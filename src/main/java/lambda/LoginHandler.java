package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.request.LoginRequest;
import service.response.LoginResponse;
import service.LoginServiceImpl;

public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        LoginServiceImpl loginService = new LoginServiceImpl();
        return loginService.login(loginRequest);
    }
}