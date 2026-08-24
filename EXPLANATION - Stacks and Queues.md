# Explanation: Stacks & Queues — Concept + Array and Linked List Implementations

---

## PART 1 — The Two Ideas

### Stack — Last In, First Out (LIFO)

A stack is a collection where you can only add and remove at ONE end,
called the **top**. The last element pushed in is the first one that
comes out.

Real-world picture: a stack of plates. You put a plate on top; you take
a plate from the top. Reaching the bottom plate means removing every
plate above it first.

```
PUSH 10:        PUSH 20:        PUSH 30:        POP → returns 30
                ┌────┐          ┌────┐          ┌────┐
                │ 10 │          │ 20 │          │ 20 │ ← top
                └────┘          │ 10 │          │ 10 │
                                └────┘          └────┘
```

Core operations:

| Operation | What it does | Error case |
|---|---|---|
| `push(x)` | put x on top | stack full (array only) |
| `pop()` | remove + return the top | stack empty (**underflow**) |
| `peek()` / `top()` | look at the top without removing | stack empty |
| `isEmpty()` | any elements? | — |

Where stacks show up: function call/return order, undo, browser back
button, expression evaluation, recursion, backtracking.

---

### Queue — First In, First Out (FIFO)

A queue allows adding at one end (**rear** / back) and removing from the
OTHER end (**front**). The first element to enter is the first to leave.

Real-world picture: a line at a bank. First person in line is served
first; newcomers join at the back.

```
ENQUEUE 10:   front → [10] ← rear
ENQUEUE 20:   front → [10] [20] ← rear
ENQUEUE 30:   front → [10] [20] [30] ← rear
DEQUEUE → returns 10:
              front → [20] [30] ← rear
```

Core operations:

| Operation | What it does | Error case |
|---|---|---|
| `enqueue(x)` | add x at the rear | queue full (array only) |
| `dequeue()` | remove + return from the front | queue empty (**underflow**) |
| `peek()` / `front()` | look at the front without removing | queue empty |

Where queues show up: printer job scheduling, keyboard buffers, BFS
graph traversal, task scheduling, customer service systems.

### The one-line summary

> **Stack = one door (top). Queue = two doors (front for leaving, rear
> for entering).** Everything else follows from that.

---
---

## PART 2 — Stack Implemented With an Array

### The idea

Reserve an array of fixed capacity N and keep an integer `top` that
stores the INDEX of the current topmost element.

Convention used below: **`top = -1` means empty**, and push increments
`top` before writing.

```
capacity = 5

empty            after push(10)     after push(20)     after pop()
top = -1         top = 0            top = 1            top = 0

[ _ _ _ _ _ ]    [ 10 _ _ _ _ ]     [ 10 20 _ _ _ ]    [ 10 _ _ _ _ ]
                ↑                  ↑                  ↑
             index 0            index 1            index 0
```

### How to read these pseudocode blocks (read once)

Pseudocode is **structured English for describing an algorithm**. It is
not a real language — it never compiles, there are no semicolons, and
you write it however your lecturer likes. Mine uses three symbols:

| Symbol | Meaning | Real code equivalent |
|---|---|---|
| `x ← y` | store y INTO x (assignment) | `x = y;` |
| `a = b` | COMPARE a and b (question) | `a == b` |
| `ERROR "..."` | report a problem and stop | `printf("..."); return;` |

And three words:

- **DECLARE** — create variables (like `int data[MAX]; int top = -1;`)
- **FUNCTION ... END** — a function definition; everything between them belongs to that function
- **RETURN x** — hand x back to whoever called the function (`return x;`)

So when you see:

```
FUNCTION push(x)
    IF isFull() THEN ERROR "overflow"
```

read it aloud as: *"to push something: if the stack is full, complain
about overflow and stop; otherwise continue below."* Every pseudocode
line in this document translates almost word-for-word into the C and
Java sketches that follow each block.

---

### Pseudocode (annotated line by line)

