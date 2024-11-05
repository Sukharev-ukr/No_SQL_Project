package com.example.no_sql_project.Model;

public enum Priority {
    low,
    normal,
    high;

    public static int comparePriority(Priority p1, Priority p2) {
        return Integer.compare(p1.ordinal(), p2.ordinal());
    }
}
