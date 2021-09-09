package service;

import dao.StoryDAO;
import domain.Status;
import domain.User;
import net.TweeterRemoteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.PostStatusRequest;
import service.response.PostStatusResponse;

import java.io.IOException;

public class PostStatusServiceImplTest {

    private PostStatusRequest request;
    private PostStatusResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private PostStatusServiceImpl postStatusServiceImplSpy;

    @Test
    public void test() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status(resultUser1, "Jan 1", "6pm", "TEST MESSAGE1", null);
        Status status2 = new Status(resultUser2, "Jan 2", "7pm", "TEST MESSAGE2", null);
        Status status3 = new Status(resultUser3, "Jan 3", "8pm", "TEST MESSAGE3", null);

        // Setup a request object to use in the tests
        request = new PostStatusRequest("@AA", "Jan 1", "6pm", "Content", "authToken");

        // Setup a mock PostStatusDAO that will return known responses
        expectedResponse = new PostStatusResponse("TEST MESSAGE1");
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        //Mockito.when(mockStoryDAO.postStatus(request)).thenReturn(expectedResponse);


        postStatusServiceImplSpy = Mockito.spy(PostStatusServiceImpl.class);
        Mockito.when(postStatusServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);

        Assertions.assertNull(mockStoryDAO.addStory(request));
    }

}