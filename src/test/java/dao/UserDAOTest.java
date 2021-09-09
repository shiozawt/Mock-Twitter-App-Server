package dao;

import domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDAOTest {

    @Test
    public void userDaoTest(){
        UserDAO userDAO = new UserDAO();

        User user_true = userDAO.getUser("@AA");
        User user_false = userDAO.getUser("@not_a_user");

        System.out.println(user_true.getFirstName());

        Assertions.assertTrue(user_true.getFirstName().equals("A"));
        Assertions.assertEquals(user_false, null);

        userDAO.putUser("user_for_testing", "first","last", "https://tweeter-cs340.s3-us-west-2.amazonaws.com/images/AA.png");

        User testing_user = userDAO.getUser("@user_for_testing");

        Assertions.assertNotNull(testing_user);
    }

}