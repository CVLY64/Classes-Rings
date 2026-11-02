package AbstractMath.Rings.Structures.Polynomial;

import AbstractMath.Rings.RingElement;
import java.util.ArrayList;
import java.util.List;

public class Builder<T extends RingElement<T>> {

    private final int degree;
    private final List<T> coefficients;

    public Builder(int degree) {
        if (degree < 0 || degree == Integer.MAX_VALUE) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_DEGREE.getMessage());
        }
        this.degree = degree;
        this.coefficients = new ArrayList<>(degree + 1);
        for (int i = 0; i <= degree; i++) {
            coefficients.add(null);
        }
    }

    public void setValue(int degree, T value) {
        if (degree < 0) {
            throw new IllegalArgumentException(ErrorStrings.NEGATIVE_TERM_DEGREE.getMessage());
        }
        if (degree > this.degree) {
            throw new IndexOutOfBoundsException(ErrorStrings.TERM_OUT_OF_BOUNDS.getMessage());
        }
        if (value == null) {
            throw new NullPointerException(ErrorStrings.NULL_COEFFICIENT.getMessage());
        }
        if (degree == this.degree && degree > 0 && value.equals(value.zero())) {
            throw new IllegalArgumentException(ErrorStrings.LEADING_ZERO.getMessage());
        }
        coefficients.set(degree, value);
    }

    public Polynomial<T> build() {
        for (T c : coefficients) {
            if (c == null) {
                throw new IllegalStateException(ErrorStrings.INCOMPLETE.getMessage());
            }
        }
        return new Polynomial<>(degree, coefficients);
    }
}