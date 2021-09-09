package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.StoryServiceImpl;
import service.request.StoryRequest;
import service.response.StoryResponse;

public class StoryHandler implements RequestHandler<StoryRequest, StoryResponse>  {

    @Override
    public StoryResponse handleRequest(StoryRequest request, Context context) {
        StoryServiceImpl service = new StoryServiceImpl();
        return service.getStory(request);
    }
}
