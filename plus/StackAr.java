// 4. A stack of ints on an array: implement it and write its specification.
//   ./esc plus/StackAr.java
// The fields are spec_public: they are private, but the public contracts may mention them.
public class StackAr {

    /** Stack capacity. */
    private static final /*@ spec_public @*/ int DEFAULT_CAPACITY = 10;

    /** Array of elements on the stack. */
    private final /*@ spec_public @*/ int[] elems;

    /** Index of the top element (-1 when the stack is empty). */
    private /*@ spec_public @*/ int top = -1;

    //@ public invariant -1 <= top && top < elems.length;

    //@ ensures elems.length == DEFAULT_CAPACITY;
    //@ ensures isEmpty();
    public StackAr() {
        this(DEFAULT_CAPACITY);
    }

    //@ requires capacity >= 0;
    //@ ensures elems.length == capacity;
    //@ ensures isEmpty();
    public StackAr(int capacity) {
        elems = new int[capacity];
    }

    //@ ensures \result <==> (top == -1);
    public /*@ pure @*/ boolean isEmpty() {
        return top == -1;
    }

    //@ ensures \result <==> (top == elems.length - 1);
    public /*@ pure @*/ boolean isFull() {
        return top == elems.length - 1;
    }

    //@ ensures \result >= 0 && \result <= elems.length;
    //@ ensures \result == top + 1;
    public /*@ pure @*/ int size() {
        return top + 1;
    }

    //@ requires !isFull();
    //@ ensures size() == \old(size()) + 1;
    //@ ensures elems[top] == o;
    //@ assignable elems[*], top;
    public void push(int o) {
        if (isFull()) {
            throw new IllegalStateException("Stack is full");
        }
        elems[++top] = o;
    }

    //@ requires !isEmpty();
    //@ ensures size() == \old(size()) - 1;
    //@ ensures \result == \old(elems[top]);
    //@ assignable top;
    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elems[top--];
    }

    //@ requires !isEmpty();
    //@ ensures \result == \old(elems[top]);
    public /*@ pure @*/ int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elems[top];
    }
}
