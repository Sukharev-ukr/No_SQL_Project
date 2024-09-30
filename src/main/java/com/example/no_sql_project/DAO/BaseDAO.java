package com.example.no_sql_project.DAO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class BaseDAO
{
    public static void testFunction() {

        try {

            // Connection URI
            MongoClientURI uri = new MongoClientURI("mongodb+srv://user:test123@test.wqbk2.mongodb.net/");

            // Creating a MongoDB client
            MongoClient mongoClient = new MongoClient(uri);

            // Connecting to the database
            MongoDatabase database = mongoClient.getDatabase("NoSQL_Project");

            // Print the name of the database to confirm connection
            System.out.println("Connected to the database: " + database.getName());

            // Close the MongoDB client
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
