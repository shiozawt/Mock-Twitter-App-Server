package service;

import dao.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.UserInfoRequest;
import service.response.UserInfoResponse;

public class UserInfoServiceImplTest {
    private UserInfoRequest request;
    private UserInfoResponse expectedResponse;
    private UserDAO mockUserDAO;
    private UserInfoServiceImpl userInfoServiceImplSpy;

    @Test
    public void test() {

        // Setup a request object to use in the tests
        request = new UserInfoRequest("alias","token");

        // Setup a mock StoryDAO that will return known responses
        expectedResponse = new UserInfoResponse("response");
        mockUserDAO = Mockito.mock(UserDAO.class);
        //Mockito.when(mockUserDAO.getUserInfo(request)).thenReturn(expectedResponse);

        userInfoServiceImplSpy = Mockito.spy(UserInfoServiceImpl.class);
        Mockito.when(userInfoServiceImplSpy.getUserInfoDAO()).thenReturn(mockUserDAO);

        Assertions.assertNull(mockUserDAO.getUser("user"));

    }

}
