package com.example.no_sql_project.DAO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.util.Pair;
import org.bson.Document;
public abstract class BaseDAO {

    //the string required to connect to the MongoDB cluster
    final String CONNECTION_STRING = "mongodb+srv://user:test123@test.wqbk2.mongodb.net/";
    final String DATABASE = "NoSQL_Project";

    protected MongoClient getClient() throws MongoException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return mongoClient;
    };

    protected Pair<MongoClient,MongoCollection<Document>> openConnection(String targetCollection) throws MongoException {
        MongoClient mongoClient = getClient();
        MongoDatabase db = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = db.getCollection(targetCollection);
        return new Pair<>(mongoClient, collection);
    }

    public void createEntry(String targetCollection, Document query)
    {
        try {
            //open conncetion
            Pair<MongoClient,MongoCollection<Document>> connection =  openConnection(targetCollection);

            //execute queryString targetCollection, Document query
            connection.getValue().insertOne((query));
            //close connection to the database
            connection.getKey().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Document findQuery(String targetCollection, Document query){
        try {
            //open conncetion
            Pair<MongoClient,MongoCollection<Document>> connection =  openConnection(targetCollection);

            //execute query
            Document document = connection.getValue().find((query)).first();
            //close connection to the database
            connection.getKey().close();

            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void updateOneEntry(String targetCollection, Document target, Document update)
    {
        try {
            //open conncetion
            Pair<MongoClient,MongoCollection<Document>> connection =  openConnection(targetCollection);

            //execute query
            connection.getValue().updateOne(target,update);

            //close connection to the database
            connection.getKey().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteOneQuery(String targetCollection, Document query)
    {
        try {
            //open conncetion
            Pair<MongoClient,MongoCollection<Document>> connection =  openConnection(targetCollection);

            //execute query
            connection.getValue().deleteOne((query));
            //close connection to the database
            connection.getKey().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected Document executeAggregation()
    {
//        TODO: look in to list<BJSON> for aggrigation pipline queries
        return null;
    };
}
