package edu.sdccd.cisc190.kanban.models;

import edu.sdccd.cisc190.kanban.util.exceptions.BrokenIssueLinkException;
import edu.sdccd.cisc190.kanban.util.exceptions.IssueNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final String name;
    private final ArrayList<Category> categories;
    private int issueLatest;

    /*
     * Some explanations for array structure:
     *
     * These arrays store the following two things about an issue:
     *  - Which category (e.g. "Backlog", "In Progress") it's in. (issueIdToCategoryId)
     *  - Where exactly in that category (e.g. 1st, 2nd) it's in. (issueIdToPlacementInCategory)
     *
     * Think of it as coordinates. If Issue ID #000009 (index 8) is 2nd (index 1) in category #5 (index 4),
     *  then the program will think of it as:
     *    8 -> (4, 1)
     *
     * Nonexistent issues are assigned to (-1, -1). Retrieving something at this coordinate will throw a
     *  **checked** IssueNotFoundException that you *must* handle.
     * If a nonexistent issue is assigned to, for example, (2, 3), then something has gone terribly wrong
     *  during cleanup or storage. The code will throw an **unchecked** BrokenIssueLinkException. This
     *  typically indicates a data integrity violation and only a wizard (gender-neutral) could ever save
     *  this mess.
     */
    private int[] issueIdToCategoryId;
    private int[] issueIdToPlacementInCategory;

    public Board(String name) {
        this.name = name;
        categories = new ArrayList<>();
        issueLatest = 0;

        issueIdToCategoryId = new int[10];
        Arrays.fill(issueIdToCategoryId, -1);

        issueIdToPlacementInCategory = new int[10];

        String[] defaultCategories = new String[]{
            "Backlog",
            "In Progress",
            "Bug Testing",
            "Completed",
            "Won't Do"
        };

        for (String categoryName : defaultCategories) {
            categories.add(new Category(categoryName));
        }
    }

    public void createIssue(String name, String description, IssueType type, String creator) {
        categories.getFirst().addIssue(new Issue(name, description, creator, type, issueLatest));
        storeIssueCategoryBindings(issueLatest++, 0);
    }

    public Issue getIssue(int id) throws IssueNotFoundException {
        if (issueIdToCategoryId[id] == -1) {
            throw new IssueNotFoundException(id);
        }

        Issue issue = categories.get(issueIdToCategoryId[id]).getIssue(issueIdToPlacementInCategory[id]);
        if (issue == null) {
            throw new BrokenIssueLinkException(id);
        }

        return issue;
    }

    public ArrayList<Category> getIssueCategories() {
        return categories;
    }
    public String getName() {
        return name;
    }

    /**
     * Set/Update an Issue index coordinates for later lookup.
     * @param issueId The Issue ID
     * @param categoryId Category it was moved to.
     */
    private void storeIssueCategoryBindings(int issueId, int categoryId) {
        issueIdToCategoryId = checkAndExpandArray(issueIdToCategoryId, issueId);
        issueIdToPlacementInCategory = checkAndExpandArray(issueIdToPlacementInCategory, issueId);

        issueIdToCategoryId[issueId] = categoryId;
        issueIdToPlacementInCategory[issueId] = categories.getFirst().size()-1;
    }

    private int[] checkAndExpandArray(int[] array, int index) {
        if (index >= array.length) {
            int[] tempArray = Arrays.copyOf(array, array.length * 2);
            Arrays.fill(tempArray, array.length, tempArray.length-1, -1);
            array = tempArray;
        }

        return array;
    }
}
