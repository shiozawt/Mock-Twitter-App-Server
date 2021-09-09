package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.LogoutServiceImpl;
import service.request.LogoutRequest;
import service.response.LogoutResponse;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest logoutRequest, Context context) {
        LogoutServiceImpl logoutService = new LogoutServiceImpl();
        return logoutService.logout(logoutRequest);
    }
}
