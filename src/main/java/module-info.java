module edu.sdccd.cisc190.kanban {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens edu.sdccd.cisc190.kanban to javafx.fxml;
    exports edu.sdccd.cisc190.kanban;
    exports edu.sdccd.cisc190.kanban.ui;
    opens edu.sdccd.cisc190.kanban.ui to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.util;
    opens edu.sdccd.cisc190.kanban.util to javafx.fxml;
    exports edu.sdccd.cisc190.kanban.ui.components;
    opens edu.sdccd.cisc190.kanban.ui.components to javafx.fxml;
}