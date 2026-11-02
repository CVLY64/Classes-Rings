package AbstractMath.Rings;

public interface RingElement<T> {

    T zero();

    T one();

    T add(T other);

    T negate();

    T multiply(T other);

    default boolean hasSameType(Object other) {
        if (other == null) {
            return false;
        }
        return this.getClass() == other.getClass();
    }
}