import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents values of all squares in the current state of sudoku board.
 */
public class SudokuValues {
    private boolean shouldDiscardValues;
    private LinkedHashMap<String, String> values;

    public SudokuValues() {
        shouldDiscardValues = false;
        values = new LinkedHashMap<>();
    }

    /**
     * Constructs a new sudoku values object using parameters passed to it.
     */
    public SudokuValues(boolean shouldDiscardValues, LinkedHashMap<String, String> values) {
        setShouldDiscardValues(shouldDiscardValues);
        setValues(values);
    }

    public boolean getShouldDiscardValues() {
        return shouldDiscardValues;
    }

    /**
     * Sets shouldDiscardValues flag to the boolean passed as a parameter.
     */
    public void setShouldDiscardValues(boolean shouldDiscardValues) {
        this.shouldDiscardValues = shouldDiscardValues;
    }

    public LinkedHashMap<String, String> getValues() {
        return values;
    }

    /**
     * Sets values to be the map passed as a parameter.
     */
    private void setValues(LinkedHashMap<String, String> values) {
        this.values = values;
    }

    /**
     * Returns a deep copy of the values map.
     */
    public LinkedHashMap<String, String> getDeepCopyValues() {
        LinkedHashMap<String, String> copiedValues = new LinkedHashMap<>();

        for (Map.Entry<String, String> currMapEntry : this.values.entrySet()) {
            copiedValues.put(currMapEntry.getKey(), currMapEntry.getValue());
        }

        return copiedValues;
    }
}
