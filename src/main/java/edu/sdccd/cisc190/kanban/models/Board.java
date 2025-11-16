package edu.sdccd.cisc190.kanban.models;

import edu.sdccd.cisc190.kanban.Launcher;
import edu.sdccd.cisc190.kanban.enums.IssueType;
import edu.sdccd.cisc190.kanban.util.exceptions.BrokenIssueLinkException;
import edu.sdccd.cisc190.kanban.util.exceptions.IssueNotFoundException;
import edu.sdccd.cisc190.kanban.util.exceptions.LastCategoryDeletionException;

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
        Arrays.fill(issueIdToPlacementInCategory, -1); // Easier for debugging

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

        // These issues only appear if the Kanban Board Program is run through a debugger.
        if (Launcher.DEBUG_MODE_ENABLED) {
            createIssue("Bug", "This is a bug", IssueType.BUG_REPORT, "Debug");
            createIssue("Feature", "This is a feature", IssueType.FEATURE_REQUEST, "Debug2");
            createIssue("Task", "This is a task", IssueType.TASK, "Debug3");
        }
    }

    public void createIssue(String name, String description, IssueType type, String creator) {
        categories.getFirst().addIssue(new Issue(name, description, creator, type, issueLatest));
        updateIssueCoordinateMapping(issueLatest++, 0);
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

    public int getCategoryOfIssue(int id) throws IssueNotFoundException {
        if (issueIdToCategoryId[id] == -1) {
            throw new IssueNotFoundException(id);
        } else {
            return issueIdToCategoryId[id];
        }
    }

    public void moveIssue(int issueId, int categoryId) throws IssueNotFoundException {
        if (issueIdToCategoryId[issueId] == -1) {
            throw new IssueNotFoundException(issueId);
        }

        final Issue issue = getIssue(issueId);
        final int issueCategoryIndex = issueIdToPlacementInCategory[issueId];

        final int originalCategoryId = getCategoryOfIssue(issueId);
        final Category originalCategory = categories.get(originalCategoryId);

        categories.get(categoryId).addIssue(issue);
        originalCategory.removeIssue(issueCategoryIndex);

        for (Issue categoryIssue : originalCategory.getIssues()) {
            if (issueIdToPlacementInCategory[categoryIssue.getId()] > issueCategoryIndex) {
                issueIdToPlacementInCategory[categoryIssue.getId()] -= 1;
            }
        }

        updateIssueCoordinateMapping(issueId, categoryId);

        issue.addComment(
            "System",
            String.format("Issue moved to \"%s.\"", getCategoriesNames()[categoryId])
        );
    }

    public Category createCategory(String name) {
        Category category = new Category(name);
        categories.add(category);

        return category;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public String[] getCategoriesNames() {
        return categories.stream().map(Category::getName).toArray(String[]::new);
    }

    private Category popCategory(int categoryId) {
        final Category category = categories.get(categoryId);
        categories.remove(categoryId);

        for (int i = 0; i < issueIdToCategoryId.length; i++) {
            if (issueIdToCategoryId[i] > categoryId) {
                issueIdToCategoryId[i] -= 1;
            }
        }

        for (Issue issue : category.getIssues()) {
            issueIdToCategoryId[issue.getId()] = -1;
        }

        return category;
    }

    public void moveCategory(int oldCategoryId, int newCategoryId) {
        final Category category = popCategory(oldCategoryId);

        for (int i = 0; i < issueIdToCategoryId.length; i++) {
            if (issueIdToCategoryId[i] >= newCategoryId) {
                issueIdToCategoryId[i] += 1;
            }
        }

        categories.add(newCategoryId, category);

        for (Issue issue : category.getIssues()) {
            issueIdToCategoryId[issue.getId()] = newCategoryId;
        }
    }

    public void removeCategory(int categoryId) throws LastCategoryDeletionException {
        if (categories.size() == 1) {
            throw new LastCategoryDeletionException(this.name);
        }

        final Category category = popCategory(categoryId);
        final int newCategoryId = categoryId == 0? categoryId : categoryId - 1;
        final Category newCategory = categories.get(newCategoryId);

        for (Issue issue : category.getIssues()) {
            newCategory.addIssue(issue);
            updateIssueCoordinateMapping(issue.getId(), newCategoryId);

            issue.addComment(
                "System",
                String.format("Issue moved to \"%s\" due to the removal of \"%s.\"", newCategory.getName(), category.getName())
            );
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Set/Update an Issue index coordinates for later lookup.
     * @param issueId The Issue ID
     * @param categoryId Category it was moved to.
     */
    private void updateIssueCoordinateMapping(int issueId, int categoryId) {
        issueIdToCategoryId = checkAndExpandArray(issueIdToCategoryId, issueId);
        issueIdToPlacementInCategory = checkAndExpandArray(issueIdToPlacementInCategory, issueId);

        issueIdToCategoryId[issueId] = categoryId;
        issueIdToPlacementInCategory[issueId] = categories.get(categoryId).size()-1;
    }

    private int[] checkAndExpandArray(int[] array, int index) {
        if (index >= array.length) {
            int[] tempArray = Arrays.copyOf(array, array.length * 2);
            Arrays.fill(tempArray, array.length, tempArray.length, -1);
            array = tempArray;
        }

        return array;
    }
}
