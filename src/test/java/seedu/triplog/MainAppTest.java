package seedu.triplog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.triplog.commons.exceptions.DataLoadingException;
import seedu.triplog.logic.LogicManager;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.storage.Storage;
import seedu.triplog.storage.StorageManager;

public class MainAppTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void initModelManager_corruptedStorage_hitsCoverageLines() throws Exception {
        Path filePath = temporaryFolder.resolve("triplog.json");
        Storage storageStub = new StorageManager(null, null) {
            @Override
            public Optional<ReadOnlyTripLog> readTripLog() throws DataLoadingException {
                throw new DataLoadingException(new Exception("Forced failure"));
            }
            @Override
            public Path getTripLogFilePath() {
                return filePath;
            }
        };

        MainApp mainApp = new MainApp();
        String expected = "Data file error: Corrupted entry detected. Starting fresh.";

        Method initModel = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, seedu.triplog.model.ReadOnlyUserPrefs.class);
        initModel.setAccessible(true);
        initModel.invoke(mainApp, storageStub, new UserPrefs());

        assertEquals(expected, getPrivateInitialDataLoadError(mainApp));

        mainApp.model = new seedu.triplog.model.ModelManager();
        mainApp.storage = storageStub;
        mainApp.logic = new LogicManager(mainApp.model, mainApp.storage, expected);

        assertEquals(expected, mainApp.logic.getInitialDataLoadError());
    }

    private String getPrivateInitialDataLoadError(MainApp app) throws Exception {
        Field field = MainApp.class.getDeclaredField("initialDataLoadError");
        field.setAccessible(true);
        return (String) field.get(app);
    }
}
