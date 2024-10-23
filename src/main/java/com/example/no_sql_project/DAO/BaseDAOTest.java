package com.example.no_sql_project.DAO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.bson.Document;

public class BaseDAOTest {
    private BaseDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new BaseDAO() {}; // Assuming BaseDao is abstract, you might need to implement any abstract methods or create a concrete subclass for testing.
    }

    @AfterEach
    public void tearDown() {
        dao.close(); // Clean up database connections
    }

    @Test
    public void testInsertAndFindDocument() {
        String collectionName = "testCollection";
        Document newDoc = new Document("name", "John Doe").append("age", 30);

        // Insert document
        dao.insertDocument(collectionName, newDoc);

        // Find document
        Document query = new Document("name", "John Doe");
        List<Document> results = dao.queryDocuments(collectionName, query);

        // Assertions to check if the document was inserted and can be retrieved
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("John Doe", results.get(0).getString("name"));
    }
}