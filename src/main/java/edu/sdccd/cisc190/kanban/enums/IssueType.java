package edu.sdccd.cisc190.kanban.enums;

public enum IssueType {
    BUG_REPORT("B", "bug-tag"),
    FEATURE_REQUEST("T", "feature-tag"),
    TASK("T", "task-tag");

    private final String letter;
    private final String tagStyle;

    IssueType(final String letter, final String tagStyle) {
        this.letter = letter;
        this.tagStyle = tagStyle;
    }

    public String getLetter() {
        return this.letter;
    }

    public String getTagStyle() {
        return this.tagStyle;
    }
}
