import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class SudokuBoard {
    private final String digits = "123456789";
    private final String rows = "ABCDEFGHI";
    private final String cols = digits;

    private ArrayList<String> squares;
    private LinkedHashMap<String, ArrayList<ArrayList<String>>> units;
    private LinkedHashMap<String, LinkedHashSet<String>> peers;

    private SudokuValues sudokuValues;

    public SudokuBoard() {
        squares = new ArrayList<>();
        units = new LinkedHashMap<>();
        peers = new LinkedHashMap<>();

        setSquares();
        setUnits();
        setPeers();
        setSudokuValues();
    }

    public String getDigits() {
        return digits;
    }

    public String getRows() {
        return rows;
    }

    public String getCols() {
        return cols;
    }

    public ArrayList<String> getSquares() {
        return squares;
    }

    private void setSquares() {
        squares = SudokuUtility.crossProduct(rows, cols);
    }

    public LinkedHashMap<String, ArrayList<ArrayList<String>>> getUnits() {
        return units;
    }

    private void setUnits() {
        ArrayList<ArrayList<String>> unitList = processUnitList();
        for (String square : squares) {
            ArrayList<ArrayList<String>> currSquareUnits = new ArrayList<>();
            for (ArrayList<String> unit : unitList) {
                if (unit.contains(square)) {
                    currSquareUnits.add(unit);
                }
            }
            units.put(square, currSquareUnits);
        }
    }

    public LinkedHashMap<String, LinkedHashSet<String>> getPeers() {
        return peers;
    }

    private void setPeers() {
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
            peers.put(square, currSquarePeers);
        }
    }

    public SudokuValues getSudokuValues() {
        return sudokuValues;
    }

    private void setSudokuValues() {
        boolean shouldDiscardValues = false;
        LinkedHashMap<String, String> values = new LinkedHashMap<>();

        for (String square : squares) {
            values.put(square, digits);
        }

        this.sudokuValues = new SudokuValues(shouldDiscardValues, values);
    }

    public SudokuValues getDeepCopySudokuValues(SudokuValues sudokuValues) {
        return new SudokuValues(sudokuValues.getShouldDiscardValues(), sudokuValues.getDeepCopyValues());
    }

    // UTILITY METHODS

    private ArrayList<ArrayList<String>> processUnitList() {
        ArrayList<ArrayList<String>> unitList = new ArrayList<>();

        // column units
        for (int c = 0; c < cols.length(); c++) {
            unitList.add(SudokuUtility.crossProduct(rows, String.valueOf(cols.charAt(c))));
        }

        // row units
        for (int r = 0; r < rows.length(); r++) {
            unitList.add(SudokuUtility.crossProduct(String.valueOf((rows.charAt(r))), cols));
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
                unitList.add(SudokuUtility.crossProduct(rb, cb));
            }
        }

        return unitList;
    }
}
