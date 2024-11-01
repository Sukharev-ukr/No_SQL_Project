package com.example.no_sql_project.DAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Connection {

    final String CONNECTION_STRING = "mongodb+srv://user:test123@test.wqbk2.mongodb.net/";

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    private static Connection connection;

    private Connection(){
        mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));

    };

    public static Connection getConncetion(){
        if (connection == null){ connection = new Connection(); }
        return connection;
    }

    public void setCollection(String collectionName){
        collection = database.getCollection(collectionName);
    }

    public MongoDatabase getDatabase(){
        return database;
    }

    public void setDatabase(String databaseName){
        database = mongoClient.getDatabase(databaseName);
    }

    public MongoClient getMongoClient(){return mongoClient;}

}
