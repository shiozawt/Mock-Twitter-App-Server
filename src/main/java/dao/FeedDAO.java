package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import domain.FollowerQueueBatch;
import service.request.FeedRequest;

import java.text.SimpleDateFormat;
import java.util.*;

public class FeedDAO extends DAO{
    private String feedTableName = "feed";
    private Table feedTable = null;
    private String timestamp = "ts";
    private List<String> contentList = new ArrayList<String>();
    private List<String> timestampList = new ArrayList<String>();
    private List<String> posterAliasList = new ArrayList<String>();

    public FeedDAO() {
        setUpTable();
    }

    public void setUpTable() {
        if (feedTable == null) {
            AmazonDynamoDB client = getDBclient();
            this.feedTable = getTable(client, this.feedTableName);
        }
    }

    public List<String> getContentList() {
        return this.contentList;
    }

    public List<String> getTimestamp() {
        return this.timestampList;
    }

    public List<String> getPosterAliasList() { return this.posterAliasList; }

    public void getFeed(FeedRequest request) {

        assert request.getLimit() > 0;
        assert request.getAlias() != null;

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#fa", "follower_alias");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":val", request.getAlias());

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            String primaryKeyName = "follower_alias";
            String sortKeyName = timestamp;
            QuerySpec querySpec;

            if (request.getLastStatus() == null) {
                System.out.println("first query");
                querySpec = new QuerySpec().withKeyConditionExpression("#fa = :val")
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
                System.out.println("paginated query");
                querySpec = new QuerySpec().withKeyConditionExpression("#fa = :val")
                        .withNameMap(nameMap)
                        .withValueMap(valueMap)
                        .withScanIndexForward(false)
                        .withMaxResultSize(10)
                        .withExclusiveStartKey(primaryKeyName, request.getAlias(), sortKeyName, timeInMilli);
            }

            items = this.feedTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                contentList.add(item.getString("user_content"));
                timestampList.add(item.getString(timestamp));
                posterAliasList.add(item.getString("poster_alias"));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
        }
    }

    public void updateFeed(FollowerQueueBatch batch) {
        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(this.feedTableName);

        // make a feed for each follower and add each feed into the batch
        for (String follower : batch.getFollowers()) {
            Item item = new Item()
                    .withPrimaryKey("follower_alias", follower)
                    .withString(timestamp, convertDateToMilli(batch.getDate() + ", " + batch.getTime()))
                    .withString("poster_alias", batch.getPosterAlias())
                    .withString("user_content", batch.getContent());

            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(this.feedTableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }

    private String convertDateToMilli(String timeInCalendar) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM dd yyyy, h:mm aa").parse(timeInCalendar);

        } catch(Exception ex) {
            System.out.println("date convertsion error");
            return "0";
        }

        return String.valueOf(date.getTime());
    }
}
