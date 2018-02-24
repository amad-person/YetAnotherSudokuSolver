import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SudokuSolver {
    private static SudokuBoard sudokuBoard = new SudokuBoard();

    private static LinkedHashMap<String, String> gridValues = new LinkedHashMap<>();

    public static void main(String[] args) {
        gridValues = parseUserInput();
        SudokuValues solution = solveGrid();
        displayResult(solution);
    }

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

    private static SudokuValues solveGrid() {
        return backtrackingSearch(parseGrid());
    }

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

    private static SudokuValues eliminateValue(SudokuValues sudokuValues, String square, String cellValue) {
        if (!(sudokuValues.getValues().get(square).contains(cellValue))) {
            return sudokuValues;
        }

        sudokuValues.getValues().put(square, sudokuValues.getValues().get(square).replace(cellValue, ""));

        if(sudokuValues.getValues().get(square).length() == 0)  {
            return SudokuSolverUtility.contradictionFound(sudokuValues);
        } else if (sudokuValues.getValues().get(square).length() == 1) {
            String valToRemove = sudokuValues.getValues().get(square);

            for (String peer : sudokuBoard.getPeers().get(square)) {
                if(eliminateValue(sudokuValues, peer, valToRemove).getShouldDiscardValues()) {
                    return SudokuSolverUtility.contradictionFound(sudokuValues);
                }
            }
        }

        for (ArrayList<String> unit : sudokuBoard.getUnits().get(square)) {
            ArrayList<String> cellPlaces = new ArrayList<>();

            for (String otherSquare : unit) {
                if(sudokuValues.getValues().get(otherSquare).contains(cellValue)) {
                    cellPlaces.add(otherSquare);
                }
            }

            if(cellPlaces.isEmpty()) {
                return SudokuSolverUtility.contradictionFound(sudokuValues);
            } else if (cellPlaces.size() == 1) {
                if(assignValue(sudokuValues, cellPlaces.get(0), cellValue).getShouldDiscardValues()) {
                    return SudokuSolverUtility.contradictionFound(sudokuValues);
                }
            }
        }

        return sudokuValues;
    }

    private static SudokuValues backtrackingSearch(SudokuValues sudokuValues) {
        if (sudokuValues.getShouldDiscardValues()) {
            return SudokuSolverUtility.contradictionFound(sudokuValues);
        }

        if (SudokuSolverUtility.hasBeenSolved(sudokuBoard)) {
            return sudokuValues;
        }

        String nextEvalValues = sudokuValues.getValues().get(SudokuSolverUtility.getMinSquare(sudokuBoard));

        for (int i = 0; i < nextEvalValues.length(); i++) {
            String cellValue = String.valueOf(nextEvalValues.charAt(i));
            SudokuValues newValues = backtrackingSearch(
                    assignValue(
                            sudokuBoard.getDeepCopySudokuValues(sudokuValues),
                            SudokuSolverUtility.getMinSquare(sudokuBoard),
                            cellValue
                    )
            );

            if (!(newValues.getShouldDiscardValues())) {
                return newValues;
            }
        }

        return SudokuSolverUtility.contradictionFound(sudokuValues);
    }

    private static LinkedHashMap<String, String> parseUserInput() {
        ArrayList<String> gridChars = SudokuUiUtility.getGridCharsFromUser();

        return SudokuUiUtility.getGridValues(sudokuBoard, gridValues, gridChars);
    }

    private static void displayResult(SudokuValues sudokuValues) {
        if(!(sudokuValues.getShouldDiscardValues())) {
            System.out.println("Solution: ");
            SudokuUiUtility.displayBoard(sudokuBoard.getSquares(), sudokuValues.getValues());
        } else {
            System.out.println("Couldn't find a solution.");
        }
    }
}
