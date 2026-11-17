# Homework: answers

Name(s): Yue Jiang 999027808

## OpenJML

### 1. Absolute

Q: What does OpenJML complain about before you write a contract, and for which input?

OpenJML complains that there is an issue in the negation of an int in JAVA. For input -2^31, its negation 2^31 is not in the interval of int.

Q: Which `requires` did you need, and why? Could you avoid it by changing the `ensures`?

We need the requirement that it is in the interval of int and is not equal to MIN_INT. Because we cannot find an absolute value of MIN_INT in the interval of int.

It cannot be avoided by changing the `ensures` because for -2^31 it always provide an error and `ensure` cannot resolve the error.


### 2. SumX

Q: Which proof obligations fail? Is the program wrong?

The proof obligations of the variable `s` fails. The verifier does not know about `s` so it reflects postcondition failure and ArithmeticOperationRange failure.

The program is right. It is a false alarm.


Q: Largest N for which `x <= N` verifies? What happens with N + 1, and why?

N equals to 65535 verifies. For N + 1 = 65536, 1+...+65536 > INT_MAX. It will return with sum: overflow in int sum.

Because the value 65536 will cause an overflow and the result does not equal to the value of x * (x + 1) / 2.


### 3. Max

Q: Is the remaining failure caused by your invariants or by the code? (Your hypothesis.)

It is caused by the code. The code ends at a.length - 1, which misses the last element.


### 4. BinarySearch

Q: What does OpenJML report, in which method, and for which concrete call? How large must the array be?

It reports ArithmeticOperationRange. It reports the method `midpoint`. In the call for calculation for mid in the interval.

The array must be larger than INT_MAX / 2 + 1. 


Q: What happens without `/*@ pure @*/` on `midpoint`, and why?

Without `/*@ pure @*/`, OpenJML does not know whether `midpoint` has a side effect or not. So it does not whether the array `a[]` has changed. So it cannot prove loop invariants and postconditions related to `a`.


## JBMC

### 5. Max, again

Q: What input did JBMC find, and what goes wrong? Was your hypothesis of exercise 3 right?

It finds a counterexample `a = {3, 32770}` and the counterexample produce an error in the second loop of the assertion `r >= a[j]` for the second number.

The hypothesis of exercise 3 is right.

Q: Smallest `--unwind` and `--max-nondet-array-length` that find it? Relation to the small scope hypothesis?

The smallest `--unwind` is 2 and smallest `--max-nondet-array-length` is also 2.

For `--unwind`, in the `MaxCheck.java`, it has to iterates twice for an correct answer. `Max.java` does not interate which ignores the last value, ignoring the last value which may be correct.

For `--max-nondet-array-length`, the array must be of length 2 to find that `Max.java` ignores the last value. It always consider the first value in the array so length=1 does NOT work.

The small scope for this exercise is length 2, which can catch the bug.

Q: Why does `--unwind 5` with unwinding assertions fail on the fixed version? Smallest K with `--max-nondet-array-length 4`, and why not one less? What has been proved?

Because it find an array of size 5 (a = {-2147483648, -2147483648, -2147483648, 1, 0}) and it requires the loop to be unwind 6 times. So unwinding assertions fail.

The smallest k for `--max-nondet-array-length 4` is 5. For one less, it does not have the fifth loop that compares. So it fails.

It has been proved that for arrays of lengths smaller or equal to 4 is correct.


### 6. SumX

Q: Which values of `x` were checked with `--unwind 10 --no-unwinding-assertions`? Why does it fail with unwinding assertions? Which K is a proof? Sizes and times for K = 10, 100 and that K.

Value `x` checks 0 to 9. And it ends at that point.

It fails with unwinding assertions because for unwind 10 loops cannot complete the number of loops that is required.

K = 1001 is the proof.

Unwind 10: (SAT formula: 4638 variables, 13644 clauses, solved in 0.00s)
Unwind 100: (SAT formula: 9587 variables, 30260 clauses, solved in 0.02s)
Unwind 1001: (SAT formula: 66870 variables, 666136 clauses, solved in 1.60s)

Q: What did OpenJML need from you, and what did JBMC need?

OpenJML needs requirements, ensures and loop invariant that cover all the variables that is given.

JBMC needs harness assuses preconditions and asserts the postcondition. Also the number of loops that it will wind. 


### 7. BinarySearch

Q: Why does JBMC miss the bug with the `search` harness? Could a larger bound help?

Because `--max-nondet-array-length` has defined that the maximum length of the array is 5. So the calculation of lo + hi will not encounter overflow error. 

A larger bound does not help because for such a large array, it will exceed the maximum memory available.

Q: What did the `midpoint` harness find?

It will find a special solution (lo = 229638144, hi = 2068840448) that will cause the assertion fails.

## Comparison

### 8. Comparison

| program | OpenJML: result, what you wrote | JBMC: result, bound used |
| --- | --- | --- |
| SumX | Verified. Additional loop invariant for varaible `s`. | Properties hold. Bound for --wind=1001 |
| Max | Verified after fix. Additional loop invariant for varaible `i`, `m` and decreases. Find the bug statement ` i < a.length - 1` | Properties hold. Bound for array length of 4, unwind = 5 |
| BinarySearch | Verified after fix. Fix the midpoint calculation that may cause an overflow. | Properties hold. Bound for array length of 5. Requires additional check for midpoint. |

Q: When would you use each tool?

We can use OpenJML when we need to prove for all inputs. For example a large input or an array without upper bound. However, we need to write proofs for all invariants.

We can use JBMC to find the exact counterexample of the problem of the program. However, it cannot find an error for large inputs such as BinarySearch.