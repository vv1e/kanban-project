package edu.sdccd.cisc190.kanban.filter;

import edu.sdccd.cisc190.kanban.models.Category;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilteringTask extends Task<Void> {
    private final Logger LOGGER = LoggerFactory.getLogger(FilteringTask.class);

    private final ObservableList<Category> categoryList;
    private final String filterText;

    public FilteringTask(ObservableList<Category> categoryList, String filterText) {
        this.categoryList = categoryList;
        this.filterText = filterText.trim().toLowerCase();
    }

    @Override
    protected Void call() {
        int matches = 0;
        rootLoop: for (Category category : categoryList) {
            for (Issue issue : category.getIssues()) {
                if (isCancelled()) {
                    break rootLoop;
                }

                boolean filteredOut = isFilteredOut(issue);
                Platform.runLater(() -> issue.filteredOutProperty().set(filteredOut));

                if (!filteredOut) matches++;
            }
        }

        LOGGER.debug("Found {} matches for prompt: \"{}\".",  matches, filterText);

        return null;
    }

    private boolean isFilteredOut(Issue issue) {
        boolean filteredOut = false;
        String[] filterTextWords = filterText.split(" ");
        String issueName = issue.getName().toLowerCase();

        if (!issueName.contains(filterText)) {
            for (String word : filterTextWords) {
                if (!issueName.contains(word)) {
                    filteredOut = true;
                    break;
                }
            }
        }

        return filteredOut;
    }
}