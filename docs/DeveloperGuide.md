---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TripLog Developer Guide

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://junit.org/junit5/), [TestFX](https://github.com/TestFX/TestFX)
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

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/MainApp.java)) is in charge of the app launch and shut down.
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

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TripListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/resources/view/MainWindow.fxml).

To support a dynamic layout, the `MainWindow` implements a `SplitPane` with a vertical orientation. This `SplitPane` contains two `StackPane` placeholders which house the `TripListPanel` and `ResultDisplay` respectively. This allows the user to manually adjust the relative height of these components while maintaining a consistent internal layout.

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Trip` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1-3")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1-3` Command" />

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
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" alt="Better Model Class Diagram" />


The `Model` component,

* stores the trip log data i.e., all `Trip` objects (which are contained in a `UniqueTripList` object).
* stores the currently 'selected' `Trip` objects (e.g., results of a search query or a specific sort) as a separate _filtered/sorted_ list which is exposed to outsiders as an unmodifiable `ObservableList<Trip>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103-F13-2/tp/tree/master/src/main/java/seedu/triplog/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both trip log data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `TripLogStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.triplog.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

### Trip Creation: Add Command
The `add` command creates a new trip in the trip log. Only the destination name (n/NAME) is mandatory. All other fields (phone, email, address, start date, end date, tags) are optional and default to null if omitted. When both `startDate` and `endDate` are provided, the Trip model validates that the start date is not after the end date.

The parsing is handled by `AddCommandParser`, which tokenizes the input and checks each optional field via `Optional<String>` before constructing the corresponding `AddCommand`.

On execution, `AddCommand` checks for duplicates via `Model#hasTrip(Trip)`. Two trips are considered duplicates if they share the same name (case-insensitive) and have overlapping date ranges, as determined by the private `Trip#datesOverlap()` method. If trip is non-duplicate, it is added to the model.

The following sequence diagram shows how the `add` command is parsed and executed:

<puml src="diagrams/AddSequenceDiagram.puml" alt="Add Command Sequence Diagram" />

### Trip Editing: Edit Command
The `edit` command allows for partial updates to an existing trip. The trip to be edited is identified by its index in the current filtered list.

**Mechanism:**
1. **`EditCommandParser`**: Tokenizes the input and stores the changes in an `EditTripDescriptor`. This descriptor is a temporary container that holds only the fields the user intends to change.
2. **`EditCommand`**:
    * Retrieves the original trip from the model using the provided index.
    * Creates an `editedTrip` by merging the original trip's data with the non-empty fields in the `EditTripDescriptor`.
    * **Validation**: It checks that the `editedTrip` is not a duplicate of another trip (excluding the one being edited) and that the new date range is logically valid.
3. **Model Update**: If valid, the model replaces the original trip with the `editedTrip`.

**Duplicate Detection during Edit:**
The `edit` command utilizes `Model#hasTripExcluding(Trip, Trip)`. This ensures that if a user changes a trip's name to match another entry, the command is only rejected if the dates also overlap with that other entry.

### Trip Deletion: Delete Command

The `delete` command removes trip(s) from the currently displayed trip list. It supports four deletion modes:
* **Single deletion**: deletes a trip at a given index (e.g. `delete 2`)
* **Range deletion**: deletes trips within an index range (e.g. `delete 1-3`)
* **Field-match deletion**: deletes trips matching a field such as name or tag (e.g. `delete n/Tokyo`, `delete t/family`)
* **Date-range deletion**: deletes trips within a specified date range (e.g. `delete sd/2026-01-01 ed/2026-12-31`)

The parsing of the command is handled by `DeleteCommandParser`, which determines which of the four deletion modes is being used based on input format and validates that only one mode is specified per command.

Deletion is performed by `DeleteCommand`, which operates on the **currently displayed trip list**.
For field-match and date-range deletion, matching is handled by `TripMatchesDeletePredicate`:
* field-match deletion checks one specified prefix at a time (e.g. `n/`, `p/`, `t/`, `sd/`)
* date-range deletion (`sd/` and `ed/`) works as follows:
    * different dates → trip start date must be on/after `sd`, and end date must be on/before `ed`
    * same date → any trip happening on that day matches

To prevent accidental deletion, a **two-step confirmation flow** is implemented in `CommandBox`:

1. The first execution transforms the command into `deletepreview`, which shows the trips that would be deleted.
2. The second execution of the same command performs the actual deletion.
3. Editing the command cancels the pending deletion.

