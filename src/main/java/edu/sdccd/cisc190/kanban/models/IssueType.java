package edu.sdccd.cisc190.kanban.models;

public enum IssueType {
    BUG_REPORT {
        public String getLetter() {
            return "B";
        }

        public String getTagStyle() {
            return "bug-tag";
        }
    },
    FEATURE_REQUEST {
        public String getLetter() {
            return "F";
        }

        public String getTagStyle() {
            return "feature-tag";
        }
    },
    TASK {
        public String getLetter() {
            return "T";
        }

        public String getTagStyle() {
            return "task-tag";
        }
    };

    public abstract String getLetter();
    public abstract String getTagStyle();
}
