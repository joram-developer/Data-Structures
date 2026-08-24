# Explanation: Merging Two Circular Linked Lists

---

## 1. What is a circular linked list?

A normal single linked list ends when a node's `next` is NULL:

```
[10] → [20] → [30] → NULL
```

In a **circular** linked list, the last node's `next` points BACK to
the first node. There is no NULL anywhere — the chain is a ring:

```
    ┌──────────────────────┐
    ↓                      │
  [10] → [20] → [30] ──────┘

  head = the node holding 10
```

Two consequences you must internalize before touching merge:

1. **There is no natural "end" to walk to.** A loop like
   `while (current != NULL)` NEVER stops. The correct traversal
   condition is "have I come back to where I started?":

   ```
   current = head
   do {
       visit(current)
       current = current.next
   } while (current != head)      ← stop AFTER visiting head again
   ```

   This is why traversal uses a **do-while**, not a while: you must
   process the head node FIRST, then check whether you've gone full circle.
   A plain `while (current != head)` would skip the head entirely.

2. **The list is identified by ANY node, but we keep `head` as the
   reference point.** Every algorithm needs one agreed entry point.

---

## 2. What does "merging" mean here?

Merging two circular lists means producing ONE circular list that
contains all nodes of both, in this order: all of list 1 first,
then all of list 2. It is concatenation of two rings into one big ring.

```
List 1:      ┌→ [10] → [20] → [30] ─┐     (head1)
             └──────────────────────┘

List 2:      ┌→ [40] → [50] ─┐            (head2)
             └───────────────┘

Merged:      ┌→ [10] → [20] → [30] → [40] → [50] ─┐
             └─────────────────────────────────────┘   (head1)
```

(Note: if your lecturer instead wants a SORTED merge of two sorted
lists, that is a different algorithm — see the note at the end.)

---

## 3. Pseudocode

```
ALGORITHM MergeCircular(head1, head2)
// Joins two circular singly linked lists into one.
// Returns the head of the merged list.

    IF head1 = NULL THEN          // list 1 empty → result IS list 2
        RETURN head2
    IF head2 = NULL THEN          // list 2 empty → result IS list 1
        RETURN head1

    // STEP 1: find last node of list 1
    // Stop condition: next wraps around to ITS OWN head
    last1 ← head1
    WHILE last1.next ≠ head1 DO
        last1 ← last1.next

    // STEP 2: find last node of list 2
    last2 ← head2
    WHILE last2.next ≠ head2 DO
        last2 ← last2.next

    // STEP 3: rewire exactly TWO arrows
    last1.next ← head2            // ring 1 now flows into list 2
    last2.next ← head1            // end of list 2 closes onto head1

    RETURN head1
END
```

### Why only TWO pointer changes?

The merged ring must satisfy exactly one rule: every node points to
the next node, and the last node points back to the first. In the old
configuration, `[30].next` pointed at `[10]` and `[50].next` pointed
at `[40]`. Both of those arrows are WRONG for the merged ring, and they
are the ONLY wrong arrows. So fixing both fixes everything:

- `last1.next = head2` breaks ring 1 open and splices in list 2
- `last2.next = head1` closes the new bigger ring

No other node changes. No loops over the middle. That is why merging
two circular lists costs O(n1 + n2) — and the time is spent FINDING
the last nodes (steps 1 and 2), not rewiring them.

### Why must the walk condition be `last1.next != head1`?

In a linear list you walk until `next == NULL`. There is no NULL here.
The equivalent of "I am standing on the last node" is
"I am standing on the node whose next leads back home":

```
[10] → [20] → [30] ─┐
 ↑                  │
 └── head1 ─────────┘

When current = [30]:  current->next == head1  → STOP, current is last.
```

Walking with `while (current != head1)` would be wrong — it stops when
current lands back on the FIRST node, meaning you'd identify head itself
as "last" and never even reach [30].

### Edge cases (both handled by the two IFs)

| head1 | head2 | Result |
|---|---|---|
| NULL | NULL | NULL |
| NULL | list | list 2 |
| list | NULL | list 1 |
| list | list | joined ring starting at head1 |

---

## 4. C version — what differs from Java

```c
struct Node *mergeCircular(struct Node *head1, struct Node *head2) {
    if (head1 == NULL) return head2;
    if (head2 == NULL) return head1;

    struct Node *last1 = head1;
    while (last1->next != head1)      // NOTE: compare against HEAD,
        last1 = last1->next;          // not NULL!

    struct Node *last2 = head2;
    while (last2->next != head2)
        last2 = last2->next;

    last1->next = head2;              // arrow 1
    last2->next = head1;              // arrow 2

    return head1;                     // merged ring starts at head1
}
```

C-specific notes:
- Traversal uses a `do { ... } while (current != head);` loop.
- After merging, freeing memory needs care: free nodes until you wrap
  back to the (new) head — see the `freeCircular` helper in the code.
- Never read from a node after `free()`ing it.

## 5. Java version — what differs from C

```java
static Node mergeCircular(Node head1, Node head2) {
    if (head1 == null) return head2;
    if (head2 == null) return head1;

    Node last1 = head1;
    while (last1.next != head1) last1 = last1.next;

    Node last2 = head2;
    while (last2.next != head2) last2 = last2.next;

    last1.next = head2;
    last2.next = head1;

    return head1;
}
```

Identical logic; differences are only `.` vs `->`, no malloc/free,
and garbage collection handles teardown (in fact, freeing a circular
list in C requires an explicit walk-back-to-head, which Java makes
unnecessary).

---

## 6. Complexity

| Step | Cost |
|---|---|
| Find last of list 1 | O(n1) |
| Find last of list 2 | O(n2) |
| Rewire two arrows | O(1) |
| **Total time** | **O(n1 + n2)** |
| Extra space | O(1) — no new nodes created |

---

## 7. Note on SORTED merging

This document merges by CONCATENATION (all of list 1, then all of
list 2). If two lists are individually sorted and the required output
must also be sorted, that is the "merge two sorted lists" algorithm —
compare front values repeatedly, splice smaller first, then close the
result into a ring. Ask if you need that variant implemented too.

---

## Quick reference

| Aspect | Value |
|---|---|
| Walk-until-last condition | `current->next != head` (NOT `!= NULL`) |
| Correct traversal loop | do-while, stop after revisiting head |
| Pointer updates needed | Exactly 2 (`last1->next`, `last2->next`) |
| Returned head | head1 (list 1 keeps its identity) |
| Time | O(n1 + n2) |
| Space | O(1) |
