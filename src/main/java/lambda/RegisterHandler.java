package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.RegisterServiceImpl;
import service.request.RegisterRequest;
import service.response.LoginResponse;

public class RegisterHandler implements RequestHandler<RegisterRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(RegisterRequest registerRequest, Context context) {
        RegisterServiceImpl registerService = new RegisterServiceImpl();
        return registerService.register(registerRequest);
    }
}