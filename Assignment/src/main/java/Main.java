import AbstractMath.Rings.ZMod.ZModElement;
import AbstractMath.Rings.ZMod.Mod3;
import AbstractMath.Rings.ZMod.Mod5;
import AbstractMath.Rings.Structures.Matrix.Matrix;
import AbstractMath.Rings.Structures.Polynomial.Polynomial;

public class Main {
    public static void main(String[] args) {
        var pb = Polynomial.<ZModElement<Mod5>>builder(1);
        pb.setValue(0, new ZModElement<>(3, new Mod5()));
        pb.setValue(1, new ZModElement<>(4, new Mod5()));
        Polynomial<ZModElement<Mod5>> p = pb.build();

        var mb = Matrix.<Polynomial<ZModElement<Mod5>>>builder(1, 1);
        mb.setValue(0, 0, p);
        Matrix<Polynomial<ZModElement<Mod5>>> m = mb.build();

        try {
            ZModElement<Mod3> z3 = new ZModElement<>(1, new Mod3());
            ZModElement<Mod5> z5 = new ZModElement<>(1, new Mod5());
            z3.equals(z5);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}