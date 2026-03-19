---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TripLog User Guide

TripLog is a **desktop app for managing trips, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TripLog can get your travel management tasks done faster than traditional GUI apps.

<page-nav-print />

---

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/triplog/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your TripLog.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar triplog.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:
   - `list` : Lists all trips.
   * `add n/Tokyo, Japan sd/2026-03-10 ed/2026-03-20` : Adds a trip to Tokyo.

   * `delete 3` : Deletes the 3rd trip shown in the current list.

   * `clear` : Deletes all entries.
   - `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

---

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

- Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

- Values with spaces do not need quotes — the parser reads up to the next prefix.<br>
  e.g. `n/New York` and `a/123 Main St` are both valid.

- Items in square brackets are optional.<br>
  e.g `n/NAME [sd/DATE]` can be used as `n/Tokyo sd/2026-01-01` or as `n/Tokyo`.

- Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/nature`, `t/nature t/photo` etc.

- Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE`, `p/PHONE n/NAME` is also acceptable.

- Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.
  </box>

### Viewing help : `help`

Shows a help window explaining the command syntax and parameter requirements.

![help message](images/helpMessage.png)

Format: `help`

- The help window can be closed by clicking the 'x' button, or by pressing **Q** or **ESCAPE** while the window is focused.

### Adding a trip: `add`

Adds a trip to the log.

Format: `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [sd/START_DATE] [ed/END_DATE] [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A trip can have any number of tags (including 0).
</box>

Examples:

- `add n/Tokyo sd/2026-03-10 ed/2026-03-20`
- `add n/New York a/123 5th Ave t/business t/high priority`

### Listing all trips : `list`

Shows a list of all trips sorted by start date in ascending order (earliest first). Trips with no start date are shown last.

Format: `list`

Example:

- `list` — displays all trips ordered from earliest to latest start date

### Editing a trip : `edit`

Edits an existing trip in the trip log.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [sd/DATE] [ed/DATE] [t/TAG]…​`

- Edits the trip at the specified `INDEX`. The index refers to the index number shown in the displayed trip list. The index **must be a positive integer** 1, 2, 3, …​
- At least one of the optional fields must be provided.
- Existing values will be updated to the input values.
- When editing tags, the existing tags of the trip will be removed i.e adding of tags is not cumulative.
- You can remove all the trip’s tags by typing `t/` without specifying any tags after it.

Examples:

- `edit 1 p/91234567 e/johndoe@example.com` Edits the phone and email of the 1st trip.
- `edit 2 n/Betsy Crower t/` Edits the name of the 2nd trip and clears all tags.

### Tagging a trip : `tag`

Tags an existing trip in the address book with the given keyword.

Format: `tag INDEX TAG`

* Tags the trip with the keyword `TAG` at the specified `INDEX`. The index refers to the index number shown in the displayed trip list. The index **must be a positive integer** 1, 2, 3, …​
* Tags must be alphanumeric (A-Z, 0-9)
* Duplicate tags will not be added

Examples:
* `tag 1 scenic beauty` Tags the 1st trip with `scenic beauty`.
* `tag 2 hotel` Tags the 2nd trip with `hotel`.


### Locating trips by name: `find`

Finds trips whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

- The search is case-insensitive. e.g `hans` will match `Hans`
- The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
- Only the name is searched.
- Only full words will be matched e.g. `Han` will not match `Hans`
- Trips matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:

- `find John` returns `john` and `John Doe`
- `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting trip(s) : `delete`

Deletes trip(s) from the currently displayed trip list.

Format: `delete INDEX`  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`delete START-END`  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`delete PREFIX/VALUE`

- The command operates on the currently displayed trip list.

#### Delete by index

- Deletes the trip at the specified `INDEX`.
- The index refers to the index number shown in the displayed trip list.
- The index **must be a positive integer** 1, 2, 3, …​

Examples:

- `delete 2` deletes the 2nd trip in the current list.
- `find Tokyo` followed by `delete 1` deletes the 1st trip in the filtered results.

#### Delete by range

- Deletes trips from `START` to `END` (inclusive).
- Both `START` and `END` must be positive integers.
- `START` must be less than or equal to `END`.
- The range must be within the currently displayed list.

Examples:

- `delete 1-3` deletes the 1st to 3rd trips.
- `delete 2-2` deletes only the 2nd trip.

#### Delete by field

- Deletes all trips that match a specified field.
- Only **one field** can be used at a time.

Supported prefixes:

- `n/NAME`
- `p/PHONE`
- `e/EMAIL`
- `a/ADDRESS`
- `sd/START_DATE`
- `ed/END_DATE`
- `t/TAG`

Examples:

- `delete n/Tokyo` deletes all trips named "Tokyo".
- `delete t/family` deletes all trips with the tag "family".
- `delete sd/2026-03-01` deletes all trips with this start date.

### Filtering by date range : `filter`

Filter trips by a given date range.

Format: `filter sd/START_DATE ed/END_DATE`

* Update the displayed list with trips satisfying this criteria: 
START_DATE <= trip start date (required) <= trip end date (optional) <= END_DATE
* START_DATE and END_DATE must be provided in YYYY-MM-DD format.
* Ignores existing trip logs without starting date present 

Examples:

- `filter sd/2026-01-01 ed/2026-03-01` will filter all trips within this period

### Clearing all entries : `clear`

Clears all entries from the trip log.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TripLog data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TripLog data are saved automatically as a JSON file `[JAR file location]/data/triplog.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, TripLog will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TripLog to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

---

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TripLog home folder.

---

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

---

## Command summary

| Action     | Format, Examples                                                                                                                                                         |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Add**    | `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [sd/DATE] [ed/DATE] [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 sd/2026-01-01 t/friend` |
| **Clear**  | `clear`                                                                                                                                   |
| **Delete** | `delete INDEX`<br> e.g., `delete 3`                                                                                                       |
| **Edit**   | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [sd/DATE] [ed/DATE] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com` |
| **Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find Tokyo Osaka`                                                                                                              |
| **List**   | `list`                                                                                                                                                                   |
| **Help**   | `help`                                                                                                                                                                   |
