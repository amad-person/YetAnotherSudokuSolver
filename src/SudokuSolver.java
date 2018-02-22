import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;

public class SudokuSolver {
    private static final String digits = "123456789";
    private static final String rows = "ABCDEFGHI";
    private static final String cols = digits;

    private static ArrayList<String> squares;
    private static HashMap<String, ArrayList<ArrayList<String>>> units;
    private static HashMap<String, LinkedHashSet<String>> peers;

    private static HashMap<String, String> gridValues;
    private static HashMap<String, String> solution;

    public static void main(String[] args) {
        squares = crossProduct(rows, cols);
        units = getUnits();
        peers = getPeers();

        gridValues = parseUserInput();
        solution = solveGrid();
        displayGrid(solution);
    }

    // SUDOKU SOLVER LOGIC

    private static HashMap<String, String> getGridValues(ArrayList<String> gridChars) {
        HashMap<String, String> gridValues = new HashMap<>();

        for (int i = 0; i < gridChars.size(); i++) {
            gridValues.put(squares.get(i), gridChars.get(i));
        }

        return gridValues;
    }

    private static HashMap<String, String> parseGrid() {
        HashMap<String, String> values = getValues();

        for (Map.Entry<String, String> gridValue : gridValues.entrySet()) {
            String square = gridValue.getKey();
            String cellValue = gridValue.getValue();
            if (digits.contains(cellValue) && assignValue(values, square, cellValue) == null) {
                return null;
            }
        }

        return values;
    }

    private static HashMap<String, String> assignValue(HashMap<String, String> values, String square, String cellValue) {
        String otherValues = values.get(square).replace(cellValue, "");

        for (int i = 0; i < otherValues.length(); i++) {
            String otherCellValue = String.valueOf(otherValues.charAt(i));

            if (eliminateValue(values, square, otherCellValue) == null) {
                return null;
            }
        }

        return values;
    }

    private static HashMap<String, String> eliminateValue(HashMap<String, String> values, String square, String cellValue) {
        if (!(values.get(square).contains(cellValue))) {
            return values;
        }

        values.put(square, values.get(square).replace(cellValue, ""));

        if(values.get(square).length() == 0)  {
            return null;
        } else if (values.get(square).length() == 1) {
            String valToRemove = values.get(square);
            for (String peer : peers.get(square)) {
                if(eliminateValue(values, peer, valToRemove) == null) {
                    return null;
                }
            }
        }

        for (ArrayList<String> unit : units.get(cellValue)) {
            ArrayList<String> cellPlaces = new ArrayList<>();

            for (String otherSquare : unit) {
                if(values.get(otherSquare).contains(cellValue)) {
                    cellPlaces.add(otherSquare);
                }
            }

            if(cellPlaces.isEmpty()) {
                return null;
            } else if (cellPlaces.size() == 1) {
                if(assignValue(values, cellPlaces.get(0), cellValue) == null) {
                    return null;
                }
            }
        }

        return values;
    }

    private static HashMap<String, String> solveGrid() {
        return backtrackingSearch(parseGrid());
    }

    private static HashMap<String, String> backtrackingSearch(HashMap<String, String> values) {
        if (values == null) {
            return null;
        }

        if (hasBeenSolved(values))
            return values;


        String nextEvalValues = values.get(getMinSquare(values));

        for (int i = 0; i < nextEvalValues.length(); i++) {
            String cellValue = String.valueOf(nextEvalValues.charAt(i));
            HashMap<String, String> newValues = backtrackingSearch(assignValue(deepCopyHashMap(values), getMinSquare(values), cellValue));
            if (newValues != null) {
                return newValues;
            }
        }

        return null;
    }

    // UTILITY METHODS

    private static HashMap<String, String> parseUserInput() {
        ArrayList<String> gridChars = getGridCharsFromUser();
        HashMap<String, String> gridValues = getGridValues(gridChars);

        return gridValues;
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

        System.out.println(gridChars.size());
        return gridChars;
    }

