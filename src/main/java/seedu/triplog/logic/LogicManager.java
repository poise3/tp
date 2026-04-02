package seedu.triplog.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.commons.core.LogsCenter;
import seedu.triplog.logic.commands.Command;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.logic.commands.TripSummaryUtil;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.logic.parser.TripLogParser;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";
    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final TripLogParser tripLogParser;
    private final String initialDataLoadError;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this(model, storage, null);
    }

    /**
     * Constructs a {@code LogicManager} with the given {@code Model}, {@code Storage}, and an initialization error.
     */
    public LogicManager(Model model, Storage storage, String initialDataLoadError) {
        this.model = model;
        this.storage = storage;
        this.initialDataLoadError = initialDataLoadError;
        tripLogParser = new TripLogParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = tripLogParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveTripLog(model.getTripLog());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyTripLog getTripLog() {
        return model.getTripLog();
    }

    @Override
    public ObservableList<Trip> getFilteredTripList() {
        return model.getFilteredTripList();
    }

    @Override
    public ObservableList<Trip> getSortedTripList() {
        return model.getSortedTripList();
    }

    @Override
    public Path getTripLogFilePath() {
        return model.getTripLogFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public String getInitialDataLoadError() {
        return initialDataLoadError;
    }

    @Override
    public String getSummary() {
        String summary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        String sortDescription = model.getLastSortDescription();

        if (sortDescription == null) {
            sortDescription = ListCommand.SORT_DESC_START;
        }

        return String.format(ListCommand.MESSAGE_SUCCESS, sortDescription, summary);
    }
}
