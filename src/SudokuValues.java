import java.util.LinkedHashMap;
import java.util.Map;

public class SudokuValues {
    private boolean shouldDiscardValues;
    private LinkedHashMap<String, String> values;

    public SudokuValues() {
        shouldDiscardValues = false;
        values = new LinkedHashMap<>();
    }

    public SudokuValues(boolean shouldDiscardValues, LinkedHashMap<String, String> values) {
        setShouldDiscardValues(shouldDiscardValues);
        setValues(values);
    }

    public boolean getShouldDiscardValues() {
        return shouldDiscardValues;
    }

    public void setShouldDiscardValues(boolean shouldDiscardValues) {
        this.shouldDiscardValues = shouldDiscardValues;
    }

    public LinkedHashMap<String, String> getValues() {
        return values;
    }

    private void setValues(LinkedHashMap<String, String> values) {
        this.values = values;
    }

    public LinkedHashMap<String, String> getDeepCopyValues() {
        LinkedHashMap<String, String> copiedValues = new LinkedHashMap<>();

        for (Map.Entry<String, String> currMapEntry : this.values.entrySet()) {
            copiedValues.put(currMapEntry.getKey(), currMapEntry.getValue());
        }

        return copiedValues;
    }
}
