package AbstractMath.Rings.Structures.Polynomial;

import AbstractMath.Rings.RingElement;
import java.util.ArrayList;
import java.util.List;

public final class Polynomial<T extends RingElement<T>> implements RingElement<Polynomial<T>> {

    private final ArrayList<T> coefficients;

    public Polynomial(int degree, List<T> coefficients) {
        if (degree < 0 || degree == Integer.MAX_VALUE) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_DEGREE.getMessage());
        }
        if (coefficients == null) {
            throw new NullPointerException(ErrorStrings.NULL_COEFFICIENTS.getMessage());
        }
        if (coefficients.isEmpty()) {
            throw new IllegalArgumentException(ErrorStrings.EMPTY_COEFFICIENTS.getMessage());
        }
        if (coefficients.size() != degree + 1) {
            throw new IllegalArgumentException(ErrorStrings.COEFFICIENT_COUNT.getMessage());
        }
        for (T c : coefficients) {
            if (c == null) {
                throw new NullPointerException(ErrorStrings.NULL_COEFFICIENT.getMessage());
            }
        }
        T first = coefficients.get(0);
        for (T c : coefficients) {
            if (!first.hasSameType(c)) {
                throw new IllegalArgumentException(ErrorStrings.MIXED_COEFFICIENT_TYPES.getMessage());
            }
        }
        this.coefficients = new ArrayList<>(coefficients);
        removeTrailingZeros();
    }

    public Polynomial(List<T> coefficients) {
        if (coefficients == null) {
            throw new NullPointerException(ErrorStrings.NULL_COEFFICIENTS.getMessage());
        }
        this.coefficients = new ArrayList<>(coefficients);
        if (this.coefficients.isEmpty()) {
            throw new IllegalArgumentException(ErrorStrings.EMPTY_COEFFICIENTS.getMessage());
        }
        for (T c : this.coefficients) {
            if (c == null) {
                throw new NullPointerException(ErrorStrings.NULL_COEFFICIENT.getMessage());
            }
        }
        T first = this.coefficients.get(0);
        for (T c : this.coefficients) {
            if (!first.hasSameType(c)) {
                throw new IllegalArgumentException(ErrorStrings.MIXED_COEFFICIENT_TYPES.getMessage());
            }
        }
        removeTrailingZeros();
    }

    private void removeTrailingZeros() {
        while (coefficients.size() > 1) {
            T last = coefficients.get(coefficients.size() - 1);
            if (last.equals(last.zero())) {
                coefficients.remove(coefficients.size() - 1);
            } else {
                break;
            }
        }
    }

    public static <T extends RingElement<T>> Builder<T> builder(int degree) {
        return new Builder<>(degree);
    }

    public int degree() {
        return coefficients.size() - 1;
    }

    @Override
    public boolean hasSameType(Object other) {
        if (other == null || !(other instanceof Polynomial<?>)) {
            return false;
        }
        Polynomial<?> o = (Polynomial<?>) other;
        T first = coefficients.get(0);
        Object otherFirst = o.coefficients.get(0);
        return first.hasSameType(otherFirst);
    }

    public T coefficient(int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException(ErrorStrings.NEGATIVE_TERM_DEGREE.getMessage());
        }
        if (degree < coefficients.size()) {
            return coefficients.get(degree);
        }
        return coefficients.get(0).zero();
    }

    public T getValue(int degree) {
        return coefficient(degree);
    }

    private void checkOperand(Polynomial<T> other) {
        if (other == null) {
            throw new NullPointerException(ErrorStrings.NULL_OPERAND.getMessage());
        }
    }

    @Override
    public Polynomial<T> add(Polynomial<T> other) {
        checkOperand(other);
        int max = Math.max(this.degree(), other.degree());
        List<T> result = new ArrayList<>(max + 1);
        for (int i = 0; i <= max; i++) {
            result.add(this.coefficient(i).add(other.coefficient(i)));
        }
        return new Polynomial<>(max, result);
    }

    @Override
    public Polynomial<T> negate() {
        List<T> result = new ArrayList<>(coefficients.size());
        for (T c : coefficients) {
            result.add(c.negate());
        }
        return new Polynomial<>(coefficients.size() - 1, result);
    }

    @Override
    public Polynomial<T> multiply(Polynomial<T> other) {
        checkOperand(other);
        T zero = coefficients.get(0).zero();
        int thisDeg = this.degree();
        int otherDeg = other.degree();
        List<T> result = new ArrayList<>(thisDeg + otherDeg + 1);
        for (int i = 0; i <= thisDeg + otherDeg; i++) {
            result.add(zero);
        }
        for (int i = 0; i <= thisDeg; i++) {
            for (int j = 0; j <= otherDeg; j++) {
                T product = this.coefficient(i).multiply(other.coefficient(j));
                result.set(i + j, result.get(i + j).add(product));
            }
        }
        return new Polynomial<>(thisDeg + otherDeg, result);
    }

    @Override
    public Polynomial<T> zero() {
        T zero = coefficients.get(0).zero();
        List<T> result = new ArrayList<>(1);
        result.add(zero);
        return new Polynomial<>(0, result);
    }

    @Override
    public Polynomial<T> one() {
        T one = coefficients.get(0).one();
        List<T> result = new ArrayList<>(1);
        result.add(one);
        return new Polynomial<>(0, result);
    }

    @Override
    public boolean equals(Object other) {
        if (!hasSameType(other)) {
            throw new ClassCastException(ErrorStrings.INCOMPATIBLE_TYPE.getMessage());
        }
        Polynomial<?> o = (Polynomial<?>) other;
        if (this.coefficients.size() != o.coefficients.size()) {
            return false;
        }
        for (int i = 0; i < this.coefficients.size(); i++) {
            if (!this.coefficients.get(i).equals(o.coefficients.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return coefficients.hashCode();
    }
}