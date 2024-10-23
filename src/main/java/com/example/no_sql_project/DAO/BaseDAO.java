package com.example.no_sql_project.DAO;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public BaseDAO() {
        // Connect to MongoDB
        mongoClient = MongoClients.create("mongodb+srv://user:test123@test.wqbk2.mongodb.net/");
        database = mongoClient.getDatabase("NoSQL_Project");
    }

    protected MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    protected void insertDocument(String collectionName, Document doc) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.insertOne(doc);
    }

    protected List<Document> findDocuments(String collectionName, Document filter) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(filter).into(new ArrayList<>());
    }

    // Example method to execute a query with parameters
    protected List<Document> queryDocuments(String collectionName, Document query) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(query).into(new ArrayList<>());
    }

    // Close the MongoDB connection
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}