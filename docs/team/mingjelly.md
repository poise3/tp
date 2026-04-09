# Yap Ming Yang - Project Portfolio for TripLog

## Project Overview
**TripLog** is a CLI-centric desktop application designed for travelers who prefer managing their itineraries via a terminal interface. It allows users to track destinations and trip schedules, providing a streamlined, keyboard-first alternative to traditional GUI-based travel planners.

### Summary of Contributions

* **Code Contribution**: [Link to Repo Analyzer](https://nus-cs2103-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=mingjelly&tabRepo=AY2526S2-CS2103-F13-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
* **Feature Enhanced: Added Tag Command**
    * **What it does**: Added the `tag` command as a separate command for the tagging of keywords such as `scenery` `family friendly`.
    * **Justification**: The `add` and `edit` commands are complicated with various fields. Having a separate `tag` command helps improve the ease of tagging.
    * **Highlights**: Implemented parsing logic in `TagCommandParser` to parse arguments.

*Feature Enhanced: Implemented duplicate checks**
* **What it does**: Tags with the same `name` are now considered duplicates.
* **Justification**: Multiple tags with the same name may be added to the same trip, resulting in clutter.
* **Highlights**:
  * Implemented duplicate tag checking logic within the TagCommand class.
  * Refactored equals() and hashCode() methods to ensure getTags().contains() works correctly regardless of capital letters.

* **Feature Enhanced: UI Main Window Overhaul**
    * **What it does**: Designed and modified the main UI window to suit the requirements of TripLog.
    * **Justification**: Main Window design is essential to capturing the essence of the TripLog add, promoting visual appeal and intuitiveness of TripLog.
    * **Highlights**: 
      * Designed a preliminary main window interface using Canva.
      * Refactored MainWindow.css and MainWindow.fxml to support the new main window interface.
      * Added menu bubbles in MainWindow.css to support the preferred file and help button bubble layout.

* **Documentation Contributions**:
    * **User Guide**: 
      * Authored the implementation and duplicate checking logic under "Tagging a trip".
      * Added appropriate images for FindCommand.
    * **Developer Guide**: Added the implementation details for the Tag command, including a sequence diagram illustrating the interaction between the `Logic` and `Model` components during a tag operation.

* **Quality Assurance & Testing**:
    * **Bug Reporting**: Actively participated in the internal alpha testing phase, identifying and documenting a variety of technical and UI/UX issues to improve application robustness.
    * **Test Implementation**: Developed and maintained a suite of unit and integration tests to ensure the reliability of new features and prevent regressions in existing logic.
    * **Validation**: Performed thorough verification of reported bugs and peer-contributed features to maintain high quality-control standards across the project.
---

## Contributions to the User Guide

### Tagging a trip : `tag`

Tags an existing trip in the TripLog with the given keyword.

Format: `tag INDEX TAG`

* Tags the trip with the keyword `TAG` at the specified `INDEX`. The index refers to the index number shown in the displayed trip list. The index **must be a positive integer** 1, 2, 3, …​
* Tags must be alphanumeric (A-Z, 0-9).
* Duplicate tags will not be added.
* Duplicate tags are case-insensitive. e.g. `Hotel` and `HOTEL` are considered duplicates.

Examples:
* `tag 1 scenic beauty` Tags the 1st trip with `scenic beauty`.
* `tag 2 hotel` Tags the 2nd trip with `hotel`.

---

## Contributions to the Developer Guide

### Tagging Trips: Tag Command

The `tag` command allows users to add a single tag to trips for easier organisation and filtering. It only supports tagging by index.

The parsing of the command is handled by `TagCommandParser`, which parses the required arguments and constructs the corresponding `TagCommand`.

The following sequence diagram illustrates the process of tagging a trip:

<puml src="diagrams/TagSequenceDiagram.puml" alt="Tag Command Sequence Diagram" />

### Tagging a trip

1. Tagging a trip using index
    1. Prerequisites: List all trips using the `list` command. Multiple trips in the list.

    2. Test case: `tag 1 scenic beauty`
       Expected: First trip is tagged with `scenic beauty`. Details of the tagged trip shown in the status message.

2. Duplicate tag (case insensitive)
    1. Prerequisites: A trip named "Hotel California" with tag "hotel" already exists.

    2. Test case: `tag 1 HOTEL`
       Expected: Error message indicating duplicate tag. No tag is added.

## Contributions to Team Tasks

* **Bug Triaging**: Actively participated in reviewing and triaging issues reported during the Alpha phase.
* **PR Reviews**: Reviewed peer PRs to ensure that their implementation is bug-free and that code is up to standard.

---

## Review of Alpha Bugs Reported
| ID   | Title                                                          | Severity |
|------|----------------------------------------------------------------|----------|
| #169 | Trip examples' tag inconsistent with the trip                  | Low      |
| #172 | User Guide details not updated for Find Command for TripLog    | Low      |
| #171 | Example code given in User Guide does not work for add command | Medium   |
| #168 | Help Window extends beyond screen                              | Medium   |
| #173 | User Guide image not updated for Help Command                  | Low      |
