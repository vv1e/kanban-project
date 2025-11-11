package edu.sdccd.cisc190.kanban.util;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

public class ObjectHelper {
    public static void removeNodes(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    public static void setControlToReadonly(TextInputControl... controls) {
        for (TextInputControl control : controls) {
            control.setEditable(false);
            control.setFocusTraversable(false);
        }
    }
}
