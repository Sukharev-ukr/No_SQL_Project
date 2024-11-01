package com.example.no_sql_project.DAO;


import com.example.no_sql_project.Model.Ticket;
import org.bson.Document;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ArchiveDoa extends BaseDAO{
/*"Archiving the entire database (For example all tickets older than 2 years): With a simple click
on a buton, several entries before a certain date, are moved to a secondary (archive)
database."*/

    final String ARCHIVE = "ArchiveDatabase";
    final String DATABASE = "NoSQL_Project";
    private final String COLLECTION_NAME = "ArchiveTickets";

    public ArchiveDoa(){

    }

    public String setCutOffDate(int days){
        LocalDateTime cutOffDate = LocalDateTime.now().minusDays(days); // Example for 2 years ago
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Use appropriate formatter if needed
        return cutOffDate.format(formatter);
    }

    public void insertManyIntoArchive(ArrayList<Document> documents){
        openArchive();
        insertMany(documents);
        closeArchive();
    }
    private void openArchive(){
        mongoDbConnection.setDatabase(ARCHIVE);
        database = mongoDbConnection.getDatabase();
        mongoDbConnection.setCollection(COLLECTION_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }
    private void closeArchive(){
        mongoDbConnection.setDatabase(DATABASE);
    }


}


