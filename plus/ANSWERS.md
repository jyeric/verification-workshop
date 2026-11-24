# Homework plus: answers

Name(s): Yue Jiang 999027808

### 1. Invariants from an LLM

| method | LLM | rounds | mistakes | final invariants by |
| --- | --- | --- | --- | --- |
| count | claude-haiku-5.5-high | 1 | NONE | claude-haiku-5.5-high |
| reverse | grok-4.7-xhigh | 1 | NONE | grok-4.7-xhigh |
| isqrt | gemini-4-carbon | 1 | NONE | gemini-4-carbon |
| dutchFlag | gemini-4-hydrogen-xhigh | 1 | NONE | gemini-4-hydrogen-xhigh |


Q: Why is it safe to use invariants written by an LLM here, even if the LLM is often wrong?

Because OpenJML will fail all the wrong invariants. With the hints of OpenJML, it is safe to update or try again with LLMs.

### 2. The Zune bug

Q: `--unwind 31 --no-unwinding-assertions` says SUCCESSFUL. Does that mean `yearOf` is correct for every day it checked?

No. Because it sets the maximum winding times. However, the loop may execute more than the times defined if there is an error in the loop. And this error cannot be found.

Q: What does JBMC report without `--no-unwinding-assertions`? Is K too small, or is something else wrong?

It will give an counterexample at line 11: days = 1827

The K is not too small because it only calculates for 30 years. So K=31 is enough. There is a loop error in the code because the loop should always stop for 30.

Q: What is wrong with the loop? Which date froze the Zune?

When the day is at exactly 366 and in the leap year. It will stay infinite because it will go into the if-statement of isLeapYear and will not minus anything. It will continue the loop and will go infinite.

The last day of the leap year (for example 2008/12/31, day 10593) froze the Zune.

Q: With your fix, what is the smallest K that gives VERIFICATION SUCCESSFUL with unwinding assertions? What exactly has been proved?

The smallest K is 22. It has checked within the frame of 30 years, which has 21 common years and 8 leap years.

Q: A colleague changes `days > 366` to `days >= 366`. Which harness notices, and why?

The method yearOfIsRight notices. Because it adds the days before this year and find out that the last day of the leap year adds an additional one.

Q (★): Why does `./esc` say OK on the original? What changes with `decreases`?

Because without `decreases`, it only prove partial correctness and the loop satisfies the partial correctness. The loop must first terminate then satisfy the postcondition.

Adding `decreases` requires the loop to be finished. Thus report the bug.

### 3. Range

Q: Why does `size()` not verify? Give concrete values.

The method requires the answer should be in the interval of long. However, it is in the interval of int. So, it will cause overflow.

For example, lo = INT_MIN, hi = INT_MAX, it will cause overflow.

Q: Compare JBMC's counterexample with your answer above.

It provides a counterexample of:
    line 9: lo = -2147483648
    line 9: hi = 2147483647
It will overflow in the range of int. It will not overflow in the range of long. Thus, not equal.

### 4. StackAr

Q: Which tool reports your bug, and how? Could a bug get past OpenJML but not JBMC, or the other way around?

A bug past OpenJML but not JBMC:

Change method `pop()` to 
```
    //@ requires !isEmpty();
    //@ ensures size() == \old(size()); //modified
    //@ ensures \result == \old(elems[top]);
    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elems[top]; //modified
    }
```
OpenJML cannot find the bug if the postcondition is wrong align with code. So if the conditions are written based on codes. It cannot find it. JBMC finds it by finding out the size of the stack is wrong.

JMBC cannot find all bugs because the capacity is limited and unwrapping is limited.


### 5. SafeLock

Q: Smallest K that finds a bug? Which sequence of events? What is wrong in the code?

The smallest K that finds a bug is 5. 

In the sequence of:  
1. wrong password   
2. wrong password   
3. close  
4. wrong password  
5. correct password

The event close will reset the fail times. JMBC will try any events possible. Thus cause an error.

Q: Formula size and time for K = 8, 10, 12.

| K | formula size | time | 
| --- | --- | --- | 
| K = 8 | 46050 variables, 68865 clauses | 0.23s |
| K = 10 | 56941 variables, 87971 clauses | 0.43s |
| K = 12 | 67836 variables, 107563 clausess | 0.83s |

```
$ ./bmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 8 --no-unwinding-assertions
$ jbmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 8 --no-unwinding-assertions

99 properties hold, 0 fail   (SAT formula: 46050 variables, 68865 clauses, solved in 0.23s)
VERIFICATION SUCCESSFUL

$ ./bmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 10 --no-unwinding-assertions
$ jbmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 10 --no-unwinding-assertions

99 properties hold, 0 fail   (SAT formula: 56941 variables, 87971 clauses, solved in 0.43s)
VERIFICATION SUCCESSFUL

$ ./bmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 12 --no-unwinding-assertions
$ jbmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 12 --no-unwinding-assertions

99 properties hold, 0 fail   (SAT formula: 67836 variables, 107563 clauses, solved in 0.83s)
VERIFICATION SUCCESSFUL
```

Q: Does SUCCESSFUL with K = 12 mean the property holds for every sequence? Can unwinding assertions help?

No. Because it only consider evenets of maximum 12. Unwinding assertions does not help because there is an infinite events exists and we cannot test for infinite.
