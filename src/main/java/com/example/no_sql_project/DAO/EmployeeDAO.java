package com.example.no_sql_project.DAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.util.Pair;
import org.bson.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EmployeeDAO extends BaseDAO {

    public boolean authenticate(String username, String password) {
        try {
            Pair<MongoClient, MongoCollection<Document>> connection = openConnection("Employees");
            MongoCollection<Document> employees = connection.getValue();

            Document userDoc = employees.find(new Document("Name", username)).first();
            connection.getKey().close();

            if (userDoc == null) {
                System.out.println("Username not found");
                return false;
            }

            String storedHash = userDoc.getString("Password");
            String storedSalt = userDoc.getString("Salt");

            String providedHash = hashPassword(password, storedSalt);

            return storedHash.equals(providedHash);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerEmployee(String username, String password) {
        try {

            String salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            Document newEmployee = new Document("Name", username)
                    .append("Password", hashedPassword)
                    .append("Salt", salt)
                    .append("Role", "Employee")
                    .append("Privileges", "basic");

            createEntry("Employees", newEmployee);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String passwordWithSalt = password + salt;
        byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());
        return bytesToHex(hashedBytes);
    }

    private String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return bytesToHex(salt);
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
