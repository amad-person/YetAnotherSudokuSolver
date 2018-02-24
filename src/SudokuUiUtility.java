import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public final class SudokuUiUtility {
    public static LinkedHashMap<String, String> getGridValues(SudokuBoard sudokuBoard,
                                                              LinkedHashMap<String, String> gridValues,
                                                              ArrayList<String> gridChars) {
        for (int i = 0; i < gridChars.size(); i++) {
            gridValues.put(sudokuBoard.getSquares().get(i), gridChars.get(i));
        }

        return gridValues;
    }

    public static ArrayList<String> getGridCharsFromUser() {
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
    public static void displayBoard(ArrayList<String> squares, LinkedHashMap<String, String> values) {
        int index = 0;
        for (String square : squares) {
            System.out.print(values.get(square) + " ");
            index++;

            if((index % 3 == 0 || index % 6 == 0) && (index % 9 != 0)) {
                System.out.print("| ");
            }

            if (index % 9 == 0) {
                System.out.println();
            }

            if(index % 27 == 0 && index % 81 != 0) {
                System.out.println("---------------------");
            }
        }
    }
}
