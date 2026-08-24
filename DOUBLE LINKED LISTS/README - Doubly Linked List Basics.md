# Doubly Linked List — The Basics (read this first)

This folder assumes you already understand single linked lists
(see the `LINKED LISTS` folder). Everything here builds on that.

---

## 1. What changed?

In a single linked list, every node knows only who comes **after** it:

```
SLL node:        [ data | next ]
                 [  10  |  • ] → [20] → [30] → NULL
```

In a doubly linked list, every node knows who comes after it
AND who comes **before** it:

```
DLL node:   [ prev | data | next ]

NULL ← [•|10|•] ⇄ [•|20|•] ⇄ [•|30|•] → NULL

With the pointer values written out:

    node10: data=10, prev=NULL, next=node20
    node20: data=20, prev=node10, next=node30
    node30: data=30, prev=node20, next=NULL
```

Every node has TWO pointers:
- `next` — points to the following node (like an SLL)
- `prev` — points to the preceding node (NEW!)

The structure in C:

```c
struct Node {
    int data;
    struct Node *prev;   // NEW: the arrow pointing backwards
    struct Node *next;
};
```

And in Java:

```java
static class Node {
    int data;
    Node prev;           // NEW
    Node next;
}
```

---

## 2. The four "boundary rules"

A correct DLL always maintains these facts. Whenever you write an
insertion or deletion, check your code against ALL FOUR:

```
Rule 1: head->prev == NULL            (nothing before the first node)
Rule 2: last-node->next == NULL       (nothing after the last node)
Rule 3: for every node X:             X->next->prev == X   (if X->next exists)
Rule 4: for every node X:             X->prev->next == X   (if X->prev exists)
```

Rules 3 and 4 say the same thing twice: **every link must exist in both
directions**. In an SLL you update one pointer per connection. In a DLL,
EVERY connection is two pointers, so most operations touch roughly twice
as many arrows.

---

## 3. Why bother with `prev`?

Three payoffs you will actually see in this folder's code:

1. **You can walk backwards.** From any node you can reach earlier nodes.
   See `traverseBackward` in InsertAtStart.c.

2. **The "previous/current" two-pointer trick disappears.** In the SLL
   insertBefore and deleteAtEnd, you needed to drag a `previous` pointer
   along while searching. In a DLL, once you find a node, it TELLS you
   its predecessor via `node->prev`. One pointer is enough.

3. **Deleting a known node becomes O(1)** if you already hold a reference
   to it — no search needed, because the node itself knows both neighbours.

The cost: more pointers to maintain, which means more places to make
ordering mistakes. That is why almost every function below has a
comment about ORDER OF OPERATIONS.

---

## 4. The golden rule of ordering

When rewiring, **always connect the new node's own pointers FIRST**, before
touching the existing nodes' pointers.

Why? Because the moment you overwrite an existing node's pointer
(for example `head->prev = newNode`), the OLD value stored there may be
the only remaining copy of information you still need. The new node starts
with free NULL fields — it can absorb values safely at any time.

Pattern used everywhere below:

```
STEP A: newNode->next = <some existing node>    (safe: newNode was empty)
STEP B: newNode->prev = <some existing node>    (safe: newNode was empty)
STEP C: now rewire the existing nodes to point at newNode
STEP D: move head if needed
```

---

## 5. C vs Java differences (same as SLL, plus one new one)

| Aspect | C | Java |
|---|---|---|
| Creating a node | `malloc(sizeof(struct Node))`, check for NULL | `new Node()` — cannot fail |
| Freeing a deleted node | `free(ptr)` — YOU must do it | Garbage collector does it automatically |
| Accessing fields | `ptr->data` | `ptr.data` |
| NULL check on head | Same in both: `head == NULL` / `null` | |

The new wrinkle specific to DLLs: when you delete a node in C you must
free it, but you must ALSO be careful not to read `toDelete->next` AFTER
freeing it. Read everything you need from a node BEFORE calling free.
Java has no such trap.

---

## 6. Operation index

Insert Operations/
- InsertAtStart / InsertAtEnd / InsertBefore / InsertAfter (+ Combined)
Delete Operations/
- DeleteAtStart / DeleteAtEnd / DeleteAfter

Each exists as .c and .java, each mirrors its single-linked-list cousin
so you can compare directly.

---

## Quick reference summary

| Operation | SLL needed | DLL needs |
|---|---|---|
| Insert before target | previous + current (two-pointer walk) | just current (use current->prev) |
| Delete at end | previous + current (two-pointer walk) | just current (use current->prev) |
| Delete a found node | needs its PREVIOUS node | nothing extra (node knows its own prev) |
| Walk backwards | impossible | follow ->prev |
