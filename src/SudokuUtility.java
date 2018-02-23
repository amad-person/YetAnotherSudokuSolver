import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class SudokuUtility {
    public static ArrayList<String> crossProduct(String a, String b) {
        ArrayList<String> crossProduct = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                crossProduct.add(String.valueOf(a.charAt(i)) + String.valueOf(b.charAt(j)));
            }
        }

        return crossProduct;
    }

    public static void displayBoard(ArrayList<String> squares, LinkedHashMap<String, String> values) {
        int index = 0;
        for (String square : squares) {
            System.out.print(values.get(square));
            index++;
            if (index % 9 == 0) {
                System.out.println();
            }
        }
    }

    public static SudokuValues contradictionFound(SudokuValues sudokuValues) {
        sudokuValues.setShouldDiscardValues(true);
        return sudokuValues;
    }
}
