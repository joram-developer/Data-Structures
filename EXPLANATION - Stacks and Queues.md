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

Keep two indices ("position markers") into the array:

- `front` — where the next item to REMOVE is sitting
- `rear` — where the most recently ADDED item is sitting

Enqueue = slide `rear` one slot right (`++rear`), drop the value there.
Dequeue = read the value under `front`, then slide `front` one slot
right (`front++`). Both markers ONLY ever move rightward.

#### Trace it slowly (capacity 5)

Start — array empty, both markers at their starting positions:

```
index:     0    1    2    3    4
value:   [ _    _    _    _    _ ]
front = 0, rear = -1         (-1 = "hasn't moved yet")
```

After `enqueue 10`, `enqueue 20`, `enqueue 30`:

```
value:   [10]  20   30   _    _
          ↑              ↑
        front           rear
```

Each enqueue slid `rear` right and wrote into the new slot.

Now `dequeue` TWICE. Each dequeue READS the value under `front`,
then slides `front` right:

| dequeue call | returns | read from index | front afterwards |
|---|---|---|---|
| 1st | 10 | 0 | 1 |
| 2nd | 20 | 1 | 2 |

```
value:   [10*] 20*  30   _    _        (* = old value still physically
                ↑          ↑             in memory but LOGICALLY gone)
             front       rear
```

Stop and stare at this state. Slots 0 and 1 no longer belong to any
item in the queue — they were drained by the two dequeues. They LOOK
free. And they are... sort of.

#### The failure moment

Continue filling: `enqueue 40` slides rear to 3 (fine), `enqueue 50`
slides rear to 4. Now:

```
value:   [10*] 20*  30   40   50
                ↑               ↑
              front           rear
```

Try `enqueue 60`. Rear must move right one more — to index 5. But valid
indexes are only 0..4. There is nowhere for 60 to go, so we must report
"queue full".

Full? Count with your eyes: only THREE live items (30, 40, 50), and two
slots are sitting unused. A half-empty array refusing service — that is
the bug.

#### Why exactly does this happen?

Because neither marker can ever move LEFT or JUMP back. `++rear` and
`front++` only march forward. Once a dequeue has slid `front` past a
slot, that slot is abandoned FOREVER — no future enqueue can reach it.
Every dequeue permanently burns one slot of capacity.

Run this queue long enough and it fills itself with dead slots until it
reports full while nearly empty.

#### The fix previewed

Make the markers wrap around like a clock face: after index 4 comes
index 0 again. Computed as `(rear + 1) % capacity`:

```
(4 + 1) % 5  =  5 % 5  =  0     ← wraps back to the start!
```

Then `enqueue 60` lands in slot 0, recycling the dead space. That wrap
trick is the entire idea behind the CIRCULAR queue in the next section.

### Fix: the CIRCULAR queue

#### Step 1: understand `%` (modulo)

`a % b` is "the remainder left over after dividing a by b":

```
7  % 5 = 2      (7 ÷ 5 = 1 with remainder 2)
10 % 5 = 0      (10 ÷ 5 = 2 exactly, nothing left over)
4  % 5 = 4      (4 ÷ 5 = 0 with remainder 4 — smaller than 5, so
                 the number survives unchanged!)
```

That last row is important: for any index smaller than capacity,
`index % capacity` is just the index itself. Modulo only CHANGES
something when the number reaches or passes capacity.

#### Step 2: use it to make the indices wrap

Feed the formula `(index + 1) % capacity` with capacity 5 repeatedly:

```
index    (index + 1) % 5
  0    →     1
  1    →     2
  2    →     3
  3    →     4
  4    →     5 % 5 = 0      ★ WRAPS back to zero!
  0    →     1              ...and round and round forever
```

Exactly like a clock face: after 12 comes 1, not 13. The array's last
slot reconnects to its first slot — the storage becomes a ring.

Apply it at the failure moment from the previous section
(state `[x x 30 40 50]`, front = 2, rear = 4):

```
enqueue 60:
    rear ← (4 + 1) % 5  =  0       rear WRAPS to slot 0...
    data[0] ← 60                   ...writing INTO a recycled slot!

value:   [60*]  x   30   40   50    (* = slot reused, old value replaced)
                       ↑          ↑
                     front       rear = 0
```

Slot 0, abandoned since the very first dequeue, is back in service.
That is the entire fix — every operation stays O(1), no shifting.

#### Step 3: the new problem — FULL vs EMPTY become ambiguous

