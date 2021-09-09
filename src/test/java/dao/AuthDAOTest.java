package dao;

import domain.AuthToken;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.request.LoginRequest;

public class AuthDAOTest {

    private AuthDAO authDAOSpy;

    @BeforeEach
    void setup() {
        authDAOSpy = Mockito.spy(new AuthDAO());
    }

    @Test
    void fullIntegrationTest(){
       AuthDAO authDAO = new AuthDAO();
       AuthToken authToken = authDAO.login();

       Assertions.assertNotNull(authToken);

       Boolean check1 = authDAO.validate(authToken);

       Assertions.assertTrue(check1);

       authDAO.logout(authToken);

       Boolean check2 = authDAO.validate(authToken);

       Assertions.assertFalse(check2);
    }

    @Test
    void testLogin_doesNotReturnWithoutUser() {
        AuthToken auth1 = authDAOSpy.login();
        AuthToken auth2 = authDAOSpy.login();

        Assertions.assertNotNull(auth1);
        Assertions.assertNotNull(auth2);
    }

    @Test
    void testLogin_noAuthToken() {
        //AuthToken request1 = new AuthToken("token");
        //Mockito.when(authDAOSpy.login("username", "password")).thenReturn(response1);

        AuthToken response1 = authDAOSpy.login();
        AuthToken response2 = authDAOSpy.login();

        Assertions.assertNotEquals(response1, response2);
        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);
    }

    @Test
    void testLogin_oneAuthTokenGiven() {
        AuthToken response1 = new AuthToken("token");
        //LoginResponse response1 = new LoginResponse(user1, auth);
        Mockito.when(authDAOSpy.login()).thenReturn(response1);

        LoginRequest request = new LoginRequest("username", "password");
        AuthToken response2 = authDAOSpy.login();

        Assertions.assertEquals(response1, response2);
        Assertions.assertTrue(response2.getToken() == "token");
        Assertions.assertTrue(response1.getToken() == "token");
    }

    @Test
    void testLogin_actualLoginAttempt_NoInfo() {

        AuthDAO authDao = new AuthDAO();

        AuthToken response1 = new AuthToken("token");
        //LoginResponse response1 = new LoginResponse(user1, auth);
        Mockito.when(authDAOSpy.login()).thenReturn(response1);

        LoginRequest request = new LoginRequest("username", "password");
        AuthToken response2 = authDAOSpy.login();

        AuthToken token = authDao.login();

        Assertions.assertNotNull(token);

        AuthToken response3 = authDao.login();

        Assertions.assertNotNull(response3);

        Assertions.assertEquals(response1, response2);

    }

    @Test
    void validateTest_givenFalseAuthToken(){
        AuthToken response1 = new AuthToken("token");
        AuthToken response2 = new AuthToken("aoijlk;jlkuo");
        AuthToken response3 = new AuthToken("testCase");

        Boolean validation1 = authDAOSpy.validate(response1);
        Boolean validation2 = authDAOSpy.validate(response2);
        Boolean validation3 = authDAOSpy.validate(response3);

        Assertions.assertFalse(validation1);
        Assertions.assertFalse(validation2);
        Assertions.assertFalse(validation3);
    }

    @Test
    void validateTest_givenTrueAuthToken(){

        PasswordDAO passDAO = new PasswordDAO();
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        LoginRequest loginRequest = new LoginRequest("AA", "password");

        if (passDAO.login(loginRequest.getUsername(), loginRequest.getPassword())) {
            AuthToken token = authDAO.login();
            User user = userDAO.getUser("@" + loginRequest.getUsername());
            Assertions.assertNotNull(token);
            System.out.print(user.getAlias());
        }
        else{
            System.out.print("this did not work");
        }


    }

}