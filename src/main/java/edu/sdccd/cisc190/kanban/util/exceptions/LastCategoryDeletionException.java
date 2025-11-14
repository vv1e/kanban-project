package edu.sdccd.cisc190.kanban.util.exceptions;

public class LastCategoryDeletionException extends Exception {
    public LastCategoryDeletionException(String boardName) {
        super(String.format("Cannot delete the last category of board \"%s\".", boardName));
    }
}
