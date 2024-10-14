package com.example.no_sql_project.DAO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.print.Doc;

public abstract class BaseDAO {

    //the string required to connect to the MongoDB cluster
    final String CONNECTION_STRING = "mongodb+srv://user:test123@test.wqbk2.mongodb.net/";
    final String DATABASE = "NoSQL_Project";

    protected MongoClient getClient() throws MongoException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        return mongoClient;
    };

    protected void createEntry(String targetCollection, Document query)
    {
        try {
            //open conncetion
            MongoClient mongoClient = getClient();
            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = db.getCollection(targetCollection);

            //execute queryString targetCollection, Document query
            collection.insertOne((query));
            //close connection to the database
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Document findQuery(String targetCollection, Document query){
        try {
            //open conncetion
            MongoClient mongoClient = getClient();
            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = db.getCollection(targetCollection);

            //execute query
            Document document = collection.find((query)).first();
            //close connection to the database
            mongoClient.close();

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
            MongoClient mongoClient = getClient();
            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = db.getCollection(targetCollection);

            //execute query
            collection.updateOne(target,update);

            //close connection to the database
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteOneQuery(String targetCollection, Document query)
    {
        try {
            //open conncetion
            MongoClient mongoClient = getClient();
            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = db.getCollection(targetCollection);

            //execute query
            collection.deleteOne((query));
            //close connection to the database
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected Document executeAggregation()
    {
//        TODO: look in to list<BJSON> for aggrigation pipline queries
        return null;
    };

    //Select Query
    /*
    protected List<Document> executeSelectQuery(Bson filter) {
        List<Document> documents = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("employees");

        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        } catch (Exception e) {
            // Log the exception; Depending on your logging framework
            // e.g., Logger.error("Error accessing database", e);
            throw e;
        }

        return documents;
    }*/
}
