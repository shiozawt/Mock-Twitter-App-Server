package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.PostStatusServiceImpl;
import service.request.PostStatusRequest;
import service.response.PostStatusResponse;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        PostStatusServiceImpl service = new PostStatusServiceImpl();
        return service.getPostStatus(request);
    }
}
