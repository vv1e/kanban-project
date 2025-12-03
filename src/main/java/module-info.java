module edu.sdccd.cisc190.kanban {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires java.management;
    requires org.slf4j;

    opens edu.sdccd.cisc190.kanban to javafx.fxml;
    exports edu.sdccd.cisc190.kanban;
    exports edu.sdccd.cisc190.kanban.ui;
    opens edu.sdccd.cisc190.kanban.ui to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.util;
    opens edu.sdccd.cisc190.kanban.util to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.util.interfaces;
    exports edu.sdccd.cisc190.kanban.ui.components;
    opens edu.sdccd.cisc190.kanban.ui.components to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.models;
    opens edu.sdccd.cisc190.kanban.models to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.util.exceptions;
    opens edu.sdccd.cisc190.kanban.util.exceptions to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.enums;
    opens edu.sdccd.cisc190.kanban.enums to javafx.fxml;
}