The preview is generated using `PreviewDeleteCommand`, which reuses the same parsing and selection logic as `DeleteCommand` to ensure consistency.

The following sequence diagram shows the logic for deleting a trip:

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Delete Command Sequence Diagram" />

### Tagging Trips: Tag Command

The `tag` command allows users to add a single tag to trips for easier organisation and filtering. It only supports tagging by index.

The parsing of the command is handled by `TagCommandParser`, which parses the required arguments and constructs the corresponding `TagCommand`.

The following sequence diagram illustrates the process of tagging a trip:

<puml src="diagrams/TagSequenceDiagram.puml" alt="Tag Command Sequence Diagram" />

### Filtering Trips by Date: Filter Command

The `filter` command allows user to filter by date range. It enforces both `sd/` and `ed/` present in the query, but accepts trips with null `sd/` and `ed/` fields. `sd/` >= `query sd/` must be present to pass the filter, `ed/` is optional but must be <= `query ed/` to pass the filter.

The following sequence diagram illustrates the process of filtering a trip:

<puml src="diagrams/FilterSequenceDiagram.puml" alt="Filter Command Sequence Diagram" />

### Trip Listing and Sorting: List Command

The `list` command has been enhanced to provide analytical feedback and dynamic reordering of the trip log. This implementation bridges the `Logic` and `Model` layers to transform a simple list view into a temporal dashboard.

#### Implementation

**Sorting Mechanism & Tie-Breaking:**
The sorting is implemented using a **Comparator Factory** pattern within `ListCommand`. The selection logic is encapsulated in `getComparator(key)`, which returns a specific comparator that often chains a primary key with a name-based fallback using `.thenComparing()`.

<puml src="diagrams/ListComparatorClassDiagram.puml" width="450" />

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
The trip statistics dashboard provides a temporal analysis of trips relative to `LocalDate.now()`. Categorization logic is centralized in `TripSummaryUtil`, using a sequential date-check flow to ensure mutually exclusive trip statuses.

<puml src="diagrams/TripSummaryActivityDiagram.puml" width="300" />

* **Centralized Logic**: The calculation logic is centralized in `TripSummaryUtil#calculateSummary(ObservableList<Trip>)`.
* **Live Updates**: The `ListCommand` retrieves the current filtered list via `Model#getFilteredTripList()` and passes it to the utility to generate the dashboard summary.
* **Status Determination**:
    1. **Planning**: `startDate == null`
    2. **Upcoming**: `today is before start date`
    3. **Completed**: `today is after end date`
    4. **Ongoing**: `else`

### Help command

#### Implementation

The `help` command supports two modes:

1. **Full help window** — `help` (no argument) opens a separate `HelpWindow` popup displaying syntax for all commands.
2. **Inline help** — `help COMMAND` (e.g., `help add`) displays the usage string for a specific command directly in the result display, without opening a window.

The mode is determined in `HelpCommand#execute()`:

* If no argument is given, a `CommandResult` with `showHelp = true` is returned. `MainWindow` detects this flag in `executeCommand()` and calls `handleHelp()`, which shows the window (if hidden) or focuses it (if already visible). The result display shows `SHOWING_HELP_MESSAGE` ("Opened help window.") in both cases. The help window can also be opened or focused via **F1** or the Help menu, which calls `handleHelp()` directly without changing the result display.
* If an argument is given, `getUsageForCommand(argument)` returns the matching usage string, and a regular `CommandResult` is returned. The text is displayed inline in the `ResultDisplay`.

The usage strings (e.g., `ADD_USAGE`, `DELETE_USAGE`) are defined as constants in `CommandUsage` and reused by both `HelpCommand` and `HelpWindow` to keep the content consistent between inline help and the popup window.

The `HelpWindow` is resizable. The `ScrollPane` (which is the scene root) grows to fill the window as the user resizes it, showing a vertical scrollbar only when the content exceeds the window height. A minimum size of 400×300 px is enforced on the `Stage` to prevent the window from being dragged to an unusably small size.

The `TripLogParser` routes `help` to `HelpCommand`:

* `help` → `new HelpCommand(arguments)` where `arguments` is `""`, equivalent in behaviour to `new HelpCommand()`
* `help add` → `new HelpCommand(arguments)` where `arguments` is `" add"`; the leading space is trimmed to `"add"` inside the constructor

#### Design considerations

**Aspect: Where to display per-command help**

