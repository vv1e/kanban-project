package edu.sdccd.cisc190.kanban;

import edu.sdccd.cisc190.kanban.ui.KanbanApplication;
import edu.sdccd.cisc190.kanban.util.Board;
import edu.sdccd.cisc190.kanban.util.IssueType;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Board board = new Board("test");
        board.createIssue("Test", "this is a description", IssueType.BUG_REPORT, "me");

        Application.launch(KanbanApplication.class, args);
    }
}
