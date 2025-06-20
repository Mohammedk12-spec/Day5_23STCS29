import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

public class EmployeeService {
    private final MongoCollection<Document> collection;

    public EmployeeService(MongoDatabase db) {
        this.collection = db.getCollection("employees");
    }

    public boolean addEmployee(Employee emp) {
        if (collection.find(Filters.eq("email", emp.getEmail())).first() != null) {
            System.out.println("Email already exists.");
            return false;
        }

        Document doc = new Document("name", emp.getName())
                .append("email", emp.getEmail())
                .append("department", emp.getDepartment())
                .append("skills", emp.getSkills())
                .append("joiningDate", emp.getJoiningDate());
        collection.insertOne(doc);
        return true;
    }

    public void updateEmployee(String email, Map<String, Object> updates) {
        List<Bson> bsonUpdates = new ArrayList<>();
        updates.forEach((k, v) -> bsonUpdates.add(Updates.set(k, v)));
        collection.updateOne(Filters.eq("email", email), Updates.combine(bsonUpdates));
    }

    public void deleteEmployeeByEmail(String email) {
        collection.deleteOne(Filters.eq("email", email));
    }

    public void deleteEmployeeById(String id) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    public List<Document> searchByName(String namePart) {
        return collection.find(Filters.regex("name", namePart, "i")).into(new ArrayList<>());
    }

    public List<Document> searchByDepartment(String department) {
        return collection.find(Filters.eq("department", department)).into(new ArrayList<>());
    }

    public List<Document> searchBySkill(String skill) {
        return collection.find(Filters.in("skills", skill)).into(new ArrayList<>());
    }

    public List<Document> searchByJoiningDateRange(Date start, Date end) {
        return collection.find(Filters.and(
                Filters.gte("joiningDate", start),
                Filters.lte("joiningDate", end)
        )).into(new ArrayList<>());
    }

    public List<Document> listPaginated(int page, int pageSize, String sortBy, boolean ascending) {
        Bson sort = ascending ? Sorts.ascending(sortBy) : Sorts.descending(sortBy);
        return collection.find()
                .sort(sort)
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .into(new ArrayList<>());
    }

    public List<Document> departmentStats() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$department", Accumulators.sum("count", 1))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}