    private static ArrayList<String> crossProduct(String a, String b) {
        ArrayList<String> crossProduct = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                crossProduct.add(String.valueOf(a.charAt(i)) + String.valueOf(b.charAt(j)));
            }
        }

        return crossProduct;
    }

    private static HashMap<String, String> getValues() {
        HashMap<String, String> values = new HashMap<>();
        for (String square : squares) {
            values.put(square, digits);
        }

        return values;
    }

    private static HashMap<String, ArrayList<ArrayList<String>>> getUnits() {
        HashMap<String, ArrayList<ArrayList<String>>> unitsHashMap = new HashMap<>();

        ArrayList<ArrayList<String>> unitList = getUnitList();

        for (String square : squares) {
            ArrayList<ArrayList<String>> currSquareUnits = new ArrayList<>();
            for (ArrayList<String> unit : unitList) {
                if (unit.contains(square)) {
                    currSquareUnits.add(unit);
                }
            }
            unitsHashMap.put(square, currSquareUnits);
        }

        return unitsHashMap;
    }

    private static ArrayList<ArrayList<String>> getUnitList() {
        ArrayList<ArrayList<String>> unitList = new ArrayList<>();

        // column units
        for (int c = 0; c < cols.length(); c++) {
            unitList.add(crossProduct(rows, String.valueOf(cols.charAt(c))));
        }

        // row units
        for (int r = 0; r < rows.length(); r++) {
            unitList.add(crossProduct(String.valueOf((rows.charAt(r))), cols));
        }

        // set up for box units
        ArrayList<String> rowBox = new ArrayList<>();
        rowBox.add("ABC");
        rowBox.add("DEF");
        rowBox.add("GHI");

        ArrayList<String> colBox = new ArrayList<>();
        colBox.add("123");
        colBox.add("456");
        colBox.add("789");

        // box units
        for (String rb : rowBox) {
            for (String cb : colBox) {
                unitList.add(crossProduct(rb, cb));
            }
        }

        return unitList;
    }

    private static HashMap<String, LinkedHashSet<String>> getPeers() {
        HashMap<String, LinkedHashSet<String>> peersHashMap = new HashMap<>();

        for (String square : squares) {
            LinkedHashSet<String> currSquarePeers = new LinkedHashSet<>();
            ArrayList<ArrayList<String>> currSquareUnits = units.get(square);
            for (ArrayList<String> unit : currSquareUnits) {
                for (String peer : unit) {
                    if(!(peer.equals(square))) {
                        currSquarePeers.add(peer);
                    }
                }
            }
            peersHashMap.put(square, currSquarePeers);
        }
        return peersHashMap;
    }

    private static String getMinSquare(HashMap<String, String> values) {
        String minSquare = "";
        int minNumVals = 100;
        for (String square : squares) {
            if ((values.get(square).length() > 1) && (values.get(square).length() < minNumVals)) {
                minSquare = square;
                minNumVals = values.get(square).length();
            }
        }

        return minSquare;
    }

    private static boolean hasBeenSolved(HashMap<String, String> values) {
        int solvedCounter = 0;

        for (String square : squares) {
            solvedCounter += values.get(square).length();
        }

        return (solvedCounter == 81);
    }

    private static HashMap<String, String> deepCopyHashMap(HashMap<String, String> currMap) {
        HashMap<String, String> copiedMap = new HashMap<>();

        for (Map.Entry<String, String> currMapEntry : currMap.entrySet()) {
            copiedMap.put(currMapEntry.getKey(), currMapEntry.getValue());
        }

        return copiedMap;
    }

    // UI

    private static void displayGrid(HashMap<String, String> values) {
        if(values != null) {
            System.out.println("Solution: ");
            int index = 0;
            for (String square : squares) {
                System.out.print(values.get(square));
                index++;
                if (index % 9 == 0) {
                    System.out.println();
                }
            }
        } else {
            System.out.println("Couldn't find a solution. " +
                    "Maybe you can try entering \'sudo ku\'? " +
                    "Just kidding, that won't work.");
        }
    }
}
