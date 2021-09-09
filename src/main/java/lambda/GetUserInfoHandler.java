package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.UserInfoServiceImpl;
import service.request.UserInfoRequest;
import service.response.UserInfoResponse;

public class GetUserInfoHandler implements RequestHandler<UserInfoRequest, UserInfoResponse> {

    @Override
    public UserInfoResponse handleRequest(UserInfoRequest request, Context context) {
        UserInfoServiceImpl service = new UserInfoServiceImpl();
        return service.getUserInfo(request);
    }
}
