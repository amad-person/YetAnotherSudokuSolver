import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Represents sudoku board used by the solver.
 */
public class SudokuBoard {
    private final String digits = "123456789";
    private final String rows = "ABCDEFGHI";
    private final String cols = digits;

    private ArrayList<String> squares;
    private LinkedHashMap<String, ArrayList<ArrayList<String>>> units;
    private LinkedHashMap<String, LinkedHashSet<String>> peers;

    private SudokuValues sudokuValues;

    /**
     * Constructs the sudoku board object.
     */
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

    /**
     * Sets up list of all possible values of squares.
     */
    private void setSquares() {
        squares = SudokuSolverUtility.crossProduct(rows, cols);
    }

    public LinkedHashMap<String, ArrayList<ArrayList<String>>> getUnits() {
        return units;
    }

    /**
     * Sets up map containing all possible units of each square.
     */
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

    /**
     * Sets up map containing all possible peers of each square.
     */
    private void setPeers() {
        for (String square : squares) {
            LinkedHashSet<String> currSquarePeers = new LinkedHashSet<>();
            ArrayList<ArrayList<String>> currSquareUnits = units.get(square);

            for (ArrayList<String> unit : currSquareUnits) {
                for (String peer : unit) {
                    if (!(peer.equals(square))) {
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

    /**
     * Sets up sudokuValues that represents the current state of the values assigned to each square in the board.
     */
    private void setSudokuValues() {
        boolean shouldDiscardValues = false;
        LinkedHashMap<String, String> values = new LinkedHashMap<>();

        // initially all squares have all digits in their domains
        for (String square : squares) {
            values.put(square, digits);
        }

        this.sudokuValues = new SudokuValues(shouldDiscardValues, values);
    }

    /**
     * Returns deep copy of sudokuValues for each new state introduced by the backtracking search algorithm.
     */
    public SudokuValues getDeepCopySudokuValues(SudokuValues sudokuValues) {
        return new SudokuValues(sudokuValues.getShouldDiscardValues(), sudokuValues.getDeepCopyValues());
    }

    /**
     * Returns list of all possible units in the sudoku board.
     */
    private ArrayList<ArrayList<String>> processUnitList() {
        ArrayList<ArrayList<String>> unitList = new ArrayList<>();

        // column units
        for (int c = 0; c < cols.length(); c++) {
            unitList.add(SudokuSolverUtility.crossProduct(rows, String.valueOf(cols.charAt(c))));
        }

        // row units
        for (int r = 0; r < rows.length(); r++) {
            unitList.add(SudokuSolverUtility.crossProduct(String.valueOf((rows.charAt(r))), cols));
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
                unitList.add(SudokuSolverUtility.crossProduct(rb, cb));
            }
        }

        return unitList;
    }
}
