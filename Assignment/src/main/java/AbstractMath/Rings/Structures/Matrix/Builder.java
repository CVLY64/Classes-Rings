package AbstractMath.Rings.Structures.Matrix;

import AbstractMath.Rings.RingElement;
import java.util.ArrayList;
import java.util.List;

public class Builder<T extends RingElement<T>> {

    private final int rows;
    private final int columns;
    private final List<List<T>> values;

    public Builder(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException(ErrorStrings.NONPOSITIVE_DIMENSIONS.getMessage());
        }
        if (rows != columns) {
            throw new IllegalArgumentException(ErrorStrings.NOT_SQUARE.getMessage());
        }
        this.rows = rows;
        this.columns = columns;
        this.values = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                row.add(null);
            }
            values.add(row);
        }
    }

    public void setValue(int row, int column, T value) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(ErrorStrings.INDEX_OUT_OF_BOUNDS.getMessage());
        }
        if (value == null) {
            throw new NullPointerException(ErrorStrings.NULL_ENTRY.getMessage());
        }
        values.get(row).set(column, value);
    }

    public Matrix<T> build() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (values.get(i).get(j) == null) {
                    throw new IllegalStateException(ErrorStrings.INCOMPLETE.getMessage());
                }
            }
        }
        return new Matrix<>(rows, columns, values);
    }
}