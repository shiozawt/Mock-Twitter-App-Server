package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import service.request.PostStatusRequest;
import service.request.StoryRequest;

import java.text.SimpleDateFormat;
import java.util.*;

public class StoryDAO extends DAO{
    private String tableName = "stories";
    private Table storyTable = null;
    private String posterAliasAttr = "poster_alias";
    private String timestampAttr = "ts";
    private String userContentAttr = "user_content";
    private List<String> contentList = new ArrayList<String>();
    private List<String> timestampList = new ArrayList<String>();

    public StoryDAO() {
        setUpTable();
    }

    public void setUpTable() {
        if (storyTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.storyTable = getTable(client, this.tableName);
        }
    }

    public List<String> getContentList() {
        return this.contentList;
    }

    public List<String> getTimestamp() {
        return this.timestampList;
    }

    public void getStory(StoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#pa", posterAliasAttr);

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":val", request.getUserAlias());

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            QuerySpec querySpec;

            if (request.getLastStatus() == null) {
                querySpec = new QuerySpec().withKeyConditionExpression("#pa = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(false)
                        .withMaxResultSize(10);
            }
            else {
                /**
                 * WARNING: for some reasons,
                 *  this line of code request.getLastStatus().getDate() + " " + request.getLastStatus().getTime();
                 *  will include comma in the result, results in format
                 *  DATE, TIME
                 */
                String timeInCalendar = request.getLastStatus().getDate() + " " + request.getLastStatus().getTime();
                String timeInMilli = convertDateToMilli(timeInCalendar);
                querySpec = new QuerySpec().withKeyConditionExpression("#pa = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(false)
                        .withMaxResultSize(10)
                        .withExclusiveStartKey(posterAliasAttr, request.getUserAlias(), timestampAttr, timeInMilli);
            }

            // got the result from DB, building the list of followee alias
            items = this.storyTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                contentList.add(item.getString(userContentAttr));
                timestampList.add(item.getString(timestampAttr));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
        }
    }

    public String addStory(PostStatusRequest request) {
        try {
            /**
             * WARNING: Please provide
             * DATE, TIME format
             * to the date-time converter
             */
            String timestamp = request.getDate() + ", " + request.getTime();
            this.storyTable.putItem(new Item().withPrimaryKey(posterAliasAttr, request.getPosterAlias(), timestampAttr, convertDateToMilli(timestamp))
                    .withString(userContentAttr, request.getContent()));
        }
        catch (Exception e) {
            System.err.println("Unable to add item");
            System.err.println(e.getMessage());
            return "Unable to add item" + e.getMessage();
        }
        
        return "";
    }

    private String convertDateToMilli(String timeInCalendar) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM dd yyyy, h:mm aa").parse(timeInCalendar);

        } catch(Exception ex) {
            System.out.println("date conversion error");
            return "0";
        }

        return String.valueOf(date.getTime());
    }

}
