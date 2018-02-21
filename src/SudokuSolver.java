import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class SudokuSolver {
    public static void main(String[] args) {
        String digits = "123456789";
        String rows = "ABCDEFGHI";
        String cols = digits;

        ArrayList<String> squares = crossProduct(rows, cols);
        HashMap<String, ArrayList<ArrayList<String>>> units = getUnits(rows, cols, squares);
        HashMap<String, LinkedHashSet<String>> peers = getPeers(squares, units);
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

    private static ArrayList<ArrayList<String>> getUnitList(String rows, String cols) {
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

    private static HashMap<String, ArrayList<ArrayList<String>>> getUnits(String rows, String cols, ArrayList<String> squares) {
        HashMap<String, ArrayList<ArrayList<String>>> unitsHashMap = new HashMap<>();

        ArrayList<ArrayList<String>> unitList = getUnitList(rows, cols);

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

    private static HashMap<String, LinkedHashSet<String>> getPeers(ArrayList<String> squares, HashMap<String, ArrayList<ArrayList<String>>> units) {
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
}