```
ALGORITHM StackArray
    // The whole stack is just an array + one number.
    DECLARE data[MAX]        // room for MAX values (e.g. MAX = 100)
    top ← -1                 // WHERE the newest element sits.
                             // -1 is our secret code for "nothing inside".
                             // After the first push, top becomes 0,
                             // because index 0 is the first array slot.

FUNCTION isEmpty()           // "is the stack empty?"
    RETURN top = -1          // still at the starting value → we never
                             // pushed anything → yes, empty.

FUNCTION isFull()            // "is there still room?"
    RETURN top = MAX - 1     // MAX - 1 is the LAST valid array index
                             // (for MAX=5: indexes 0,1,2,3,4 → last is 4).
                             // If top already sits there, no room left.

FUNCTION push(x)             // put a new value on top
    IF isFull() THEN ERROR "overflow"   // check BEFORE writing, so we can
                                        // never write outside the array
    top ← top + 1           // slide the marker up to the next free slot...
    data[top] ← x           // ...and drop x exactly on that slot.
                            // Example: empty stack, push(10):
                            //   top: -1 → 0,  data[0] = 10

FUNCTION pop()               // remove AND give back the top value
    IF isEmpty() THEN ERROR "underflow" // cannot remove what isn't there
    x ← data[top]           // copy out the top value FIRST...
    top ← top - 1           // ...then slide the marker DOWN one slot.
                            // Note we do NOT erase data[top]. The old
                            // value just sits there forgotten; the next
                            // push will overwrite it anyway. Only the
                            // MARKER moving down makes it "removed".

FUNCTION peek()              // look at the top WITHOUT removing it
    IF isEmpty() THEN ERROR "underflow"
    RETURN data[top]        // same read as pop(), but no marker move
END
```

### Why every operation is O(1)

`top` always points directly at the newest element. Push/pop never scan
or shift anything — they touch exactly one cell (`data[top]`) plus the
counter. This is THE advantage of array implementation: raw speed and
memory locality.

### The weaknesses

1. **Fixed capacity.** You must guess MAX in advance. Pushing past it is
   overflow.
2. **Wasted memory.** If you over-estimate MAX, unused cells are locked
   up even when the stack holds 3 items.

### C sketch

```c
#define MAX 100
int data[MAX];
int top = -1;                      // -1 = empty

void push(int x) {
    if (top == MAX - 1) { printf("Overflow\n"); return; }
    data[++top] = x;
}

int pop() {
    if (top == -1) { printf("Underflow\n"); return -1; }
    return data[top--];
}
```

### Java sketch

```java
static int[] data = new int[100];
static int top = -1;

static void push(int x) {
    if (top == data.length - 1) { System.out.println("Overflow"); return; }
    data[++top] = x;
}

static int pop() {
    if (top == -1) { System.out.println("Underflow"); return -1; }
    return data[top--];
}
```

Identical logic — the differences are only syntax (`->` vs `.`, printf vs
println). In real Java you would use `ArrayDeque` or `Stack`, but for
learning, hand-roll it.

---
---

## PART 3 — Stack Implemented With a Linked List

### The idea

Use a singly linked list where the **HEAD of the list is the TOP of the
stack**. Every operation happens at the head, which you already know how
to do in O(1):

- push = insertAtStart
- pop = deleteAtStart
- peek = read head's data

```
push(30):                 push(20):
(top)                     (top/head)
NULL                      [20] → [NULL]... more precisely:

head                       head
 ↓                          ↓
NULL      → push(20) →    [20] → NULL   → push(10) →   [10] → [20] → NULL
                          ↑ top                        ↑ top
```

Why must the head be the top? Because inserting/deleting at the END of
an SLL costs O(n) (walk to find last). At the START it is O(1). Choosing
head-as-top is what makes all stack operations O(1).

### Pseudocode

