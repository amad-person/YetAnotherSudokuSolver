import java.util.ArrayList;

/**
 * Provides utility methods for the sudoku solver.
 */
public final class SudokuSolverUtility {
    private static final int NUM_SQUARES = 81;
    private static final int MAX_DOMAIN_VALUE = 100;

    /**
     * Returns cross product of the two strings passed as parameters.
     */
    public static ArrayList<String> crossProduct(String a, String b) {
        ArrayList<String> crossProduct = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                crossProduct.add(String.valueOf(a.charAt(i)) + String.valueOf(b.charAt(j)));
            }
        }

        return crossProduct;
    }

    /**
     * Sets shouldDiscardValues flag to be true if a conflict is found in the current sudokuValues assignment.
     *
     * @return updated sudokuValues that will be discarded by the sudoku solver.
     */
    public static SudokuValues contradictionFound(SudokuValues sudokuValues) {
        sudokuValues.setShouldDiscardValues(true);
        return sudokuValues;
    }

    /**
     * Implements Minimum-Remaining-Values heuristic.
     *
     * @return  square with the minimum number of values left in its domain.
     */
    public static String getMinSquare(SudokuBoard sudokuBoard) {
        String minSquare = "";
        int minNumVals = MAX_DOMAIN_VALUE;

        for (String square : sudokuBoard.getSquares()) {
            if ((sudokuBoard.getSudokuValues().getValues().get(square).length() > 1)
                    && (sudokuBoard.getSudokuValues().getValues().get(square).length() < minNumVals)) {
                minSquare = square;
                minNumVals = sudokuBoard.getSudokuValues().getValues().get(square).length();
            }
        }

        return minSquare;
    }

    /**
     * Checks if sudoku board has been solved.
     */
    public static boolean hasBeenSolved(SudokuBoard sudokuBoard) {
        int solvedCounter = 0;

        for (String square : sudokuBoard.getSquares()) {
            solvedCounter += sudokuBoard.getSudokuValues().getValues().get(square).length();
        }

        // sudokuBoard has been solved if each square has one value in its domain
        return (solvedCounter == NUM_SQUARES);
    }
}
