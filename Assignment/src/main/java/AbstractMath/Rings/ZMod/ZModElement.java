package AbstractMath.Rings.ZMod;

import AbstractMath.Rings.RingElement;

public class ZModElement<M extends Modulus> implements RingElement<ZModElement<M>> {

    private final int value;
    private final M modulus;

    public ZModElement(int value, M modulus) {
        if (modulus == null) {
            throw new NullPointerException(ErrorStrings.NULL_MODULUS.getMessage());
        }
        int m = modulus.value();
        if (m <= 0 || m >= 1000) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_MODULUS.getMessage());
        }
        if (value >= 1000) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_VALUE.getMessage());
        }
        this.modulus = modulus;
        this.value = normalize(value, m);
    }

    private static int normalize(int v, int m) {
        int r = v % m;
        if (r < 0) {
            r += m;
        }
        return r;
    }

    public int getValue() {
        return value;
    }

    public int getModulus() {
        return modulus.value();
    }

    @Override
    public boolean hasSameType(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        ZModElement<?> o = (ZModElement<?>) other;
        if (this.modulus.getClass() != o.modulus.getClass()) {
            return false;
        }
        return this.modulus.value() == o.modulus.value();
    }

    private void checkOperand(ZModElement<M> other) {
        if (other == null) {
            throw new NullPointerException(ErrorStrings.NULL_OPERAND.getMessage());
        }
        if (this.modulus.value() != other.modulus.value()) {
            throw new IllegalArgumentException(ErrorStrings.MODULUS_MISMATCH.getMessage());
        }
    }

    @Override
    public ZModElement<M> add(ZModElement<M> other) {
        checkOperand(other);
        int m = modulus.value();
        return new ZModElement<>(normalize(this.value + other.value, m), modulus);
    }

    @Override
    public ZModElement<M> negate() {
        int m = modulus.value();
        return new ZModElement<>(normalize(-this.value, m), modulus);
    }

    @Override
    public ZModElement<M> multiply(ZModElement<M> other) {
        checkOperand(other);
        int m = modulus.value();
        return new ZModElement<>(normalize(this.value * other.value, m), modulus);
    }

    @Override
    public ZModElement<M> zero() {
        return new ZModElement<>(0, modulus);
    }

    @Override
    public ZModElement<M> one() {
        int m = modulus.value();
        return new ZModElement<>(normalize(1, m), modulus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ZModElement<?>)) {
            throw new ClassCastException(ErrorStrings.INCOMPATIBLE_TYPE.getMessage());
        }
        ZModElement<?> o = (ZModElement<?>) other;
        if (this.modulus.value() != o.modulus.value()) {
            throw new IllegalArgumentException(ErrorStrings.MODULUS_MISMATCH.getMessage());
        }
        if (this.getClass() != o.getClass()
                || this.modulus.getClass() != o.modulus.getClass()) {
            throw new ClassCastException(ErrorStrings.MODULUS_TYPE_MISMATCH.getMessage());
        }
        return this.value == o.value;
    }

    @Override
    public int hashCode() {
        return 31 * value + modulus.value();
    }
}
