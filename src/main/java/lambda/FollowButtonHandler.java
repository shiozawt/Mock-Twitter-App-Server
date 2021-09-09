package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.FollowButtonServiceImpl;
import service.request.FollowButtonRequest;
import service.response.FollowButtonResponse;

public class FollowButtonHandler implements RequestHandler<FollowButtonRequest, FollowButtonResponse> {

    @Override
    public FollowButtonResponse handleRequest(FollowButtonRequest request, Context context) {
        FollowButtonServiceImpl service = new FollowButtonServiceImpl();
        return service.follow(request);
    }
}
