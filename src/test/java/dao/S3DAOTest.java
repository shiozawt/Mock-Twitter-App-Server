package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class S3DAOTest {

    @Test
    public void S3DAOTest(){
        S3DAO s3 = new S3DAO();

        String testString = "server_test_user";
        String image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwucXQ7F0Fz1o6pm-76S0c2VdMJYkeYYEi-w&usqp=CAU";

        String returnURL = s3.upload(testString, image);

        Assertions.assertEquals("https://tweeter-cs340.s3-us-west-2.amazonaws.com/images/server_test_user.png", returnURL);
        Assertions.assertNotNull(returnURL);
    }

}
