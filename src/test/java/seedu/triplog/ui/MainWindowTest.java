package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.logic.Logic;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.trip.Trip;

@ExtendWith(ApplicationExtension.class)
public class MainWindowTest {

    private MainWindow mainWindow;
    private Stage stage;
    private final String error = "Data file error: Corrupted entry detected. Starting fresh.";

    private class LogicStub implements Logic {
        private String errorToReturn;

        LogicStub(String errorToReturn) {
            this.errorToReturn = errorToReturn;
        }

        @Override
        public CommandResult execute(String cmd) {
            return null;
        }

        @Override
        public ReadOnlyTripLog getTripLog() {
            return null;
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Trip> getSortedTripList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public Path getTripLogFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings gui) {
        }

        @Override
        public String getInitialDataLoadError() {
            return errorToReturn;
        }

        @Override
        public String getSummary() {
            return "Summary: 0 Upcoming, 0 Ongoing, 0 Completed, 0 Planning";
        }
    }

    @Start
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void fillInnerParts_withError_updatesResultDisplay(FxRobot robot) {
        robot.interact(() -> {
            mainWindow = new MainWindow(stage, new LogicStub(error));
            mainWindow.fillInnerParts();
        });

        try {
            Field resultDisplayField = MainWindow.class.getDeclaredField("resultDisplay");
            resultDisplayField.setAccessible(true);
            ResultDisplay rd = (ResultDisplay) resultDisplayField.get(mainWindow);

            Field textAreaField = ResultDisplay.class.getDeclaredField("resultDisplay");
            textAreaField.setAccessible(true);
            TextArea textArea = (TextArea) textAreaField.get(rd);

            assertTrue(textArea.getText().contains(error), "Result display should contain the load error.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Reflection failed to access UI components", e);
        }
    }
}
