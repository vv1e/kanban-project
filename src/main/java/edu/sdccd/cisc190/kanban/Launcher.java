package edu.sdccd.cisc190.kanban;

import javafx.application.Application;

import java.lang.management.ManagementFactory;

public class Launcher {
    public static final boolean DEBUG_MODE_ENABLED = isDebugMode();

    public static void main(String[] args) {
        Application.launch(KanbanApplication.class, args);
    }

    private static boolean isDebugMode() {
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("-agentlib:jdwp")) {
                return true;
            }
        }
        return false;
    }
}
