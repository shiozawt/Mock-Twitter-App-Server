package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.IsFollowingServiceImpl;
import service.request.IsFollowingRequest;
import service.response.IsFollowingResponse;

public class IsFollowingHandler implements RequestHandler<IsFollowingRequest, IsFollowingResponse> {

    @Override
    public IsFollowingResponse handleRequest(IsFollowingRequest request, Context context) {
        IsFollowingServiceImpl service = new IsFollowingServiceImpl();
        return service.isFollowing(request);
    }
}
