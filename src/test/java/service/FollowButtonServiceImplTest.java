package service;

import dao.FollowDAO;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.FollowButtonRequest;
import service.response.FollowButtonResponse;

public class FollowButtonServiceImplTest {
    private FollowButtonRequest request;
    private FollowButtonResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowButtonServiceImpl followButtonServiceImplSpy;

    @Test
    public void test() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowButtonRequest(user1, user2,"token");

        // Setup a mock FeedDAO that will return known responses
        expectedResponse = new FollowButtonResponse(0,0);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        //Mockito.when(mockFollowDAO.follow(request)).thenReturn(expectedResponse);

        followButtonServiceImplSpy = Mockito.spy(FollowButtonServiceImpl.class);
        Mockito.when(followButtonServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);

        Assertions.assertNotNull(followButtonServiceImplSpy.getFollowDAO().follow(user1.getAlias(), user2.getAlias()));
    }

}
