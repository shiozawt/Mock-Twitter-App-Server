package service;

import dao.FollowDAO;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.FollowerRequest;
import service.response.FollowerResponse;

import java.util.Arrays;

public class FollowerServiceImplTest {
    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowerServiceImpl followerServiceImplSpy;

    @Test
    public void test() {
        User currentUser = new User("FirstName", "LastName", null);
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowerRequest(currentUser.getAlias(), 3, null,"token");

        // Setup a mock FeedDAO that will return known responses
        expectedResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        //Mockito.when(mockFollowDAO.getFollowers(request)).thenReturn(expectedResponse);

        followerServiceImplSpy = Mockito.spy(FollowerServiceImpl.class);
        Mockito.when(followerServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);

        Assertions.assertNotNull(mockFollowDAO.getFollowers(request));
    }

}