```
ALGORITHM StackLinkedList
    topNode ← NULL           // a POINTER to the first node of the list.
                             // That node IS the top of the stack.
                             // NULL = "the list is empty" = empty stack.

FUNCTION isEmpty()
    RETURN topNode = NULL    // no first node → nothing in the stack.
                             // (Compare: array version asked top = -1.
                             // Same question, different way of asking.)

FUNCTION push(x)             // this is insertAtStart from your SLL code!
    newNode ← CREATE NODE(x) // build a node holding x, next not set yet
    newNode.next ← topNode   // STEP 1: new node points at the old first
                             // node. (If stack was empty, it points at
                             // NULL — which is correct for a last node.)
    topNode ← newNode        // STEP 2: the new node IS the first now.
                             // Order matters: if you did step 2 first,
                             // you'd lose the address of the old list.

FUNCTION pop()               // this is deleteAtStart from your SLL code!
    IF isEmpty() THEN ERROR "underflow"
    x ← topNode.data         // copy out the value BEFORE losing the node
    topNode ← topNode.next   // head moves to the second node; the old
                             // first node becomes unreachable → removed.
                             // In C you must also free() that old node —
                             // save its address in temp first! Java's GC
                             // does this step for you.
    RETURN x

FUNCTION peek()
    IF isEmpty() THEN ERROR "underflow"
    RETURN topNode.data      // read the value; change nothing
END
```

Notice this is EXACTLY your single linked list `InsertAtStart` +
`DeleteAtStart` code with different names.

### C sketch — the free() discipline

```c
struct Node { int data; struct Node *next; };

struct Node *topNode = NULL;

void push(int x) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) { printf("Overflow (no memory)\n"); return; }
    newNode->data = x;
    newNode->next = topNode;      // STEP 1: wire new node
    topNode = newNode;            // STEP 2: move head/top
}

int pop() {
    if (topNode == NULL) { printf("Underflow\n"); return -1; }
    struct Node *temp = topNode;  // save BEFORE losing the address
    int x = temp->data;
    topNode = topNode->next;
    free(temp);                   // release the popped node
    return x;
}
```

⚠ C-specific trap: read `temp->data` and advance `topNode` BEFORE
`free(temp)`. Reading anything from a freed pointer is undefined
behaviour.

In Java, drop `temp`/`free` — just `x = topNode.data; topNode =
topNode.next; return x;` and let the GC collect the old node.

### Strengths and weaknesses

| | Array stack | Linked list stack |
|---|---|---|
| Capacity | Fixed (MAX), can overflow | Grows until RAM runs out |
| Memory per element | Just the value (compact) | Value + next pointer (overhead) |
| Speed | O(1), very cache-friendly | O(1), slightly slower per op |
| Worst-case malloc cost | None | Each push allocates |
| Simplicity | Very simple | Requires node management |

---
---

## PART 4 — Queue Implemented With an Array

### Naive approach and why it fails

Keep two indices, `front` and `rear`. Enqueue writes at `++rear`,
dequeue takes `front++`.

```
capacity = 5
enqueue 10, 20, 30:      dequeue twice:
front = 0                front = 2
rear  = 2                rear  = 2

[ 10 20 30 _ _ ]
          ↑↑
        front, rear both sit at index 2
```

Now try to enqueue 40: `rear` becomes 3, fine. But keep going — enqueue
50, then try again: rear reaches 4 (the end), yet cells 0 and 1 are FREE.
The naive queue reports "full" while half-empty. Dequeued slots are never
reusable because indices only march forward.

### Fix: the CIRCULAR queue

Wrap the indices around using modulo arithmetic:

```
index = (index + 1) % capacity

after index 4 comes index 0 again:

[_ _ 30 40 50]
  ↑ rear wrapped here!
  front still at 2
```

One slot is deliberately sacrificed to distinguish FULL from EMPTY
(both states otherwise look identical when front == rear):

```
EMPTY: front = (rear + 1) % capacity     FULL: (rear + 1) % capacity == front
```

### Pseudocode

