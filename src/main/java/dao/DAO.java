package dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;

public class DAO {
    public AmazonDynamoDB getDBclient() {
        return AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    }

    public Table getTable(AmazonDynamoDB client, String tableName) {
        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable(tableName);
    }

}
