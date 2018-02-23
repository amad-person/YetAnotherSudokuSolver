import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class SudokuSolver {
    private static SudokuBoard sudokuBoard = new SudokuBoard();

    private static LinkedHashMap<String, String> gridValues;

    public static void main(String[] args) {
        gridValues = parseUserInput();
        SudokuValues solution = solveGrid();
        displayResult(solution);
    }

    // SUDOKU SOLVER LOGIC

    private static LinkedHashMap<String, String> getGridValues(ArrayList<String> gridChars) {
        gridValues = new LinkedHashMap<>();

        for (int i = 0; i < gridChars.size(); i++) {
            gridValues.put(sudokuBoard.getSquares().get(i), gridChars.get(i));
        }

        return gridValues;
    }

    private static SudokuValues parseGrid() {
        SudokuValues sudokuValues = sudokuBoard.getSudokuValues();

        for (Map.Entry<String, String> gridValue : gridValues.entrySet()) {
            String square = gridValue.getKey();
            String cellValue = gridValue.getValue();
            if (sudokuBoard.getDigits().contains(cellValue)
                    && assignValue(sudokuValues, square, cellValue).getShouldDiscardValues()) {
                return SudokuUtility.contradictionFound(sudokuValues);
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
                return SudokuUtility.contradictionFound(sudokuValues);
            }
        }

        return sudokuValues;
    }

    private static SudokuValues eliminateValue(SudokuValues sudokuValues, String square, String cellValue) {
        if (!(sudokuValues.getValues().get(square).contains(cellValue))) {
            return sudokuValues;
        }

        sudokuValues.getValues()
                .put(square, sudokuValues.getValues().get(square).replace(cellValue, "")
                );

        if(sudokuValues.getValues().get(square).length() == 0)  {
            return SudokuUtility.contradictionFound(sudokuValues);
        } else if (sudokuValues.getValues().get(square).length() == 1) {
            String valToRemove = sudokuValues.getValues().get(square);
            for (String peer : sudokuBoard.getPeers().get(square)) {
                if(eliminateValue(sudokuValues, peer, valToRemove).getShouldDiscardValues()) {
                    return SudokuUtility.contradictionFound(sudokuValues);
                }
            }
        }

        for (ArrayList<String> unit : sudokuBoard.getUnits().get(cellValue)) {
            ArrayList<String> cellPlaces = new ArrayList<>();

            for (String otherSquare : unit) {
                if(sudokuValues.getValues().get(otherSquare).contains(cellValue)) {
                    cellPlaces.add(otherSquare);
                }
            }

            if(cellPlaces.isEmpty()) {
                return SudokuUtility.contradictionFound(sudokuValues);
            } else if (cellPlaces.size() == 1) {
                if(assignValue(sudokuValues, cellPlaces.get(0), cellValue).getShouldDiscardValues()) {
                    return SudokuUtility.contradictionFound(sudokuValues);
                }
            }
        }

        return sudokuValues;
    }

    private static SudokuValues backtrackingSearch(SudokuValues sudokuValues) {
        if (sudokuValues.getShouldDiscardValues()) {
            return sudokuValues;
        }

        if (hasBeenSolved(sudokuValues))
            return sudokuValues;


        String nextEvalValues = sudokuValues.getValues().get(getMinSquare(sudokuValues));

        for (int i = 0; i < nextEvalValues.length(); i++) {
            String cellValue = String.valueOf(nextEvalValues.charAt(i));
            SudokuValues newValues = backtrackingSearch(
                    assignValue(sudokuBoard.getDeepCopySudokuValues(sudokuValues), getMinSquare(sudokuValues), cellValue)
            );

            if (!(newValues.getShouldDiscardValues())) {
                return newValues;
            }
        }

        return SudokuUtility.contradictionFound(sudokuValues);
    }

    // UTILITY METHODS

    private static String getMinSquare(SudokuValues sudokuValues) {
        String minSquare = "";
        int minNumVals = 100;
        for (String square : sudokuBoard.getSquares()) {
            if ((sudokuValues.getValues().get(square).length() > 1)
                    && (sudokuValues.getValues().get(square).length() < minNumVals)) {
                minSquare = square;
                minNumVals = sudokuValues.getValues().get(square).length();
            }
        }

        return minSquare;
    }

    private static boolean hasBeenSolved(SudokuValues sudokuValues) {
        int solvedCounter = 0;

        for (String square : sudokuBoard.getSquares()) {
            solvedCounter += sudokuValues.getValues().get(square).length();
        }

        return (solvedCounter == 81);
    }

    // UI

    private static LinkedHashMap<String, String> parseUserInput() {
        ArrayList<String> gridChars = getGridCharsFromUser();

        return getGridValues(gridChars);
    }

    private static ArrayList<String> getGridCharsFromUser() {
        Scanner userInputScanner = new Scanner(System.in);
        System.out.println("Enter grid and press enter twice:");

        ArrayList<String> gridChars = new ArrayList<>();
        StringBuilder input = new StringBuilder();
        String line;

        while (userInputScanner.hasNextLine()) {
            line = userInputScanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line);
        }

        String gridCharsString = input.toString();
        for (int i = 0; i < gridCharsString.length(); i++) {
            char ch = gridCharsString.charAt(i);
            if (Character.isDigit(ch) || ch == '.') {
                gridChars.add(String.valueOf(ch));
            }
        }

        return gridChars;
    }


    private static void displayResult(SudokuValues sudokuValues) {
        if(!(sudokuValues.getShouldDiscardValues())) {
            System.out.println("Solution: ");
            SudokuUtility.displayBoard(sudokuBoard.getSquares(), sudokuValues.getValues());
        } else {
            System.out.println("Couldn't find a solution.");
        }
    }
}
