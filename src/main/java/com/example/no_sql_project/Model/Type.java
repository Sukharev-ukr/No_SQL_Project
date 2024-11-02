package com.example.no_sql_project.Model;

public enum Type {
    SoftwareIssue {
        @Override
        public String toString() {
            return "Software Issue";
        }
    },
    NetworkProblem {
        @Override
        public String toString() {
            return "Network Problem";
        }
    },
    HardwareMalfunction {
        @Override
        public String toString() {
            return "Hardware Malfunction";
        }
    },
    Maintenance {
        @Override
        public String toString() {
            return "Maintenance";
        }
    },
    UserSupport {
        @Override
        public String toString() {
            return "User Support";
        }
    }
}
