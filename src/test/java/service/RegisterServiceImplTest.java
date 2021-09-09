package service;

import dao.AuthDAO;
import dao.PasswordDAO;
import domain.AuthToken;
import domain.User;
import net.TweeterRemoteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.response.LoginResponse;

import java.io.IOException;

public class RegisterServiceImplTest {
    private RegisterRequest request;
    private LoginResponse expectedResponse;
    private PasswordDAO passwordDAO;
    private RegisterServiceImpl registerServiceImplSpy;

    public RegisterServiceImplTest() {
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
        request = new RegisterRequest("username","password","username","password","pic");

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new LoginResponse(currentUser, authToken);
        passwordDAO = Mockito.mock(PasswordDAO.class);
        //Mockito.when(mockAuthDAO.register("username", "password","username","password","pic")).thenReturn(expectedResponse);

        registerServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        //Mockito.when(registerServiceImplSpy.getAuthDAO()).thenReturn(passwordDAO);

        Assertions.assertNotNull(passwordDAO.register("username", "password"));

    }
}
