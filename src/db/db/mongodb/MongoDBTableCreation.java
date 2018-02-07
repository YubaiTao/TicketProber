package db.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * project: TicketProber
 *
 * @author YubaiTao on 06/02/2018.
 */
public class MongoDBTableCreation {
    // Run as Java application to create MongoDB collections with index.
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(MongoDBUtility.DB_NAME);

        mongoClient.close();
        System.out.println("Import is done successfully.");
    }

}
