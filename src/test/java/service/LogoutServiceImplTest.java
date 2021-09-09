package service;

import dao.AuthDAO;
import domain.AuthToken;
import domain.User;
import net.TweeterRemoteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.response.LoginResponse;
import service.response.LogoutResponse;

import java.io.IOException;

public class LogoutServiceImplTest {
    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private AuthDAO mockAuthDAO;
    private LogoutServiceImpl logoutServiceImplSpy;

    public LogoutServiceImplTest() {
    }

    @Test
    public void test() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        AuthToken authToken = new AuthToken("token");
        authToken.setToken("1623134f");

        // Setup a request object to use in the tests
        request = new LogoutRequest();

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new LogoutResponse(true, "logout");
        mockAuthDAO = Mockito.mock(AuthDAO.class);
        //Mockito.when(mockAuthDAO.logout(authToken)).thenReturn(expectedResponse);

        logoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.getAuthDAO()).thenReturn(mockAuthDAO);

        Assertions.assertNotNull(mockAuthDAO.logout(authToken));
    }

}
