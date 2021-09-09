package service;

import dao.FeedDAO;
import domain.Status;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.FeedRequest;
import service.response.FeedResponse;

import java.util.Arrays;
import java.util.List;

public class FeedServiceImplTest {

    private FeedRequest request;
    private FeedResponse expectedResponse;
    private FeedDAO mockFeedDAO;
    private FeedServiceImpl feedServiceImplSpy;

    @Test
    public void test() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status(resultUser1,"Jan 1","6pm","TEST MESSAGE1", null);
        Status status2 = new Status(resultUser2,"Jan 2","7pm","TEST MESSAGE2",null);
        Status status3 = new Status(resultUser3,"Jan 3","8pm","TEST MESSAGE3",null);

        // Setup a request object to use in the tests
        request = new FeedRequest(currentUser.getAlias(), 3, null, "token");

        // Setup a mock FeedDAO that will return known responses
        expectedResponse = new FeedResponse(Arrays.asList(status1,status2,status3), false);
        mockFeedDAO = Mockito.mock(FeedDAO.class);
        mockFeedDAO.getFeed(request);

        List<String> list = mockFeedDAO.getContentList();

        feedServiceImplSpy = Mockito.spy(FeedServiceImpl.class);

        Assertions.assertEquals(0, list.size());
    }

}