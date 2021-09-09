package dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordDAOTest {
    //TODO: add integration test!
    //TODO: functions to test: login, register, hashPassword

    @Test
    public void passwordIntegrationTest(){
        PasswordDAO passwordDAO = new PasswordDAO();
        boolean test_case_true = passwordDAO.login("AA", "password");

        boolean test_case_false = passwordDAO.login("not_a_user", "password");

        //Assertions.assertNotEquals("password", passwordHash);

        Assertions.assertFalse(test_case_false);

        Assertions.assertTrue(test_case_true);


    }

}
