package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import domain.FollowerQueueBatch;
import net.JsonSerializer;
import service.FollowerServiceImpl;
import service.request.PostStatusRequest;

import java.util.ArrayList;
import java.util.List;

public class ConsumePostStatusHandler implements RequestHandler<SQSEvent, Void> {
    private String followerQueueURL = "https://sqs.us-west-2.amazonaws.com/935812087864/follower_queue";
    private int limit = 25;

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostStatusRequest request = JsonSerializer.deserialize(msg.getBody(), PostStatusRequest.class);

            // Use FollowerServiceImpl to load all followers of the poster
            FollowerServiceImpl service = new FollowerServiceImpl();
            List<String> followers = service.getFollowersForPostStatus(request.getPosterAlias());

            // write to the follower SQS queue
            buildBatch(followers, request);
        }
        return null;
    }

    // write the followers to the follower_queue in batches
    private void buildBatch(List<String> followers, PostStatusRequest request) {
        List<String> batchFollower = new ArrayList<String>();
        for (int i = 0; i < followers.size(); i++) {
            batchFollower.add(followers.get(i));
            if (batchFollower.size() == limit) {
                FollowerQueueBatch batch = new FollowerQueueBatch(request.getPosterAlias(), request.getDate(),
                        request.getTime(), request.getContent(), batchFollower);

                // writing to queue
                sendToSQS(batch);
                batchFollower.clear();
            }
        }

        // writing the rest that are not enough to make up to 25 to queue
        if (batchFollower.size() > 0) {
            FollowerQueueBatch batch = new FollowerQueueBatch(request.getPosterAlias(), request.getDate(),
                    request.getTime(), request.getContent(), batchFollower);

            // writing to queue
            sendToSQS(batch);
            batchFollower.clear();
        }
    }

    private void sendToSQS(FollowerQueueBatch batch) {
        String message = JsonSerializer.serialize(batch);
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(followerQueueURL)
                .withMessageBody(message);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }
}
