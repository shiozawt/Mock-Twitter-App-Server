package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import domain.FollowerQueueBatch;
import net.JsonSerializer;
import service.FeedServiceImpl;

public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            FollowerQueueBatch batch = JsonSerializer.deserialize(msg.getBody(), FollowerQueueBatch.class);

            // Use FeedServiceImpl to update the feed table
            FeedServiceImpl service = new FeedServiceImpl();
            service.updateFeed(batch);
        }
        return null;
    }
}
