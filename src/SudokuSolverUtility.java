import java.util.ArrayList;

public final class SudokuSolverUtility {
    public static ArrayList<String> crossProduct(String a, String b) {
        ArrayList<String> crossProduct = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                crossProduct.add(String.valueOf(a.charAt(i)) + String.valueOf(b.charAt(j)));
            }
        }

        return crossProduct;
    }

    public static SudokuValues contradictionFound(SudokuValues sudokuValues) {
        sudokuValues.setShouldDiscardValues(true);
        return sudokuValues;
    }

    public static String getMinSquare(SudokuBoard sudokuBoard) {
        String minSquare = "";
        int minNumVals = 100;
        for (String square : sudokuBoard.getSquares()) {
            if ((sudokuBoard.getSudokuValues().getValues().get(square).length() > 1)
                    && (sudokuBoard.getSudokuValues().getValues().get(square).length() < minNumVals)) {
                minSquare = square;
                minNumVals = sudokuBoard.getSudokuValues().getValues().get(square).length();
            }
        }

        return minSquare;
    }

    public static boolean hasBeenSolved(SudokuBoard sudokuBoard) {
        int solvedCounter = 0;

        for (String square : sudokuBoard.getSquares()) {
            solvedCounter += sudokuBoard.getSudokuValues().getValues().get(square).length();
        }

        return (solvedCounter == 81);
    }
}
