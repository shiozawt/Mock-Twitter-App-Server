package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import domain.User;

public class UserDAO extends DAO {
    //TODO: THIS IS A PLACHOLDER - need to fix in the future - hardcoded image FOR NOW
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private String tableName = "users";
    private String firstnameAttr = "firstname";
    private String lastnameAttr = "lastname";
    private String followingCountAttr = "followingCount";
    private String followerCountAttr = "followerCount";
    private String aliasHandle = "alias_handle";
    private String imageURLAttr = "image";
    private Table userTable = null;

    public UserDAO() {
        setUpTable();
    }

    public void setUpTable() {
        if (userTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.userTable = getTable(client, this.tableName);
        }
    }

    // this returns a user with: firstname, lastname, alias only .... SO FAR
    public User getUser(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(aliasHandle, alias);
        User user = null;
        try {
            System.out.println("Attempting to read the item...");
            Item outcome = this.userTable.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);

            String firstname = outcome.get(firstnameAttr).toString();
            String lastname = outcome.get(lastnameAttr).toString();
            String image = outcome.getString(imageURLAttr);
            String username = outcome.getString(aliasHandle);
            if (image == null){
                image = MALE_IMAGE_URL;
            }
            int followingCount = outcome.getInt(followingCountAttr);
            int followerCount = outcome.getInt(followerCountAttr);
            user = new User(firstname, lastname, username, image);
            user.setFollowingCount(followingCount);
            user.setFollowerCount(followerCount);
        }
        catch (Exception e) {
            System.err.println("Unable to read item");
            System.err.println(e.getMessage());
        }
        return user;
    }

    public void updateUserFollowingCount(String follower, String followingCount) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(aliasHandle, follower)
                .withUpdateExpression("set followingCount = :fec")
                .withValueMap(new ValueMap().withString(":fec", followingCount))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = this.userTable.updateItem(updateItemSpec);
//            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item");
            System.err.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void updateUserFollowerCount(String followee, String followerCount) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(aliasHandle, followee)
                .withUpdateExpression("set followerCount = :frc")
                .withValueMap(new ValueMap().withString(":frc", followerCount))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = this.userTable.updateItem(updateItemSpec);
//            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item");
            System.err.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public User putUser(String username, String firstName, String lastName, String image){
        User user = new User(firstName, lastName, "@" + username, image);
        user.setFollowerCount(0);
        user.setFollowingCount(0);
        try {
            PutItemOutcome outcome = this.userTable.putItem(new Item()
                    .withPrimaryKey(aliasHandle, user.getAlias())
                    .withString(firstnameAttr, user.getFirstName())
                    .withString(lastnameAttr, user.getLastName())
                    .withString(imageURLAttr, user.getImageUrl())
                    .withInt(followerCountAttr, user.getFollowerCount())
                    .withInt(followingCountAttr, user.getFollowingCount()));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return user;
    }

}
