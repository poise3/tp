# Derrick Low - Project Portfolio for TripLog

## Project Overview
**TripLog** is a CLI-centric desktop application designed for travelers who prefer managing their itineraries via a terminal interface. It allows users to track destinations and trip schedules, providing a streamlined, keyboard-first alternative to traditional GUI-based travel planners.

### Summary of Contributions

* **Code Contribution**: [Link to Repo Analyzer](https://nus-cs2103-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=derrikzzz&tabRepo=AY2526S2-CS2103-F13-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Feature Enhanced: Help Command with Inline Mode**
    * **What it does**: Extended the `help` command to support two modes — a **full help window** (no argument) that opens a popup listing all command syntaxes, and an **inline mode** (`help COMMAND`) that displays the usage string for a specific command directly in the result display without opening a window.
    * **Justification**: Users who already know most commands should be able to quickly look up one specific command without being interrupted by a full popup window. The inline mode keeps the user in the flow of typing commands.
    * **Highlights**: Added `getUsageForCommand()` in `HelpCommand` to map command keywords to their usage strings, and handled unknown command arguments gracefully by returning a clear error message. The `TripLogParser` was updated to route `help COMMAND` to `new HelpCommand(argument)`.

* **Feature Enhanced: Help Window UI Overhaul**
    * **What it does**: Redesigned the `HelpWindow` popup to display structured command syntax for all commands. Replaced the static layout with a `ScrollPane`, added a key event filter so users can dismiss the window with **Q** or **ESCAPE**, enforced a minimum window size of 400×300 px, and included a note on how to close the window.
    * **Justification**: The original help window showed only a URL to the user guide. A usable in-app reference that lists all command syntaxes removes the need for users to leave the application.
    * **Highlights**: The layout uses pre-declared `@FXML` `Label` nodes (defined in `HelpWindow.fxml`) that are populated from `CommandUsage` constants in the constructor, ensuring the popup and the inline help always display identical content.

* **Refactor: Centralised Command Usage Strings (`CommandUsage`)**
    * **What it does**: Extracted all command usage strings (e.g., `ADD_USAGE`, `DELETE_USAGE`) into a single `CommandUsage` class in `commons.core`. Both `HelpCommand` and `HelpWindow` reference these constants.
    * **Justification**: Having usage strings duplicated across `HelpCommand` and `HelpWindow` meant that any edit had to be made in two places. A single source of truth prevents the inline help and the popup from diverging.
    * **Highlights**: Refactored switch statements in `HelpCommand` and display logic in `HelpWindow` to reference the new constants.

* **Documentation Contributions**:
    * **User Guide**: Authored the section on the `help` command, documenting both the full window mode and inline mode, keyboard shortcuts for closing the window, and resizing behaviour. Fixed consistency issues across multiple UG sections.
    * **Developer Guide**: Authored the implementation section for the `help` command, covering the dual-mode design, the role of `CommandUsage`, `HelpWindow` resizing, and the `TripLogParser` routing logic. Also included design considerations comparing inline display vs. a filtered popup window.

* **Quality Assurance & Testing**:
    * Added tests for `HelpWindow` using equivalence partitioning on `isCloseKey`, covering close keys (Q, ESCAPE), modifier keys, function keys, digit keys, and navigation keys; also tested null constructor input and `CommandUsage` string content.
    * Added tests for `HelpCommand` covering the no-argument (full window) mode, all known command arguments (inline mode), unknown command argument (exception), and whitespace-trimming behaviour.
    * Performed consistency fixes across diagrams in the Developer Guide (architecture, model class, parser class, storage class, undo/redo state and class diagrams, tag sequence diagram, logic sequence diagram).

---

## Contributions to the User Guide

### Viewing help : `help`

Shows help for TripLog commands.

Format: `help [COMMAND]`

- Without arguments, `help` opens a help window showing syntax for all commands.
- With a command name, `help COMMAND` displays the usage for that specific command inline in the result display (no window opens).

Examples:
- `help` — opens the full help window.
- `help add` — shows the usage for the `add` command inline.
- `help delete` — shows the usage for the `delete` command inline.

Notes:
- The help window can also be opened by pressing **F1** or using the Help menu.
- The help window can be closed by clicking the 'x' button, or by pressing **Q** or **ESCAPE** while the window is focused.
- The help window is resizable — drag any edge or corner to adjust its size.

---

## Contributions to the Developer Guide

### Help Command

#### Implementation

The `help` command supports two modes:

1. **Full help window** — `help` (no argument) opens a separate `HelpWindow` popup displaying syntax for all commands.
2. **Inline help** — `help COMMAND` (e.g., `help add`) displays the usage string for a specific command directly in the result display, without opening a window.

The mode is determined in `HelpCommand#execute()`:

* If no argument is given, a `CommandResult` with `showHelp = true` is returned. `MainWindow` detects this flag and calls `handleHelp()` to show the `HelpWindow`.
* If an argument is given, `getUsageForCommand(argument)` returns the matching usage string, and a regular `CommandResult` is returned. The text is displayed inline in the `ResultDisplay`.

The usage strings (e.g., `ADD_USAGE`, `DELETE_USAGE`) are defined as constants in `CommandUsage` and reused by both `HelpCommand` and `HelpWindow` to keep the content consistent between inline help and the popup window.

The `HelpWindow` is resizable. The `ScrollPane` (which is the scene root) grows to fill the window as the user resizes it, showing a vertical scrollbar only when the content exceeds the window height. A minimum size is enforced on the `Stage` to prevent the window from being dragged to an unusably small size.

#### Design considerations

**Aspect: Where to display per-command help**

* **Alternative 1 (current choice):** Display inline in the existing `ResultDisplay`.
    * Pros: No extra window; fast to access; keyboard-friendly.
    * Cons: Long usage text may get truncated if the result area is small.

* **Alternative 2:** Always open a `HelpWindow`, filtered to the requested command.
    * Pros: Consistent UI; more space for content.
    * Cons: More disruptive; requires an extra window management step.

---

## Contributions to Team Tasks

* **Diagram Maintenance**: Updated all major Developer Guide diagrams (architecture, model class, parser class, storage class, undo/redo state and class diagrams, tag sequence diagram, logic sequence diagram) to accurately reflect the current TripLog implementation.
* **Consistency Fixes**: Reviewed and corrected terminology and formatting across multiple User Guide and Developer Guide sections to ensure a uniform style throughout the documentation.
* **PR Reviews**: Reviewed pull requests from teammates, providing feedback on code quality, and adherence to project conventions.

---

## Review of Alpha Bugs Reported

| ID | Title | Severity |
|---|---|---|
| #142 | Delete preview prints "null" for trips without dates | Medium |
| #143 | Near-duplicate tags on same trip (case-insensitive duplicates allowed) | Medium |
| #144 | Edit command silently accepts duplicate prefixes and uses last value | Low |
| #145 | `help clear` and `help exit` return "Unknown Command" on CLI | Medium |
| #204 | Help window omits `help` command entry; `help help` uses inconsistent format | Low |
