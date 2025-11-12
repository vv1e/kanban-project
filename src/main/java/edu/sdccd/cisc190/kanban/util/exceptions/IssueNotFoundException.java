package edu.sdccd.cisc190.kanban.util.exceptions;

public class IssueNotFoundException extends Exception {
    public IssueNotFoundException(int id) {
        super(String.format("Issue ID #%06d does not exist.", id+1));
    }
}
