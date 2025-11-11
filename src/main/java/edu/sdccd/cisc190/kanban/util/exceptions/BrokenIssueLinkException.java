package edu.sdccd.cisc190.kanban.util.exceptions;

public class BrokenIssueLinkException extends RuntimeException {
    public BrokenIssueLinkException(int id) {
        super(String.format("Issue ID %06d has living pointers but dead reference.", id+1));
    }
}
