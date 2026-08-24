# Explanation: Merging Two Doubly Linked Lists

---

## 1. What does "merging" mean here?

Merging two doubly linked lists means producing ONE doubly linked
list containing all nodes of both: all of list 1 first, then all of
list 2. It is concatenation that preserves BOTH link directions.

```
List 1:   NULL ← [10] ⇄ [20] ⇄ [30] → NULL      (head1)
List 2:   NULL ← [40] ⇄ [50] → NULL             (head2)

Merged:   NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] ⇄ [50] → NULL
```

(Note: this is CONCATENATION. If two lists are individually sorted
and a sorted result is required, that is the "merge two sorted lists"
algorithm — see the note at the end.)

---

## 2. Pseudocode

```
ALGORITHM MergeDoubly(head1, head2)
// Joins two non-circular doubly linked lists into one.
// Returns the head of the merged list.

    IF head1 = NULL THEN          // list 1 empty → result IS list 2
        RETURN head2
    IF head2 = NULL THEN          // list 2 empty → result IS list 1
        RETURN head1

    // STEP 1: walk to the last node of list 1
    last1 ← head1
    WHILE last1.next ≠ NULL DO
        last1 ← last1.next

    // STEP 2: connect the joint — in BOTH directions
    last1.next ← head2            // forward arrow across the seam
    head2.prev ← last1            // backward arrow across the seam

    RETURN head1
END
```

### The heart of it: ONE connection, TWO arrows

In an SLL merge you would write exactly one line:
`last1->next = head2`. In a DLL, every connection between adjacent
nodes is stored TWICE (once in each node). So the single new
connection at the "seam" between the lists needs both halves:

```
        ... [30]         [40] ...
              ↓ next          ↑ prev
   before:    NULL ✗     NULL ✗      ← both ends were dangling

   after:     [30].next = [40]   ✓
              [40].prev  = [30]  ✓
```

Forgetting `head2->prev = last1` is THE classic mistake. The forward
traversal would look fine, but any backward traversal from the tail
would stop dead at [40] (its prev says "nothing comes before me").
This is why the DLL version's verification step walks BACKWARD after
merging.

### Why do we only touch list 1's tail?

head2 keeps its identity as the start of the second segment; nothing
before head2 changes. And no node of either list except `last1` and
`head2` participates in the seam. Total rewiring work: O(1).

### Edge cases (both handled by the IFs)

| head1 | head2 | Result |
|---|---|---|
| NULL | NULL | NULL |
| NULL | list | list 2 |
| list | NULL | list 1 |
| list | list | one list, head1 first, head2's nodes appended |

---

## 3. C vs Java differences

Identical logic; only syntax and memory management differ:

```c
// C — note -> and the free() loop needed for teardown
struct Node *mergeDoubly(struct Node *head1, struct Node *head2) {
    if (head1 == NULL) return head2;
    if (head2 == NULL) return head1;

    struct Node *last1 = head1;
    while (last1->next != NULL) last1 = last1->next;

    last1->next = head2;
    head2->prev = last1;

    return head1;
}
```

```java
// Java — dot notation, GC handles teardown
static Node mergeDoubly(Node head1, Node head2) {
    if (head1 == null) return head2;
    if (head2 == null) return head1;

    Node last1 = head1;
    while (last1.next != null) last1 = last1.next;

    last1.next = head2;
    head2.prev = last1;

    return head1;
}
```

One C-only trap worth remembering: when freeing the merged list,
a plain `while (current != NULL)` walk works fine (this list IS
linear, unlike the circular case) — free each node as you pass it,
saving `next` BEFORE calling free.

---

## 4. Complexity

| Step | Cost |
|---|---|
| Walk to last node of list 1 | O(n1) |
| Rewire two arrows | O(1) |
| **Total time** | **O(n1 + n2)** — often stated as O(n1), since list 2 is never scanned |
| Extra space | O(1) — zero new nodes created |

(If a TAIL POINTER to list 1 were maintained with the list, the walk
disappears and merging becomes O(1). That optimization appears when
you wrap a list in a struct/class holding {head, tail}.)

---

## 5. Note on SORTED merging

Concatenation does not care about values. If your lecturer wants two
SORTED doubly lists merged into one sorted list, that is a different
algorithm (compare front nodes, splice the smaller, fix prev links as
you go). Ask if you need that variant implemented.

---

## Quick reference

| Aspect | Value |
|---|---|
| Find-last condition | `current->next != NULL` (normal linear walk) |
| Pointer updates needed | Exactly 2 (`last1->next`, `head2->prev`) |
| Classic bug | forgetting `head2->prev = last1` |
| Returned head | head1 |
| Time | O(n1 + n2) |
| Space | O(1) |