> ⚠ **Conventions changed — don't let this trip you.**
>
> The naive queue (previous section) started `rear` at **-1** because it
> ADVANCED first (`++rear`) and then wrote. THIS section uses a different,
> equally valid bookkeeping:
>
> ```
> circular convention:    front = 0, rear = 0   at creation
>                         rear = the slot where the NEXT enqueue will write
>                         enqueue: write FIRST, then advance rear
> ```
>
> Here the two markers are born EQUAL — and that equality is precisely
> what "empty" means. They separate on the first enqueue and can only
> meet again when everything has been dequeued (the full-check below
> stops rear one step short of completing a lap onto front).

Wrapping created a subtle trap. Track only `front` and `rear`
(both start at 0, capacity 5):

```
EMPTY queue:            front = 0, rear = 0
enqueue A → rear = 1
enqueue B → rear = 2
enqueue C → rear = 3
enqueue D → rear = 4
enqueue E → rear = (4+1) % 5 = 0   ← wrapped!

FULL queue (all 5 slots used):   front = 0, rear = 0
```

Look at those two final states. **Identical.** `front == rear` describes
both "nothing inside" AND "completely stuffed". One test cannot mean two
opposite things — any code checking fullness would misfire.

Two standard ways out:

**Option A — sacrifice one slot** (the classical textbook answer):
simply refuse to ever use the LAST available position. Define:

```
EMPTY test:   front == rear
FULL test:    (rear + 1) % capacity == front
```

**Why `front == rear` means empty — the gap model:**

Under this convention each marker makes a promise:

```
front = the slot the next DEQUEUE would read from
rear  = the slot the next ENQUEUE would write into
```

The live items are EXACTLY the slots crossed when walking from
`front` up to (but not including) `rear`. The data lives in the
gap between the markers:

```
front=0, rear=3   →  slots 0,1,2 alive          → 3 items
front=2, rear=3   →  slot 2 alive               → 1 item
front=3, rear=3   →  walk from 3 to 3: no slots → 0 items = EMPTY
```

Trace of the gap opening and closing again:

| event              | front | rear | gap holds |
|--------------------|-------|------|-----------|
| creation           | 0     | 0    | nothing   |
| enqueue A,B,C      | 0     | 3    | A B C     |
| dequeue × 3        | 3     | 3    | nothing ← markers reunited |

Enqueue pushes rear AWAY from front (gap grows);
dequeue pushes front TOWARD rear (gap shrinks).
Markers meeting = gap empty = queue empty.

And it can never fire falsely when full: the FULL test stops
insertion one step early, so rear is never allowed to complete a
full lap onto front while items exist. Equality stays reserved
for empty.

Because we stop ONE step early, rear can never come full circle onto
front, so `front == rear` now means ONLY empty, and the other equation
means ONLY full. They can never be confused. The price: a capacity-5
array really holds just 4 items.

Visual of "full" with capacity 5 (holding a,b,c,d — one slot idle):

```
[ d | a | b | c | _ ]
   ↑               ↑
 rear            front
check: (0 + 1) % 5 = 1 == front  → FULL, even though slot 4 is free ✓
```

