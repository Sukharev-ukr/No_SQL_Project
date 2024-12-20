package com.example.no_sql_project.DAO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;

public abstract class BaseDAO {


    Connection mongoDbConnection;

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    protected BaseDAO() {
        mongoDbConnection = Connection.getConncetion();
        mongoClient = mongoDbConnection.getMongoClient();

    }

    protected void insertOne(Document query)
    {
        try {
            //execute queryString targetCollection, Document query
            collection.insertOne((query));
            //close connection to the database
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void insertMany(ArrayList<Document> query){
        try {
            //execute queryString targetCollection, Document query
            collection.insertMany(query);
            //close connection to the database
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected Document findOneQuery(Document query){
        try {
            //execute query
            return collection.find((query)).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected FindIterable<Document> findMultiple(Document query)
    {
        try {
            return collection.find(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected FindIterable<Document> getAll(){
        return collection.find();
    }

    protected void updateOneEntry(ObjectId id, Document update)
    {
        try {
            collection.updateOne(eq("_id",id),update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteOne(ObjectId id)
    {
        try {
            //execute query
            collection.deleteOne(eq("_id",id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteMany(Document query){
        try {
            collection.deleteMany(query);
        }catch (Exception e)
        { e.printStackTrace();}
    }

    protected Document executeAggregation()
    {
//        TODO: look in to list<BJSON> for aggrigation pipline queries
        return null;
    };

    protected void closeConnection(){
        mongoClient.close();
    }
}
