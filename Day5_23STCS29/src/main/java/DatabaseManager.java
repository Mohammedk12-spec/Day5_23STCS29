import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {
    private final MongoClient client;
    private final MongoDatabase database;

    public DatabaseManager() {
        client = MongoClients.create("mongodb://localhost:27017");
        database = client.getDatabase("employee_db");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        client.close();
        System.out.println("MongoDB connection closed.");
    }
}