**Option B — keep a counter** (what this document's pseudocode uses):
store `count` next to front/rear and update it (+1 on enqueue, −1 on
dequeue):

```
EMPTY test:   count == 0
FULL test:    count == MAX
```

No slot wasted, no cleverness needed — just one extra assignment per
operation. Most real implementations prefer this.

Either option solves the ambiguity; pick based on whether your lecturer
asks for the "classic" version (A) or the practical version (B).

(The pseudocode below deliberately sidesteps ALL of this: it starts
`rear` at `MAX - 1`, advances before writing, and carries a `count`
variable — so full/empty are just `count` comparisons and no marker
equality ever needs interpreting.)

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
MAX = 5, enqueue 10..50, dequeue twice, enqueue 60, enqueue 70:

after enqueues:      [10 20 30 40 50]   front=0 rear=4 count=5
after 2 dequeues:    [_ _ 30 40 50]     front=2 rear=4 count=3
enqueue 60:          rear=(4+1)%5=0
                     [60 _ 30 40 50]    front=2 rear=0 count=4 ✓
                     (60 sits at index 0 — RECYCLED slot!)
enqueue 70:          rear=(0+1)%5=1
                     [60 70 30 40 50]   front=2 rear=1 count=5
```

### Why FRONT needs the `% MAX` too (not just rear)

Rear's wrap lets ENQUEUE reuse freed slots. But notice what happened
above: after wrapping, the queue's newest items (60, 70) physically
live at the START of the array (slots 0 and 1), while the oldest items
(30, 40, 50) are at the end. The logical line now runs ACROSS THE SEAM:

```
physical:   [ 60 | 70 | 30 | 40 | 50 ]
              ↑         ↑
logical first  └──┘ logical last
(the queue reads: 30, 40, 50, 60, 70 — starting at front=2,
wrapping past the right edge back into slots 0 and 1)
```

So when dequeues march front forward — 2, 3, 4 — the NEXT read must
land on slot 0 to collect 60. Watch what each formula does:

```
plain:    front = front + 1        →  4 + 1 = 5   →  data[5] CRASH!
                                                 (a 5-slot array has no
                                                  index 5)
with MOD: front = (front + 1) % MAX →  5 % 5 = 0   →  data[0] = 60 ✓
```

**The point:** `rear` and `front` walk the SAME circle. Rear's wrap
would be useless if front couldn't follow it around the ring to pick
up the recycled items. The two lines are twins:

```
enqueue: rear  ← (rear  + 1) MOD MAX    producer circles forward
dequeue: front ← (front + 1) MOD MAX    consumer follows the same path
```

Full drain proving both the wrap AND that FIFO order survives the
physical scrambling:

| dequeue call | returns | read from | front after |
|---|---|---|---|
| 1st | 30 | 2 | 3 |
| 2nd | 40 | 3 | 4 |
| 3rd | 50 | 4 | **0 ← wrapped!** (plain `+1` would give 5 = crash) |
| 4th | 60 | **0** (recycled slot!) | 1 |
| 5th | 70 | 1 | 2 → empty |

Values came out in perfect FIFO order (30, 40, 50, 60, 70) even though
they were physically stored scrambled across the seam. That is the
whole magic of the circular queue.

### C implementation

```c
#include <stdio.h>

#define MAX 5

int data[MAX];
int front = 0;
int rear  = MAX - 1;     // starts "one before" slot 0, so the FIRST
                         // enqueue's (rear + 1) % MAX lands on slot 0
int count = 0;

int isEmpty(void) { return count == 0; }
int isFull(void)  { return count == MAX; }

void enqueue(int x) {
    if (isFull()) { printf("Overflow\n"); return; }
    rear = (rear + 1) % MAX;     // advance with wrap...
    data[rear] = x;              // ...then write
    count++;
}

int dequeue(void) {
    if (isEmpty()) { printf("Underflow\n"); return -1; }
    int x = data[front];         // read BEFORE moving front
    front = (front + 1) % MAX;   // front wraps around the ring too!
    count--;
    return x;
}

int main(void) {
    for (int i = 10; i <= 50; i += 10) enqueue(i);   // fill: 10..50
    dequeue(); dequeue();                            // remove 10, 20
    enqueue(60);                                     // reuses slot 0
    enqueue(70);                                     // reuses slot 1

    printf("Queue contents (front to rear): ");
    while (!isEmpty()) {
        printf("%d ", dequeue());
    }
    printf("\n");    // prints: 30 40 50 60 70  ← note 60,70 AFTER 50:
                     // FIFO order preserved across the seam
    return 0;
}
```

#### Tracing main() line by line — why the output is sorted while the array isn't

Nothing ever prints "the array". printf prints ONLY what dequeue()
returns, and dequeue() reads ONE cell: data[front]. front is the
bookmark for where the logical line starts — physical layout is
irrelevant to it.

Full trace (f=front, r=rear, c=count):

| action      | data[] physically     | f | r | c | returns/prints |
|-------------|-----------------------|---|---|---|----------------|
| enqueue 10..50 (loop) | [10 20 30 40 50] | 0 | 4 | 5 | —          |
| dequeue()   | (bytes unchanged)     | 1 | 4 | 4 | 10             |
| dequeue()   | (bytes unchanged)     | 2 | 4 | 3 | 20             |
| enqueue(60) | [60 20 30 40 50]      | 2 | 0 | 4 | — (reused slot 0) |
| enqueue(70) | [60 70 30 40 50]      | 2 | 1 | 5 | — (reused slot 1) |
| dequeue()   |                       | 3 | 1 | 4 | **30** ← data[2]! |
| dequeue()   |                       | 4 | 1 | 3 | 40             |
| dequeue()   |                       | 0 | 1 | 2 | 50 (front WRAPPED 4→0) |
| dequeue()   |                       | 1 | 1 | 1 | 60 ← recycled slot |
| dequeue()   |                       | 2 | 1 | 0 | 70 (count=0, loop stops) |

The single most important column: after the two early dequeues,
front = 2. So draining STARTS at slot 2 (holding 30), marches to the
right edge, WRAPS to slot 0, and picks up 60 and 70 last. FIFO order
falls straight out of following front around the ring.

while (!isEmpty()) simply means "while count > 0".

### Java implementation

```java
public class CircularQueue {

    static final int MAX = 5;

    static int[] data = new int[MAX];
    static int front = 0;
    static int rear = MAX - 1;   // same starting trick as C
    static int count = 0;

    static boolean isEmpty() { return count == 0; }
    static boolean isFull()  { return count == MAX; }

    static void enqueue(int x) {
        if (isFull()) { System.out.println("Overflow"); return; }
        rear = (rear + 1) % MAX;     // advance with wrap...
        data[rear] = x;              // ...then write
        count++;
    }

    static int dequeue() {
        if (isEmpty()) { System.out.println("Underflow"); return -1; }
        int x = data[front];         // read BEFORE moving front
        front = (front + 1) % MAX;   // front wraps around the ring too!
        count--;
        return x;
    }

    public static void main(String[] args) {
        for (int i = 10; i <= 50; i += 10) enqueue(i);   // fill: 10..50
        dequeue(); dequeue();                            // remove 10, 20
        enqueue(60);                                     // reuses slot 0
        enqueue(70);                                     // reuses slot 1

        System.out.print("Queue contents (front to rear): ");
        while (!isEmpty()) {
            System.out.print(dequeue() + " ");
        }
        System.out.println();   // 30 40 50 60 70
    }
}
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

Why the flipped design fails — remember: an SLL node cannot see its
predecessor. Suppose dequeue had to remove the LAST node:

    before:   [10] → [20] → [30] → NULL
    after:    [10] → [20] → NULL     ← [20].next HAD to be rewritten!

Rewriting [20].next means REACHING [20]. But we're holding a pointer
to [30], and [30] has no idea who [20] is — SLL nodes only point
forward. Even the rear pointer is useless here: it stands on exactly
the wrong node. The only route to [20] is entering at [10] and
walking: 10 → 20 → ... For n items that walk touches up to n-1 nodes.
EVERY dequeue would crawl the whole queue. That is the O(n) cost.

The standard direction dodges both problems:

    dequeue removes the HEAD   → needs NO predecessor, just
                                 front = front.next        → O(1)
    enqueue appends at TAIL    → rear pointer already stands on
                                 the spot: rear.next = newNode → O(1)

Rule of thumb for SLLs: deletion must happen at the HEAD (only place
with no predecessor requirement); insertion can go anywhere you hold
a pointer to, so enqueue claims the rear.

(Footnote: this problem is SLL-only. Doubly linked lists know their
predecessor via ->prev, so tail-deletion is O(1) there — one of the
selling points from the DOUBLE LINKED LISTS folder.)

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
node is dequeued. Here is the disaster in slow motion.

Queue holding ONE item [99] — both pointers aim at the same node,
because that node is simultaneously the first AND the last:

    front ──┐
            ├──→ [99] → NULL
    rear ───┘

dequeue() runs WITHOUT the fix:

    x = front.data          // take out 99
    front = front.next      // front → NULL
    // forgot to reset rear!
    State:  front = NULL     ("empty")
            rear  → [99]     (dead node — freed in C, un-collectable
                              in Java because rear still references it)

Nothing crashed YET. The bomb is armed. Now enqueue(42) arrives:

    enqueue asks REAR: "is the queue empty?"  (IF rear = NULL ...)
    rear still points somewhere → answer NO → takes the ELSE branch:
    rear.next ← newNode     ← WRITES INTO THE DEAD NODE
                             (C: use-after-free, memory corruption.
                              Java: silently "succeeds".)

    rear ← newNode

    Final state:  front = NULL   rear → [42]

The queue LOOKS empty (front is NULL), dequeue() answers "underflow",
and 42 can never be retrieved. You enqueued an item and it vanished —
with no error message anywhere. In Java this bug never crashes; it
just quietly eats data, which is far harder to notice.

With the fix, draining the last node sets BOTH to NULL, the pointers
agree again, and the next enqueue correctly takes the fresh-start
branch (front ← rear ← newNode).

Why does ONLY this case need special handling? Delete any node other
than the last and rear still correctly aims at the surviving last
node. But deleting the FINAL node removes the first and last node in
one blow — they are the SAME node — so BOTH markers must move
together. front and rear are a team; every operation must leave them
telling the same story.

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
