package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.FollowerServiceImpl;
import service.request.FollowerRequest;
import service.response.FollowerResponse;;

public class GetFollowerHandler implements RequestHandler<FollowerRequest, FollowerResponse> {
    @Override
    public FollowerResponse handleRequest(FollowerRequest request, Context context) {
        FollowerServiceImpl service = new FollowerServiceImpl();
        return service.getFollowers(request);
    }
}
