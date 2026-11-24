import org.cprover.CProver;

// 3. A harness for the record Range.
//   ./bmc RangeCheck.size --unwind 1
public class RangeCheck {

    public static void size(int lo, int hi) {
        CProver.assume(lo <= hi);
        Range r = new Range(lo, hi);
        long Rangesize = r.size();
        long ans = (long) hi - lo + 1;
        assert Rangesize == ans;
    }
}
