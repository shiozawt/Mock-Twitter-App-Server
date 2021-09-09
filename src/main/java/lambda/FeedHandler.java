package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.FeedServiceImpl;
import service.request.FeedRequest;
import service.response.FeedResponse;

public class FeedHandler implements RequestHandler <FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        FeedServiceImpl service = new FeedServiceImpl();
        return service.getFeed(request);
    }
}
