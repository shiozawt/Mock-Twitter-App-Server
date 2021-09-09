package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import domain.AuthToken;
import domain.User;
import service.response.LoginResponse;
import service.response.LogoutResponse;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthDAO extends DAO{
    private final String timeAttr = "ts";
    private final String tokenAttr = "token";
    private Table tokenTable = null;
    private final int timelimit = 60;

    public AuthDAO() {
        if (tokenTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.tokenTable = getTable(client, "authTokens");
        }
    }

    public AuthToken login(){
        String token = generateToken();
        long time = System.currentTimeMillis();
        AuthToken result;
        try {
            PutItemOutcome outcome = this.tokenTable.putItem(new Item()
                    .withPrimaryKey(tokenAttr, token)
                    .withNumber(timeAttr, time));
            result = new AuthToken(token);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            result = null;
        }
        return result;
    }

    public boolean validate(AuthToken token){
        String tokenString = token.getToken();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(tokenAttr, tokenString);
        try {
            Item outcome = this.tokenTable.getItem(spec);
            if (outcome != null){
                long time = outcome.getLong(timeAttr);
                if (System.currentTimeMillis()-time < timelimit*60000){
                    updateTime(tokenString);
                    return true;
                }
                deleteToken(tokenString);
                return false;
            }
            return false;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Boolean logout(AuthToken token){
        return deleteToken(token.getToken());
    }

    private String generateToken(){
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    private void updateTime(String token){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(tokenAttr, token)
                .withUpdateExpression("set " + timeAttr + " = :t")
                .withValueMap(new ValueMap().withLong(":t", System.currentTimeMillis()));
        try {
            UpdateItemOutcome outcome = this.tokenTable.updateItem(updateItemSpec);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private boolean deleteToken(String token){
        try {
            this.tokenTable.deleteItem(tokenAttr, token);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
