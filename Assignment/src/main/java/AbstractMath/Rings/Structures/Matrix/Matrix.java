package AbstractMath.Rings.Structures.Matrix;

import AbstractMath.Rings.RingElement;
import java.util.ArrayList;
import java.util.List;

public final class Matrix<T extends RingElement<T>> implements RingElement<Matrix<T>> {

    private final int rows;
    private final int columns;
    private final List<List<T>> values;

    public Matrix(int rows, int columns, List<List<T>> defaultValues) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException(ErrorStrings.NONPOSITIVE_DIMENSIONS.getMessage());
        }
        if (rows != columns) {
            throw new IllegalArgumentException(ErrorStrings.NOT_SQUARE.getMessage());
        }
        if (defaultValues == null) {
            throw new NullPointerException(ErrorStrings.NULL_VALUES.getMessage());
        }
        if (defaultValues.size() != rows) {
            throw new IllegalArgumentException(ErrorStrings.ROW_COUNT.getMessage());
        }
        List<List<T>> copy = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = defaultValues.get(i);
            if (row == null) {
                throw new NullPointerException(ErrorStrings.NULL_ROW.getMessage());
            }
            if (row.size() != columns) {
                throw new IllegalArgumentException(ErrorStrings.COLUMN_COUNT.getMessage());
            }
            List<T> rowCopy = new ArrayList<>(columns);
            for (T entry : row) {
                if (entry == null) {
                    throw new NullPointerException(ErrorStrings.NULL_ENTRY.getMessage());
                }
                rowCopy.add(entry);
            }
            copy.add(rowCopy);
        }
        T first = copy.get(0).get(0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!first.hasSameType(copy.get(i).get(j))) {
                    throw new IllegalArgumentException(ErrorStrings.MIXED_ENTRY_TYPES.getMessage());
                }
            }
        }
        this.rows = rows;
        this.columns = columns;
        this.values = copy;
    }

    public static <T extends RingElement<T>> Builder<T> builder(int rows, int columns) {
        return new Builder<>(rows, columns);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public boolean hasSameType(Object other) {
        if (other == null || !(other instanceof Matrix<?>)) {
            return false;
        }
        Matrix<?> o = (Matrix<?>) other;
        T first = values.get(0).get(0);
        Object otherFirst = o.values.get(0).get(0);
        return first.hasSameType(otherFirst);
    }

    private void checkIndex(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(ErrorStrings.INDEX_OUT_OF_BOUNDS.getMessage());
        }
    }

    public T getValue(int row, int column) {
        checkIndex(row, column);
        return values.get(row).get(column);
    }

    private void checkOperand(Matrix<T> other) {
        if (other == null) {
            throw new NullPointerException(ErrorStrings.NULL_OPERAND.getMessage());
        }
        if (this.rows != other.rows || this.columns != other.columns) {
            throw new IllegalArgumentException(ErrorStrings.SIZE_MISMATCH.getMessage());
        }
    }

    @Override
    public Matrix<T> add(Matrix<T> other) {
        checkOperand(other);
        List<List<T>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                row.add(values.get(i).get(j).add(other.values.get(i).get(j)));
            }
            result.add(row);
        }
        return new Matrix<>(rows, columns, result);
    }

    @Override
    public Matrix<T> negate() {
        List<List<T>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                row.add(values.get(i).get(j).negate());
            }
            result.add(row);
        }
        return new Matrix<>(rows, columns, result);
    }

    @Override
    public Matrix<T> multiply(Matrix<T> other) {
        checkOperand(other);
        T zero = values.get(0).get(0).zero();
        List<List<T>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                T sum = zero;
                for (int k = 0; k < columns; k++) {
                    T product = values.get(i).get(k).multiply(other.values.get(k).get(j));
                    sum = sum.add(product);
                }
                row.add(sum);
            }
            result.add(row);
        }
        return new Matrix<>(rows, columns, result);
    }

    @Override
    public Matrix<T> zero() {
        T zero = values.get(0).get(0).zero();
        List<List<T>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                row.add(zero);
            }
            result.add(row);
        }
        return new Matrix<>(rows, columns, result);
    }

    @Override
    public Matrix<T> one() {
        T zero = values.get(0).get(0).zero();
        T one = values.get(0).get(0).one();
        List<List<T>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) {
                row.add(i == j ? one : zero);
            }
            result.add(row);
        }
        return new Matrix<>(rows, columns, result);
    }

    @Override
    public boolean equals(Object other) {
        if (!hasSameType(other)) {
            throw new ClassCastException(ErrorStrings.INCOMPATIBLE_TYPE.getMessage());
        }
        Matrix<?> o = (Matrix<?>) other;
        if (this.rows != o.rows || this.columns != o.columns) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!this.values.get(i).get(j).equals(o.values.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 31 * h + rows;
        h = 31 * h + columns;
        for (List<T> row : values) {
            for (T entry : row) {
                h = 31 * h + entry.hashCode();
            }
        }
        return h;
    }
}