package service.proxyInterface;

import net.TweeterRemoteException;
import service.request.StoryRequest;
import service.response.StoryResponse;

import java.io.IOException;

public interface IStoryService {
    StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException;
}
