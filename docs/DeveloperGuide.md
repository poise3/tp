---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TripLog Developer Guide

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://junit.org/junit5/)
* Original codebase: [AddressBook-Level3](https://github.com/se-edu/addressbook-level3)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Trip` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `TripLogParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a trip).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `TripLogParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `TripLogParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the trip log data i.e., all `Trip` objects (which are contained in a `UniqueTripList` object).
* stores the currently 'selected' `Trip` objects (e.g., results of a search query or a specific sort) as a separate _filtered/sorted_ list which is exposed to outsiders as an unmodifiable `ObservableList<Trip>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both trip log data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `TripLogStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.triplog.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

### Trip Statistics and Multi-Key Sorting

#### Implementation

The `ListCommand` has been enhanced to provide analytical feedback and dynamic reordering of the trip log. This implementation bridges the `Logic` and `Model` layers to transform a simple list view into a temporal dashboard.

**Sorting Mechanism & Tie-Breaking:**
The sorting is implemented using a **Comparator Factory** pattern within `ListCommand`. The application supports dynamic reordering based on four primary sort keys: `name`, `start`, `end`, and `len`.

The following sequence diagram illustrates the interactions within the `Logic` and `Model` components when a user executes a sort command:

<puml src="diagrams/ListSortSequenceDiagram.puml" width="550" alt="Sequence Diagram for List Sorting" />

To ensure a stable and deterministic user experience, a **Multi-Level Tie-Breaker** is implemented:
1. **Primary Key**: The user-selected key (e.g., `start date`).
2. **Secondary Key (Tie-breaker)**: A case-insensitive alphabetical sort by trip name retrieved via `Trip#getNameLowerCase()`.

**Technical Workflow:**
1. **`ListCommandParser`**: Intercepts the `list` command and identifies the `sort/` prefix.
2. **`getComparator(key)`**: In `ListCommand`, this method chains the primary comparator with a name-based fallback using Java's `.thenComparing()` method.
3. **Persistent Sorting**: The sort order is maintained in the `ModelManager` via a `SortedList` wrapper. Any subsequent operations (adding or editing trips) automatically re-apply the last used comparator through `Model#updateSortedTripList(Comparator<Trip>)`.

**Temporal Dashboard:**
The trip statistics dashboard provides a temporal analysis of trips relative to `LocalDate.now()`.

<puml src="diagrams/LogicClassDiagram.puml" width="550" alt="Logic Class Diagram showing Summary Utility" />

* **Centralized Logic**: The calculation logic is centralized in `TripSummaryUtil#calculateSummary(ObservableList<Trip>)`.
* **Live Updates**: The `ListCommand` retrieves the current filtered list via `Model#getFilteredTripList()` and passes it to the utility to generate the dashboard summary.
* **Status Determination**:
    1. **Planning**: `startDate == null`
    2. **Upcoming**: `today < startDate`
    3. **Completed**: `today > endDate`
    4. **Ongoing**: `startDate <= today <= endDate`

### Help command

#### Implementation

The `help` command supports two modes:

1. **Full help window** — `help` (no argument) opens a separate `HelpWindow` popup displaying syntax for all commands.
2. **Inline help** — `help COMMAND` (e.g., `help add`) displays the usage string for a specific command directly in the result display, without opening a window.

The mode is determined in `HelpCommand#execute()`:

* If no argument is given, a `CommandResult` with `showHelp = true` is returned. `MainWindow` detects this flag and calls `handleHelp()` to show the `HelpWindow`.
* If an argument is given, `getUsageForCommand(argument)` returns the matching usage string, and a regular `CommandResult` is returned. The text is displayed inline in the `ResultDisplay`.

The usage strings (e.g., `ADD_USAGE`, `DELETE_USAGE`) are defined as constants in `HelpCommand` and reused by `HelpWindow` to keep the content consistent between inline help and the popup window.

The `TripLogParser` routes `help` to `HelpCommand`:

* `help` → `new HelpCommand()`
* `help add` → `new HelpCommand("add")`

#### Design considerations

**Aspect: Where to display per-command help**

* **Alternative 1 (current choice):** Display inline in the existing `ResultDisplay`.
    * Pros: No extra window; fast to access; keyboard-friendly.
    * Cons: Long usage text may get truncated if the result area is small.

* **Alternative 2:** Always open a `HelpWindow`, filtered to the requested command.
    * Pros: Consistent UI; more space for content.
    * Cons: More disruptive; requires an extra window management step.

--------------------------------------------------------------------------------------------------------------------

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedTripLog`. It extends `TripLog` with an undo/redo history, stored internally as an `tripLogStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedTripLog#commit()` — Saves the current trip log state in its history.
* `VersionedTripLog#undo()` — Restores the previous trip log state from its history.
* `VersionedTripLog#redo()` — Restores a previously undone trip log state from its history.

These operations are exposed in the `Model` interface as `Model#commitTripLog()`, `Model#undoTripLog()` and `Model#redoTripLog()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedTripLog` will be initialized with the initial trip log state, and the `currentStatePointer` pointing to that single trip log state.

Step 2. The user executes `delete 5` command to delete the 5th trip in the trip log. The `delete` command calls `Model#commitTripLog()`, causing the modified state of the trip log after the `delete 5` command executes to be saved in the `tripLogStateList`, and the `currentStatePointer` is shifted to the newly inserted trip log state.

Step 3. The user executes `add n/David …​` to add a new trip. The `add` command also calls `Model#commitTripLog()`, causing another modified trip log state to be saved into the `tripLogStateList`.

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitTripLog()`, so the trip log state will not be saved into the `tripLogStateList`.

</box>

Step 4. The user now decides that adding the trip was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoTripLog()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous trip log state, and restores the trip log to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial TripLog state, then there are no previous TripLog states to restore. The `undo` command uses `Model#canUndoTripLog()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The `redo` command does the opposite — it calls `Model#redoTripLog()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the trip log to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `tripLogStateList.size() - 1`, pointing to the latest trip log state, then there are no undone TripLog states to restore. The `redo` command uses `Model#canRedoTripLog()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the trip log, such as `list`, will usually not call `Model#commitTripLog()`, `Model#undoTripLog()` or `Model#redoTripLog()`. Thus, the `tripLogStateList` remains unchanged.

Step 6. The user executes `clear`, which calls `Model#commitTripLog()`. Since the `currentStatePointer` is not pointing at the end of the `tripLogStateList`, all trip log states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of trips and travel records
* wants a way to log trips, destinations and activities in an organized and structured manner
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

- Productivity: Enable Travelers to quickly record and retrieve travel experiences in a fast, distraction-free CLI. Record and search trips without switching apps or dealing with cluttered interfaces.
- Organization: Tag and jot quick notes about the destinations. Organize entries with tags for easy filtering and retrieval later.
- Simplicity: Lightweight solution and bypass slow, feature-heavy mobile travel apps. No installation of heavy software; runs directly in the terminal with minimal setup.
- Privacy: Fully local application with no cloud communication. No risk of data leakage or slow network latency.

### User stories

Priorities: Essential (must have) MVP, High (expected to have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                   | I want to …​                                                     | So that I can…​                                              |
|----------|---------------------------|------------------------------------------------------------------|--------------------------------------------------------------|
| `MVP`    | traveler                  | add a trip entry with just a location name                       | record where I am                                            |
| `MVP`    | software user             | delete wrong entries                                             | my travel log remains accurate and clean                     |
| `MVP`    | traveler                  | tag trips based on category                                      | be aware of the activity/purpose of each trip quickly        |
| `MVP`    | traveler                  | search my entries / logs for tags                                | see whether i have done a specific activity in that region   |
| `* * *`  | traveler                  | update the description of a trip entry                           | efficiently correct typos or add more detail to a trip       |
| `* * *`  | traveler                  | list all trips sorted by various criteria (name, date, duration) | view my travel history according to my current needs         |
| `* * *`  | traveler                  | see a summary dashboard of my trips                              | get a high-level view of my travel status                    |
| `* * *`  | new user                  | use a generic help command to recover the syntax of the commands | use the CLI without the need to memorise all instructions    |
| `* * *`  | traveler                  | add start and end dates to a trip                                | distinguish short trips from long journeys                   |
| `* * *`  | traveler                  | view trips taken within a given date range                       | analyze travel patterns over time                            |
| `* * *`  | traveler                  | record multiple cities in one trip                               | log complex itineraries                                      |
| `* *`    | traveler                  | mark a trip as "Completed"                                       | distinguish between upcoming plans and past trips            |
| `* *`    | returning traveler        | mark activities in a region as missing or haven't tried yet      | search for it and pick up the experience in an upcoming plan |
| `* *`    | traveler                  | attach short personal notes to a trip                            | remember meaningful experiences beyond basic facts           |
| `*`      | photographer traveler     | link to photos in the local file system                          | retrieve relevant photos quickly                             |
| `*`      | budget-conscious traveler | track the expenses at each destination                           | analyze the budget and plan accurately next time             |

### Non-Functional Requirements

1. TripLog should work on Windows, Linux, and macOS systems with Java `17` installed.
2. TripLog should run without requiring an installer and should be distributed as a single JAR file, or as a single ZIP file only if additional required files cannot be packaged into the JAR.
3. TripLog should allow users to perform all core operations, including adding, listing, deleting, and tagging trips, through typed commands without requiring mouse-based input.
4. TripLog should be designed for use by one user only and should not support concurrent access to the same data during normal operation.
5. TripLog should store all application data locally in a human-editable text file and should not use a DBMS for storage.
6. TripLog should allow all core features to function without requiring an Internet connection or a remote server.
7. TripLog should support at least 1000 trip records, with typical commands such as `add`, `delete`, `tag`, and `list` completing within 2 seconds on a standard personal computer.
8. TripLog should display error messages for invalid commands and invalid input that state the cause of the error and the expected command format.
9. TripLog should work well at screen resolutions of 1920×1080 and above with 100% and 125% display scaling, and remain usable at 1280×720 and above with 150% display scaling.
10. TripLog should be implemented using a modular object-oriented design so that new commands or trip fields can be added with changes localized to a small number of components.

### Glossary

- **Mainstream OS**: Windows, Linux, Unix, MacOS
- **Trip**: Travel entry defined by destination, start date, and end date
- **Destination**: Primary location of a trip (e.g "Mount Fuji"), mapped to the Name field
- **Experience Log**: Descriptive note added to a trip to record activities or reminders
- **Category Tag**: Label for grouping trips by purpose (e.g work) or region (e.g Japan)
- **Duration**: The total days between the start and end date of a trip.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Listing, Sorting, and Statistics

1. Initial setup (Assume Today is 2026-03-24)
    1. Add a variety of trips:
        - Past: `add n/History sd/2020-01-01 ed/2020-01-05`
        - Ongoing: `add n/Current Trip sd/2026-03-20 ed/2026-03-30`
        - Future: `add n/Future Trip sd/2026-12-01 ed/2026-12-10`
        - Planning: `add n/Bucket List Ideas`

2. Testing Sort Keys and Statistics
    1. Test case: `list sort/name`<br>
       Expected: Trips are sorted alphabetically. Result box shows: `Listed all trips sorted by name (alphabetical). Summary: 1 Upcoming, 1 Ongoing, 1 Completed, 1 Planning`.
    2. Test case: `list sort/len`<br>
       Expected: Trips are sorted by duration (longest first).
    3. Test case: `list sort/price`<br>
       Expected: Error message "Invalid sort key! Supported keys: name, start, end, len".

3. Testing Persistence
    1. Test case: Execute `list sort/name`, then `add n/B-Destination`.
       Expected: The new trip is added and automatically positioned in alphabetical order.

### Deleting a trip

1. Deleting a trip while all trips are being shown

    1. Prerequisites: List all trips using the `list` command. Multiple trips in the list.

    1. Test case: `delete 1`<br>
       Expected: First trip is deleted from the list. Details of the deleted trip shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No trip is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_
