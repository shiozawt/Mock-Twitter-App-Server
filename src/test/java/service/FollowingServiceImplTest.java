package service;

import dao.FollowDAO;
import domain.User;
import net.TweeterRemoteException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import service.request.FollowingRequest;
import service.response.FollowingResponse;

import java.util.Arrays;

public class FollowingServiceImplTest {

    private FollowingRequest request;
    private FollowingResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowingServiceImpl followingServiceImplSpy;

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
        request = new FollowingRequest(currentUser.getAlias(), 3, null, "token");

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        //Mockito.when(mockFollowDAO.getFollowees(request)).thenReturn(expectedResponse);

        followingServiceImplSpy = Mockito.spy(FollowingServiceImpl.class);
        Mockito.when(followingServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);

        Assertions.assertNotNull(mockFollowDAO.getFollowees(request));

    }
}