* **Alternative 1 (current choice):** Display inline in the existing `ResultDisplay`.
    * Pros: No extra window; fast to access; keyboard-friendly.
    * Cons: Long usage text may get truncated if the result area is small.

* **Alternative 2:** Always open a `HelpWindow`, filtered to the requested command.
    * Pros: Consistent UI; more space for content.
    * Cons: More disruptive; requires an extra window management step.

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

### Use cases

(For all use cases below, the **System** is `TripLog` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - Add a trip**

**MSS**

1.  User requests to add a trip.
2.  TripLog adds the trip.
3.  TripLog shows the updated trip list.

    Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TripLog shows an error message.
    * Use case resumes at step 1.

* 1b. The trip details provided are invalid (e.g., end date before start date).
    * 1b1. TripLog shows an error message.
    * Use case resumes at step 1.

* 1c. The trip is a duplicate of an existing entry.
    * 1c1. TripLog shows an error message.
    * Use case resumes at step 1.

**Use case: UC2 - Delete a trip**

**MSS**

1.  User requests to list trips.
2.  TripLog shows a list of trips.
3.  User requests to delete a specific trip in the list.
4.  TripLog shows a preview of the trip to be deleted and asks for confirmation.
5.  User confirms the deletion.
6.  TripLog deletes the trip.

    Use case ends.

**Extensions**

* 2a. The list is empty.
    * Use case ends.

* 3a. The index provided is invalid.
    * 3a1. TripLog shows an error message.
    * Use case resumes at step 3.

* 5a. User edits the command instead of confirming.
    * 5a1. TripLog cancels the deletion.
    * Use case ends.

**Use case: UC3 - Filter trips by date**

**MSS**

1.  User requests to filter trips by a date range.
2.  TripLog updates the displayed list with trips satisfying the criteria.

    Use case ends.

**Extensions**

* 1a. The date range format is invalid.
    * 1a1. TripLog shows an error message.
    * Use case resumes at step 1.

* 1b. No trips match the criteria.
    * 1b1. TripLog shows an empty list.
    * Use case ends.

**Use case: UC4 - Edit a trip**

**MSS**

1.  User requests to list trips.
2.  TripLog shows a list of trips.
3.  User requests to edit a specific trip in the list.
4.  TripLog updates the trip with provided details.
5.  TripLog shows the updated trip in the list.

    Use case ends.

**Extensions**

* 2a. The list is empty.
    * Use case ends.

* 3a. The index provided is invalid.
    * 3a1. TripLog shows an error message.
    * Use case resumes at step 3.

* 3b. No fields to edit are provided.
    * 3b1. TripLog shows an error message.
    * Use case resumes at step 3.

* 4a. The new trip details are invalid (e.g., end date before start date).
    * 4a1. TripLog shows an error message.
    * Use case resumes at step 3.

* 4b. The edit results in a duplicate trip.
    * 4b1. TripLog shows an error message.
    * Use case resumes at step 3.

**Use case: UC5 - Tag a trip**

**MSS**

1.  User requests to list trips.
2.  TripLog shows a list of trips.
3.  User requests to tag a specific trip in the list.
4.  TripLog adds the tag to the trip.
5.  TripLog shows the updated trip with the new tag.

    Use case ends.

**Extensions**

* 2a. The list is empty.
    * Use case ends.

* 3a. The index provided is invalid.
    * 3a1. TripLog shows an error message.
    * Use case resumes at step 3.

* 3b. The tag name is invalid (not alphanumeric).
    * 3b1. TripLog shows an error message.
    * Use case resumes at step 3.

* 4a. The tag is already present on the trip.
    * 4a1. TripLog shows an error message.
    * Use case resumes at step 3.

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
- **Trip**: A travel record identified by a destination name, with optional fields such as start date, end date, address, and tags.
- **Destination**: Primary location of a trip (e.g "Mount Fuji"), mapped to the Name field
- **Experience Log**: Descriptive note added to a trip to record activities or reminders
- **Category Tag**: Label for grouping trips by purpose (e.g work) or region (e.g Japan)
- **Duration**: The total days between the start and end date of a trip.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

While TripLog originated from AB3, the transition to a travel-specific manager required significant architectural changes:

* **Temporal Logic Implementation**: Unlike static contact data, `Trip` objects are time-dependent. We implemented logic to handle overlapping dates for duplicate detection and created `TripSummaryUtil` to recalculate trip statuses relative to the system clock (`LocalDate.now()`).
* **Complex Command Parsing**: The `delete" command was overhauled to support four distinct modes (Index, Range, Field-Match, and Date-Range). This required a sophisticated `DeleteCommandParser` and custom state-management logic in the UI for the "two-step confirmation" process without modifying the core `Logic` interface.
* **State Persistence**: We expanded `UserPrefs` to include the last-used `sort order`. This required coordination between `Logic` and `Storage` to ensure the app launches exactly as the user left it.
* **Dynamic UI Layout**: We replaced the static vertical stacking of the Trip List and Result Display with a vertically-oriented `SplitPane`. This allows users to manually adjust the height of the feedback area, which is critical for viewing large results (e.g., help commands) without obstructing the primary list.
* **Enhanced UI Feedback**: We integrated dynamic icons and success/error styling in the `ResultDisplay` and `CommandBox`, moving away from AB3's purely text-based feedback.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team size: 6**

1. **Improve Error Message for Invalid Indices**: Currently, when an index is out of bounds or not a positive integer, the app shows a generic error. We plan to specify the valid range based on the current list size (e.g., "Index must be between 1 and 5").
2. **Support for Non-English Characters**: We plan to allow Unicode characters in the destination `NAME` field to support international travel records (e.g., Tokyo / 東京).
3. **Multi-Field Substring Search**: The `find` command currently only searches the Name field. We plan to expand substring matching to include Address and Tags fields simultaneously.
4. **Refine Phone and Email Validation**: Currently, the validation for phone numbers and emails follows a strict alphanumeric format. We plan to allow more flexible symbols (e.g., "+" for country codes in phone numbers) to support international contact details.
5. **Clearer Duplicate Detection Feedback**: When a duplicate trip is rejected, we plan to specify which existing entry it overlaps with (e.g., "Overlaps with trip at index 2") in the feedback box.
6. **Custom Icons Toggle**: Provide a setting in `preferences.json` to allow users to toggle off the custom `[OK]` and `[!!]` icons for a minimalist UI.

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

### Command Word Case-Insensitivity

1. Testing command word variations
    1. Prerequisites: App launched with sample data.
    2. Test case: `ADD n/Paris`
       Expected: Trip to Paris is added (case-insensitive for command word).
    3. Test case: `lIsT`
       Expected: Trip list is displayed normally.
    4. Test case: `EXIT`
       Expected: Application shuts down.

### Adding a trip

1. Adding a trip with only the required field
    1. Test case: `add n/Tokyo`
       Expected: Trip with name "Tokyo" is added. All other fields show as blank/absent.

2. Adding a trip with all fields
    1. Test case: `add n/Osaka p/91234567 e/trip@mail.com a/Dotonbori sd/2026-06-01 ed/2026-06-10 t/food t/travel`
       Expected: Trip is added with all fields populated and both tags shown.

3. Adding a trip with optional fields omitted
    1. Test case: `add n/Kyoto sd/2026-07-01 ed/2026-07-10`
       Expected: Trip is added. Phone, email, and address are absent.

4. Invalid date order
    1. Test case: `add n/Seoul sd/2026-12-31 ed/2026-01-01`
       Expected: Error message indicating start date cannot be after end date. No trip is added.

5. Duplicate trip (same name, overlapping dates)
    1. Prerequisites: A trip named "Tokyo" with dates 2026-06-01 to 2026-06-10 already exists.
    2. Test case: `add n/Tokyo sd/2026-06-05 ed/2026-06-15`
       Expected: Error message indicating duplicate trip. No trip is added.

6. Same name, non-overlapping dates (allowed)
    1. Prerequisites: A trip named "Tokyo" with dates 2026-06-01 to 2026-06-10 already exists.
    2. Test case: `add n/Tokyo sd/2026-09-01 ed/2026-09-10`
       Expected: Trip is added successfully. Both "Tokyo" trips coexist.

7. Missing name
    1. Test case: `add sd/2026-06-01 ed/2026-06-10`
       Expected: Error message indicating invalid command format. No trip is added.

### Editing a trip

1. Prerequisites: List all trips using the `list` command. Multiple trips in the list.

2. Editing a single field
    1. Test case: `edit 1 n/New Destination Name`
       Expected: Only the name of the first trip is updated. All other fields remain unchanged.

3. Editing dates (valid range)
    1. Prerequisites: Trip 1 has dates 2026-01-01 to 2026-01-10.
    2. Test case: `edit 1 sd/2026-01-05`
       Expected: Start date updated to Jan 5th. End date remains Jan 10th.

4. Editing dates (invalid range)
    1. Prerequisites: Trip 1 has start date 2026-01-01 and end date 2026-01-10.
    2. Test case: `edit 1 ed/2025-12-31`
       Expected: Error message indicating start date cannot be after end date. No changes made.

5. No changes from original values
    1. Test case: `edit 1 n/Original Name` (where trip 1 already has "Original Name")
       Expected: Error message indicating edited fields are the same as the original. No changes made.

6. Creating a duplicate via edit
    1. Prerequisites: Trip 1 is "Tokyo" (2026-01-01 to 2026-01-10). Trip 2 is "Osaka" (same dates).
    2. Test case: `edit 2 n/Tokyo`
       Expected: Error message indicating the trip already exists (duplicate). No changes made.

7. Clearing tags
    1. Test case: `edit 1 t/`
       Expected: All tags are removed from the first trip.

8. No fields provided
    1. Test case: `edit 1`
       Expected: Error message indicating at least one field must be provided.

### Listing, Sorting, and Statistics

1. Initial setup (Assume Today is 2026-04-02)
    1. Add a variety of trips:
        - Past: `add n/History sd/2020-01-01 ed/2020-01-05`
        - Ongoing: `add n/Current Trip sd/2026-04-01 ed/2026-04-10`
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

4. Testing Startup Summary and Persistence
    1. Prerequisites: App contains trips with various dates.
    2. Test case: Open the application.
       Expected: The Result Display immediately shows a summary dashboard and the last used sort order without entering any commands.
    3. Test case: Sort the list using `list sort/name`, exit the application, and re-launch.
       Expected: The summary dashboard and the list itself remain sorted by name alphabetically.

### Tagging a trip

1. Tagging a trip using index
    1. Prerequisites: List all trips using the `list` command. Multiple trips in the list.

    2. Test case: `tag 1 scenic beauty`
       Expected: First trip is tagged with `scenic beauty`. Details of the tagged trip shown in the status message.

2. Duplicate tag (case insensitive)
    1. Prerequisites: A trip named "Hotel California" with tag "hotel" already exists.

    2. Test case: `tag 1 HOTEL`
       Expected: Error message indicating duplicate tag. No tag is added.

### Locating trips by name

1. Testing partial word matching
    1. Prerequisites: App launched with sample data (e.g., "Tokyo Japan").
    2. Test case: `find Tok`<br>
       Expected: "Tokyo Japan" is found (partial match).
    3. Test case: `find tokyo`<br>
       Expected: "Tokyo Japan" is found (case-insensitive).
    4. Test case: `find Tok Jap`<br>
       Expected: "Tokyo Japan" is found (multiple partial keywords).

### Deleting a trip

1. Deleting a trip using index
    1. Prerequisites: List all trips using the `list` command. Multiple trips in the list.
    2. Test case: `delete 1`  
       Expected: A preview of the first trip is shown. No trip is deleted yet.
    3. Test case: Press Enter again with `delete 1`  
       Expected: First trip is deleted from the list. Details of the deleted trip shown in the status message.

2. Cancelling deletion
    1. Test case: `delete 1`, then modify the command instead of confirming  
       Expected: No trip is deleted.

3. Invalid index
    1. Test case: `delete 0`  
       Expected: No trip is deleted. Error message shown.

4. Deleting a range of trips
    1. Test case: `delete 1-3`  
       Expected: Preview of trips 1 to 3 is shown.  
       After confirmation, all three trips are deleted.

5. Deleting by field
    1. Test case: `delete n/Tokyo`  
       Expected: All trips with name "Tokyo" are previewed, then deleted after confirmation.

6. Deleting by date range
    1. Test case: `delete sd/2026-03-01 ed/2026-05-10`  
       Expected: Trips matching the specified date range are previewed, then deleted after confirmation.

### Saving data

1. Dealing with missing/corrupted data files

    1. To simulate a missing file, simply delete the `triplog.json` file from the `data/` folder while the app is closed.
    2. Expected: Upon re-launching, TripLog should initialize with a clean set of sample data.
    3. To simulate a corrupted file, open `triplog.json` in a text editor and delete a necessary bracket or comma to break the JSON structure.
    4. Expected: Upon re-launching, TripLog should detect the invalid format, discard the data, and show the message `[!!] Data file error: Corrupted entry detected. Starting fresh.`

--------------------------------------------------------------------------------------------------------------------
