# Longest Period of Employee Collaboration

This program analyzes the data from a CSV input file and finds the longest period of time that a pair of employees have worked together on a common project.

## Usage Instructions

1. Run the program.
2. Use the user interface to select and upload a CSV file containing employee data.
3. The file path of the selected input file will be displayed. If this is the file you want to upload, click "Yes." If not, click "No" and upload a new file.
4. The program will process the uploaded file and display the results, showing a table consisting of pairs of employees who have worked together for the longest period, the associated project IDs, and the amount of days they have worked together.

## Works for:
* Multiple solutions - if there are more than one pair of employees that have worked together for the longest period of time.
* More than 2 employees who have worked together for the longest period of time: will display emp1 and emp2, emp1 and emp3, emp2 and emp3.
* Date formats: yyyy/mm/dd, yyyy-mm-dd, dd/mm/yyyy, dd-mm-yyyy.
* Non-consistent date formats in the input.

## No error handling:
* Give only CSV files as input.
* Use only the supported date format.