```
ALGORITHM CircularQueueArray
    DECLARE data[MAX]        // the storage, same as the stack's array
    front ← 0                // index we DEQUEUE from (the oldest item)
    rear ← MAX - 1           // index we most recently ENQUEUED at.
                             // Starts at MAX-1 (one "before" index 0 in
                             // ring terms) so that the FIRST enqueue's
                             // wrap step below lands neatly on index 0.
    count ← 0                // how many items are inside right now.
                             // Keeping a counter is the EASY way to tell
                             // full from empty; without it both states
                             // look confusingly similar.

FUNCTION isEmpty()
    RETURN count = 0         // counter says nothing inside → empty.
                             // Simple and unambiguous.

FUNCTION isFull()
    RETURN count = MAX       // MAX items inside → every slot occupied.

FUNCTION enqueue(x)          // add x at the BACK of the line
    IF isFull() THEN ERROR "overflow"   // check first, as always
    rear ← (rear + 1) MOD MAX // THE circular trick: step forward one
                             // slot, but WRAP AROUND to 0 after the end.
                             // e.g. MAX=5: 0→1→2→3→4→0→1...
                             // First call check: rear was MAX-1, so
                             // (MAX-1)+1 = MAX, and MAX MOD MAX = 0 →
                             // lands on index 0 exactly as promised.
    data[rear] ← x           // drop x into that slot
    count ← count + 1        // one more item inside

FUNCTION dequeue()           // remove from the FRONT of the line
    IF isEmpty() THEN ERROR "underflow"
    x ← data[front]          // copy out the oldest value first
    front ← (front + 1) MOD MAX   // front also walks in a circle with
                                  // the same wrap trick — so slots freed
                                  // at the front get REUSED by later
                                  // enqueues. That is what fixes the
                                  // naive version's wasted space.
    count ← count - 1        // one fewer item inside
    RETURN x
END
```

Every operation is O(1). The modulo wrap is the ONLY trick worth
memorising here.

### Walkthrough of the wrap-around

```
MAX = 5, enqueue 10..50, dequeue twice, enqueue 60:

after enqueues:      [10 20 30 40 50]   front=0 rear=4 count=5
after 2 dequeues:    [_ _ 30 40 50]     front=2 rear=4 count=3
enqueue 60:          rear=(4+1)%5=0
                     [60 _ 30 40 50]    front=2 rear=0 count=4 ✓
                     (60 sits at index 0 — the ring closed)
```

---
---

## PART 5 — Queue Implemented With a Linked List

### The idea

A singly linked list with TWO permanent pointers:

- `front` — the HEAD of the list → dequeue happens here
- `rear` — the LAST node → enqueue happens here

```
front                         rear
  ↓                             ↓
[10] → [20] → [30] → NULL

dequeue removes [10];  enqueue appends after [30]
```

Both ends are O(1): deletion at head is O(1) (you know this from SLL
deleteAtStart), and insertion at the tail is O(1) BECAUSE we keep the
`rear` pointer — no walking needed. That second pointer is the entire
point of the implementation.

⚠ Direction matters! Do NOT flip it (enqueue at head, dequeue at tail).
Then dequeue needs the SECOND-to-last node, which requires an O(n) walk.
Front-for-dequeue + rear-for-enqueue keeps everything O(1).

### Pseudocode

```
ALGORITHM QueueLinkedList
    front ← NULL             // pointer to the FIRST node = the end we
                             // DEQUEUE from (oldest item)
    rear ← NULL              // pointer to the LAST node = the end we
                             // ENQUEUE at (newest item).
                             // Two pointers, because a queue touches
                             // BOTH ends and each must be O(1).

FUNCTION isEmpty()
    RETURN front = NULL      // no first node → empty.
                             // (Checking front, not rear — either would
                             // work if we maintain them correctly.)

FUNCTION enqueue(x)          // add x at the back of the line
    newNode ← CREATE NODE(x) // build the node; its next stays NULL,
                             // because it will be the new LAST node
    IF rear = NULL THEN      // was the queue empty?
        front ← rear ← newNode   // then this one node is BOTH ends:
                                 // it is the first AND the last.
                                 // ("a ← b ← c" means assign right to
                                 // left: both get the same value.)
    ELSE                     // queue already has items:
        rear.next ← newNode  // hang the new node after the old last...
        rear ← newNode       // ...and crown it the new last.

FUNCTION dequeue()           // remove from the front of the line
    IF front = NULL THEN ERROR "underflow"  // nothing to serve
    x ← front.data           // copy out the oldest value first...
    front ← front.next       // ...then move front to the second node.
                             // The old first node is now out of the
                             // queue. (In C: free(temp) after saving
                             // its address; Java's GC handles it.)
    IF front = NULL THEN     // did that dequeue EMPTY the queue?
        rear ← NULL          // ★ YES → reset rear too!
                             // Otherwise rear still points at the
                             // removed node, and the NEXT enqueue's
                             // `rear.next ← newNode` writes into a
                             // dead node. This is THE classic bug.
    RETURN x
```

