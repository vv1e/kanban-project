package edu.sdccd.cisc190.kanban.enums;

import edu.sdccd.cisc190.kanban.util.interfaces.Sortable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public enum SortingType {
    NONE(
        SortField.NONE, SortDirection.NONE,
        null
    ),
    DATE_CREATED_ASCENDING(
        SortField.DATE_CREATED, SortDirection.ASCENDING,
        Comparator.comparingLong(Sortable::getDateCreatedNano)
    ),
    DATE_CREATED_DESCENDING(
        SortField.DATE_CREATED, SortDirection.DESCENDING,
        Comparator.comparingLong(Sortable::getDateCreatedNano).reversed()
    ),
    DATE_MODIFIED_ASCENDING(
        SortField.DATE_MODIFIED, SortDirection.ASCENDING,
        Comparator.comparingLong(Sortable::getDateModifiedNano)
    ),
    DATE_MODIFIED_DESCENDING(
        SortField.DATE_MODIFIED, SortDirection.DESCENDING,
        Comparator.comparingLong(Sortable::getDateModifiedNano).reversed()
    );

    private final SortField field;
    private final SortDirection direction;
    private final Comparator<Sortable> comparator;

    private static final Map<Integer, SortingType> TYPE_LOOKUP_MAP = new HashMap<>();

    SortingType(SortField field, SortDirection direction, Comparator<Sortable> comparator) {
        this.field = field;
        this.direction = direction;
        this.comparator = comparator;
    }

    static {
        for (SortingType type : SortingType.values()) {
            /*
             * Explanation:
             * This is a way for the enum to efficiently resolve a SortingType from a SortField and a SortDirection
             * Every SortingType has an "ID" of sorts that is an integer.
             * The "ID" isn't created sequentially (0, 1, 2), but is based off its field and its direction.
             * Now, let's think of IDs in binary form (I will truncate it into a byte)
             * 0b00000000 (0b means binary, as opposed to 0x (hexadecimal))
             * The field and direction are each reserved a part of this number
             * 0bFFFFFFFFDD
             * F - field-reserved | D - direction-reserved
             * So let's suppose we have a SortingType called ALPHABETICAL_ASCENDING
             * Naturally, its field type is ALPHABETICAL (0b00000011 (3)) and its direction is ASCENDING (0b00000001 (1))
             * The "ID" will look like this:
             *      0b00001101
             *     ┌──┴─┬──┘└┴──────┬┐
             * 0b00000011   0b00000001
             *
             * What does each operand do?
             *
             * << - shift bits to the left:
             * before << 2   after << 2
             * 0b00000011    0b00001100
             *
             * | - bitwise OR (it adds all the 1s from both sides)
             * 0b00001100 | 0b00000001 -> 0b00001101
             *
             * This method provides incredibly fast O(1) lookup and allows SortingType.from() to look elegant
             *  and not be a several lines long if-else chain that is also slow.
             *
             * FAQ:
             * Q: Why shift over by 2?
             * A: There are 3 directions: NONE (0b00), ASCENDING (0b01), DESCENDING (0b10). This naturally fits
             *     into 2 bits.
             *
             * Q: Why do this instead of if-else?
             * A: Because this cycle is much more efficient:
             *       0.Program supplies field (ALPHABETICAL) and direction (DESCENDING)
             *                               [ if-else ]
             *    1.Check if field equals NONE
             *    2.Compare if field equals DATE_CREATED and direction equals ASCENDING
             *    3.Compare if field equals DATE_CREATED and direction equals DESCENDING
             *    4.Compare if field equals DATE_MODIFIED and direction equals ASCENDING
             *    5.Compare if field equals DATE_MODIFIED and direction equals DESCENDING
             *    ...
             *    n.Compare if the field equals ALPHABETICAL and direction equals DESCENDING
             *    n+1.Return ALPHABETICAL_DESCENDING
             *
             *                                [ lookup ]
             *    1.Check if field equals NONE
             *    2.Get "ID" by combining field and direction
             *    3.Look up "ID" inside map
             *    4.Return ALPHABETICAL_DESCENDING
             */
            int key = type.field.ordinal() << 2 | type.direction.ordinal();
            TYPE_LOOKUP_MAP.put(key, type);
        }
    }

    public static SortingType from(SortField field, SortDirection direction) {
        if (field.equals(SortField.NONE)) {
            return SortingType.NONE;
        } else {
            int key = field.ordinal() << 2 | direction.ordinal();
            SortingType type = TYPE_LOOKUP_MAP.get(key);
            return type != null? type: SortingType.NONE;
        }
    }

    public Comparator<Sortable> getComparator() {
        return comparator;
    }
}