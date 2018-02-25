import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains logic for solving sudoku problems.
 */
public class SudokuSolver {
    private static SudokuBoard sudokuBoard = new SudokuBoard();

    private static LinkedHashMap<String, String> gridValues = new LinkedHashMap<>();

    public static void main(String[] args) {
        gridValues = parseUserInput();
        SudokuValues solution = solveGrid();
        displayResult(solution);
    }

    private static SudokuValues solveGrid() {
        return backtrackingSearch(parseGrid());
    }

    /**
     * Parses grid to assign values to each square on the board.
     */
    private static SudokuValues parseGrid() {
        SudokuValues sudokuValues = sudokuBoard.getSudokuValues();

        for (Map.Entry<String, String> gridValue : gridValues.entrySet()) {
            String square = gridValue.getKey();
            String cellValue = gridValue.getValue();

            if (sudokuBoard.getDigits().contains(cellValue)
                    && assignValue(sudokuValues, square, cellValue).getShouldDiscardValues()) {
                return SudokuSolverUtility.contradictionFound(sudokuValues);
            }
        }

        return sudokuBoard.getSudokuValues();
    }

    /**
     * Assigns digit to current square if no contradiction has been found.
     *
     * @param sudokuValues current mapping of values to squares.
     * @param square current square that needs to be assigned a value.
     * @param cellValue digit to be assigned to the square.
     */
    private static SudokuValues assignValue(SudokuValues sudokuValues, String square, String cellValue) {
        String otherValues = sudokuValues.getValues().get(square).replace(cellValue, "");

        for (int i = 0; i < otherValues.length(); i++) {
            String otherCellValue = String.valueOf(otherValues.charAt(i));

            if (eliminateValue(sudokuValues, square, otherCellValue).getShouldDiscardValues()) {
                return SudokuSolverUtility.contradictionFound(sudokuValues);
            }
        }

        return sudokuValues;
    }

    /**
     * Eliminates digit assigned to the current square from domains of squares in the same unit
     * if no contradiction has been found.
     */
    private static SudokuValues eliminateValue(SudokuValues sudokuValues, String square, String cellValue) {
        // current digit has been eliminated before
        if (!(sudokuValues.getValues().get(square).contains(cellValue))) {
            return sudokuValues;
        }

        // remove current digit from domain
        sudokuValues.getValues().put(square, sudokuValues.getValues().get(square).replace(cellValue, ""));

        if (sudokuValues.getValues().get(square).length() == 0)  {
            // if domain of current square has no more elements, it is an invalid assignment
            return SudokuSolverUtility.contradictionFound(sudokuValues);
        } else if (sudokuValues.getValues().get(square).length() == 1) {
            // if domain of current square has only one element, assign the value to that square provided that
            // eliminating the digit from domains of the square's peers does not cause a contradiction
            String valToRemove = sudokuValues.getValues().get(square);

            for (String peer : sudokuBoard.getPeers().get(square)) {
                if (eliminateValue(sudokuValues, peer, valToRemove).getShouldDiscardValues()) {
                    return SudokuSolverUtility.contradictionFound(sudokuValues);
                }
            }
        }

        for (ArrayList<String> unit : sudokuBoard.getUnits().get(square)) {
            ArrayList<String> cellPlaces = new ArrayList<>();

            for (String otherSquare : unit) {
                if (sudokuValues.getValues().get(otherSquare).contains(cellValue)) {
                    cellPlaces.add(otherSquare);
                }
            }

            if (cellPlaces.isEmpty()) {
                // if digit has no possible square to be in, it is an invalid assignment
                return SudokuSolverUtility.contradictionFound(sudokuValues);
            } else if (cellPlaces.size() == 1) {
                // if digit has only one possible square to be in out of all the squares in that unit,
                // assign it to that square provided that no contradiction will be found
                if (assignValue(sudokuValues, cellPlaces.get(0), cellValue).getShouldDiscardValues()) {
                    return SudokuSolverUtility.contradictionFound(sudokuValues);
                }
            }
        }

        return sudokuValues;
    }

    /**
     * Performs backtracking search to find out a valid assignment of digits to all squares on the board.
     */
    private static SudokuValues backtrackingSearch(SudokuValues sudokuValues) {
        if (sudokuValues.getShouldDiscardValues()) {
            return SudokuSolverUtility.contradictionFound(sudokuValues);
        }

        // return solution if a consistent and complete assignment has been found
        if (SudokuSolverUtility.hasBeenSolved(sudokuBoard)) {
            return sudokuValues;
        }

        // get the next square to be checked based on the Minimum-Remaining-Values heuristic
        String nextEvalValues = sudokuValues.getValues().get(SudokuSolverUtility.getMinSquare(sudokuBoard));

        for (int i = 0; i < nextEvalValues.length(); i++) {
            String cellValue = String.valueOf(nextEvalValues.charAt(i));
            // recursively call backtracking search with a deep copy of values updated with assignment
            // that needs to be checked
            SudokuValues newValues = backtrackingSearch(
                    assignValue(
                            sudokuBoard.getDeepCopySudokuValues(sudokuValues),
                            SudokuSolverUtility.getMinSquare(sudokuBoard),
                            cellValue
                    )
            );

            // accept assignment if no contradiction has been found
            if (!(newValues.getShouldDiscardValues())) {
                return newValues;
            }
        }

        return SudokuSolverUtility.contradictionFound(sudokuValues);
    }

    /**
     * Parses and returns user input of the sudoku problem to be solved.
     */
    private static LinkedHashMap<String, String> parseUserInput() {
        ArrayList<String> gridChars = SudokuUiUtility.getGridCharsFromUser();

        return SudokuUiUtility.getGridValues(sudokuBoard, gridValues, gridChars);
    }

    /**
     * Displays result of running the solver algorithm.
     */
    private static void displayResult(SudokuValues sudokuValues) {
        if (!(sudokuValues.getShouldDiscardValues())) {
            System.out.println("Solution:\n=====================");
            SudokuUiUtility.displayBoard(sudokuBoard);
            System.out.println("=====================");
        } else {
            System.out.println("Couldn't find a solution.");
        }
    }
}
