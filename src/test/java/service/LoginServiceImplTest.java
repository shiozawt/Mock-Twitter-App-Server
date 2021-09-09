package service;

import dao.AuthDAO;
import domain.AuthToken;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.LoginRequest;
import service.response.LoginResponse;

public class LoginServiceImplTest {
    private LoginRequest request;
    private LoginResponse expectedResponse;
    private AuthDAO mockAuthDAO;
    private LoginServiceImpl loginServiceImplSpy;

    public LoginServiceImplTest() {
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
        authToken.setToken("token");

        // Setup a request object to use in the tests
        request = new LoginRequest("username","password");

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new LoginResponse(currentUser, authToken);
        mockAuthDAO = Mockito.mock(AuthDAO.class);
        //Mockito.when(mockAuthDAO.login("username", "password")).thenReturn(expectedResponse);

        loginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(loginServiceImplSpy.getAuthDAO()).thenReturn(mockAuthDAO);

        Assertions.assertNull(mockAuthDAO.login());
    }

}
