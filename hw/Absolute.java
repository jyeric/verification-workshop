// 1. Write the contract of abs and verify it.
//   ./esc hw/Absolute.java
public class Absolute {

    // Returns the absolute value of n.
    //@ requires n >= Integer.MIN_VALUE + 1 && n <= Integer.MAX_VALUE;
    //@ ensures \result >= 0 && ( \result == n || \result == -n );
    public static int abs(int n) {
        if (n < 0) {
            return -n;
        }
        return n;
    }
}