★ The classic bug: forgetting to reset `rear` to NULL when the last
node is dequeued. Later, `isEmpty()` checks `front`, sees NULL, behaves
fine — but `rear` still points at freed memory, and the NEXT enqueue's
`rear.next = newNode` writes into garbage. Always update BOTH pointers
together.

### C sketch

```c
struct Node *front = NULL, *rear = NULL;

void enqueue(int x) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) { printf("Overflow\n"); return; }
    newNode->data = x;
    newNode->next = NULL;

    if (rear == NULL) {           // was empty
        front = newNode;
        rear  = newNode;
    } else {
        rear->next = newNode;     // link after old last
        rear = newNode;           // rear advances
    }
}

int dequeue() {
    if (front == NULL) { printf("Underflow\n"); return -1; }
    struct Node *temp = front;    // save before advancing
    int x = temp->data;
    front = front->next;
    if (front == NULL) rear = NULL;   // ★ both pointers together
    free(temp);
    return x;
}
```

Java version: same structure minus `malloc/free/temp`.

---
---

## PART 6 — The Full Comparison

| Aspect | Array stack | LL stack | Array queue | LL queue |
|---|---|---|---|---|
| Key state variable(s) | `top` index | head pointer (= top) | `front`, `rear`, `count` (circular!) | `front` AND `rear` pointers |
| Overflow possible? | Yes (fixed MAX) | Only when RAM exhausted | Yes (fixed MAX) | Only when RAM exhausted |
| Extra memory per item | none | 1 pointer | none | 1 pointer |
| All core ops O(1)? | yes | yes | yes (with circular wrap) | yes (thanks to rear pointer) |
| Trickiest bug | off-by-one on top | forgetting free() (C) | full-vs-empty confusion, missing `% MAX` | dangling rear after last dequeue |
| Best when | max size known, speed critical | size unpredictable | max size known, speed critical | size unpredictable |

## Which implementation should YOU write for class?

Rule of thumb your lecturer will likely grade against:

- Asked for **"simple"** → array version (fewer moving parts).
- Asked for **"dynamic/unlimited"** → linked list version.
- Any queue built on an array MUST be circular, or it wastes space —
  say this sentence in your answer; it earns marks.
- In a linked-list queue, mention WHY you keep two pointers (rear makes
  enqueue O(1)); and WHY head is the dequeue side (tail-deletion would
  be O(n)).

## How this connects to what's already in this workspace

- Linked-list stack = your `LINKED LISTS/Insert Operations/InsertAtStart`
  + `LINKED LISTS/Delete Operations/DeleteAtStart` glued under new names.
- Linked-list queue = InsertAtEnd (via rear) + DeleteAtStart (via front),
  with the extra rule "never leave rear dangling".
- Array circular queue uses the same `% capacity` wrap-around thinking
  as the circular linked lists folder — a bounded resource reused in a
  loop.

---

## Quick reference card

```
STACK (LIFO)                    QUEUE (FIFO)
one open end: TOP               two ends: FRONT out, REAR in
push  → add at top              enqueue → add at rear
pop   → remove from top         dequeue → remove from front
peek  → read top                peek    → read front
empty check: top == -1 / NULL   empty check: count==0 / front==NULL

ARRAY versions                  LINKED LIST versions
stack: top index ± 1            stack: head IS the top
queue: circular, % MAX wrap     queue: front=head (out), rear=last (in)
```
