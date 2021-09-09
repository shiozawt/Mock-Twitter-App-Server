package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import domain.AuthToken;
import domain.User;
import service.response.LoginResponse;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordDAO extends DAO{
    private final String usernameAttr = "user";
    private final String passwordAttr = "password";
    private Table passwordTable = null;

    public PasswordDAO() {
        setUpTable();
    }

    public void setUpTable() {
        if (passwordTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.passwordTable = getTable(client, "passwords");
        }
    }

    public Boolean login(String username, String password){
        String passwordHash = hashPassword(password);
        String alias = "@" + username;
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(usernameAttr, alias);
        try {
            Item outcome = this.passwordTable.getItem(spec);
            String storedHash = outcome.getString(passwordAttr);
            return storedHash.equals(passwordHash);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Boolean register(String username, String password){
        String passwordHash = hashPassword(password);
        String alias = "@" + username;
        try {
            PutItemOutcome outcome = this.passwordTable.putItem(new Item()
                    .withPrimaryKey(usernameAttr, alias)
                    .withString(passwordAttr, passwordHash));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hexString = new StringBuilder(no.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
