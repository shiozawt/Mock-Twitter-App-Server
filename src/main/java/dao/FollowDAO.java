package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import service.request.FollowerRequest;
import service.request.FollowingRequest;

import java.util.*;

public class FollowDAO extends DAO {
    private String followerHandle = "follower_handle";
    private String followeeHandle = "followee_handle";
    private String followsTableIndex = "follows_index";
    private String tableName = "follows";
    private Table followTable = null;

    public FollowDAO() {
        setUpTable();
    }

    public void setUpTable() {
        if (followTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.followTable = getTable(client, this.tableName);
        }
    }

    public List<String> getAllFollowers(String followee) {
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#feh", followeeHandle);

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":val", followee);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> followerAlias = new ArrayList<String>();

        try {
            QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#feh = :val")
                    .withNameMap(nameMap)
                    .withValueMap(valueMap);

            items = this.followTable.getIndex(followsTableIndex).query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                followerAlias.add(item.getString(followerHandle));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query getAllFollowers");
            System.err.println(e.getMessage());
        }

        return followerAlias;
    }

    public List<String> getFollowees(FollowingRequest request) {
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#frh", followerHandle);

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":val", request.getFollowerAlias());

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> followeeAlias = new ArrayList<String>();

        try {
            QuerySpec querySpec;

            if (request.getLastFolloweeAlias() == null) {
                System.out.println("getFollowees first query");
                querySpec = new QuerySpec().withKeyConditionExpression("#frh = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(true)
                        .withMaxResultSize(10);
            }
            else {
                System.out.println("getFollowees paginated query");
                querySpec = new QuerySpec().withKeyConditionExpression("#frh = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(true)
                        .withMaxResultSize(10)
                        .withExclusiveStartKey(followerHandle, request.getFollowerAlias(), followeeHandle, request.getLastFolloweeAlias());
            }

            // got the result from DB, building the list of followee alias
            items = this.followTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                followeeAlias.add(item.getString(followeeHandle));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
        }

        return followeeAlias;
    }

    public List<String> getFollowers(FollowerRequest request) {
        assert request.getLimit() > 0;
        assert request.getFolloweeAlias() != null;

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#feh", followeeHandle);

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":val", request.getFolloweeAlias());

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> followerAlias = new ArrayList<String>();

        try {
            QuerySpec querySpec;

            if (request.getLastFollowerAlias() == null) {
                System.out.println("getFollowers first query");
                querySpec = new QuerySpec().withKeyConditionExpression("#feh = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(true)
                        .withMaxResultSize(10);
            }
            else {
                System.out.println("getFollowers paginated query");
                querySpec = new QuerySpec().withKeyConditionExpression("#feh = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(true)
                        .withMaxResultSize(10)
                        .withExclusiveStartKey(followeeHandle, request.getFolloweeAlias(), followerHandle, request.getLastFollowerAlias());
            }

            items = this.followTable.getIndex(followsTableIndex).query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                followerAlias.add(item.getString(followerHandle));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
        }

        return followerAlias;
    }

    // Modify the follow table(add or remove an item)
    // return true meaning follow action, return false meaning unfollow action
    public boolean follow(String follower, String followee) {
        boolean searchResult = getFollowRelationship(follower, followee);

        // if follower IS following the followee already, that means they want to unfollow when they hit the button
        // vice versa
        if (searchResult) {
            // remove the follow relationship from the table
            deleteItem(follower, followee);
            return false;
        }
        else {
            // add the item because the user wants to follow someone
            addItem(follower, followee);
            return true;
        }
    }

    public boolean isFollowing(String follower, String followee) {
        boolean searchResult = getFollowRelationship(follower, followee);

        if (searchResult) {
            return true;
        }
        else {
            return false;
        }
    }

    private void addItem(String follower, String followee) {
        try {
            this.followTable.putItem(new Item().withPrimaryKey(followerHandle, follower, followeeHandle, followee));
        }
        catch (Exception e) {
            System.err.println("Unable to add item");
            System.err.println(e.getMessage());
        }

    }

    private void deleteItem(String follower, String followee) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(followerHandle, follower, followeeHandle, followee);

        try {
            this.followTable.deleteItem(deleteItemSpec);
        }
        catch (Exception e) {
            System.err.println("Unable to delete item");
            System.err.println(e.getMessage());
        }
    }

    private boolean getFollowRelationship(String follower, String followee) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(followerHandle, follower, followeeHandle, followee);
        boolean result = true;
        try {
            Item outcome = this.followTable.getItem(spec);
            if (outcome != null) {
                result = true; // meaning the follower IS following the followee right now
            }
            else {
                result = false; // meaning the follower IS NOT following the followee right now
            }
        }
        catch (Exception e) {
            System.err.println("Unable to read item");
            System.err.println(e.getMessage());
        }

        return result;
    }
}
