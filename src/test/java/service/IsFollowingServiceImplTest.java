package service;

import dao.FollowDAO;
import domain.User;
import net.TweeterRemoteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.IsFollowingRequest;
import service.response.IsFollowingResponse;

import java.io.IOException;

public class IsFollowingServiceImplTest {
    private IsFollowingRequest request;
    private IsFollowingResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private IsFollowingServiceImpl isFollowingServiceImplSpy;

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
        request = new IsFollowingRequest(resultUser1, resultUser2,"token");

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new IsFollowingResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        //Mockito.when(mockFollowDAO.isFollowing(request)).thenReturn(expectedResponse);

        isFollowingServiceImplSpy = Mockito.spy(IsFollowingServiceImpl.class);
        Mockito.when(isFollowingServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);

        Assertions.assertNotNull(mockFollowDAO.isFollowing(resultUser1.getAlias(), resultUser2.getAlias()));
    }
}
