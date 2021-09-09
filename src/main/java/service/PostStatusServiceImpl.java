package service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import dao.AuthDAO;
import dao.StoryDAO;
import domain.AuthToken;
import net.JsonSerializer;
import service.proxyInterface.IPostStatusService;
import service.request.PostStatusRequest;
import service.response.PostStatusResponse;

public class PostStatusServiceImpl implements IPostStatusService {

    @Override
    public PostStatusResponse getPostStatus(PostStatusRequest request) {
        if(!new AuthDAO().validate(new AuthToken(request.token))) {
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        // update the story table
        String result = getStoryDAO().addStory(request);

        // Sending a request to the queue
        writeSQS(request);

        if (result.equals("")) {
            return new PostStatusResponse();
        }
        else {
            return new PostStatusResponse(result);
        }
    }

    private void writeSQS(PostStatusRequest request) {
        String queueURL = "https://sqs.us-west-2.amazonaws.com/935812087864/post_status_queue";
        String message = JsonSerializer.serialize(request);
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(message);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }

    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
