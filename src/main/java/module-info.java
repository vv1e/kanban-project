module edu.sdccd.cisc190.kanban {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.sdccd.cisc190.kanban to javafx.fxml;
    exports edu.sdccd.cisc190.kanban;
}