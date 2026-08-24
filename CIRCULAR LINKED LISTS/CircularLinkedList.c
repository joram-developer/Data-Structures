#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE
// ==============================

/*
    Same node as a single linked list — the "circular" part
    is not in the struct, it's in HOW WE LINK the nodes:
    the last node's next points back to head instead of NULL.

        ┌─────────────────────┐
        ↓                     │
      [10] → [20] → [30] ─────┘
*/

struct Node {
    int data;
    struct Node *next;
};


// ==============================
// CREATE A NEW NODE
// ==============================

/*
    Note: next starts as NULL here, but for a circular list
    a node is never left with next == NULL. Every creator of
    a node must immediately wire it into the ring (or make it
    point to itself). The NULL is just a safe starting value.
*/

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->next = NULL;
    return newNode;
}


// ======================================================
// INSERT AT THE END
// ======================================================

struct Node *insertAtEnd(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 1: The list is empty.  ★ THE SPECIAL CASE
        ─────────────────────────────────────────

            head → NULL

            We create the FIRST node of the ring. A ring with one
            node still has to be a ring: the node must point to
            ITSELF, otherwise its next would dangle and traversal
            could never detect "back home".

                ┌──────┐
                ↓      │
              [10] ────┘        newNode->next = newNode;

            Then it becomes the head.

        Compare with an SLL: there, the empty case was simply
        `head = newNode` and newNode->next stayed NULL.
        THE SELF-LOOP IS THE ONE THING THAT MAKES IT CIRCULAR.
    */

    if (head == NULL) {
        newNode->next = newNode;    // one-node ring points to itself
        head = newNode;
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 2: The list has nodes already.
        ─────────────────────────────────────────

            ┌→ [10] → [20] → [30] ─┐     insert 40 at end:

            walk until we stand on the LAST node.
            ⚠ Stop condition is `current->next != head`,
              NOT `!= NULL` — there is no NULL in a ring!

                current
                   ↓
            ┌→ [10] → [20] → [30] ─┐

        STEP 1: splice the new node in between last and head.

            current->next = newNode;    // [30].next → [40]
            newNode->next  = head;      // [40].next → [10]

        ORDER: either line first works here because both old values
        are recoverable ([30].next from current, head from head),
        but "wire the NEW node first" stays the safest habit.

        Result:

            ┌→ [10] → [20] → [30] → [40] ─┐
    */

    struct Node *current = head;

    while (current->next != head) {   // stop when next wraps home
        current = current->next;
    }

    newNode->next = head;             // STEP 1a: new node → first node
    current->next = newNode;          // STEP 1b: old last → new node

    return head;                      // head unchanged
}


// ======================================================
// INSERT AT THE BEGINNING
// ======================================================

struct Node *insertAtBeginning(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        CASE 1: Empty list → identical to insertAtEnd's CASE 1.
        One node, pointing to itself.
    */

    if (head == NULL) {
        newNode->next = newNode;    // self-loop makes it a ring
        head = newNode;
        return head;
    }


    /*
        CASE 2: Non-empty.

        ⚠ THE TRAP THAT DIFFERS FROM AN SLL:

        In an SLL, insert-at-beginning touches ONLY the new node
        and head. In a CIRCULAR list that's not enough — the LAST
        node also points at head, and after the insert it must
        point at the NEW head instead:

            before:  ┌→ [10] → [20] → [30] ─┐     [30].next = [10]
            after:   ┌→ [5] → [10] → [20] → [30] ┐  [30].next = [5]!

        So we must FIND THE LAST NODE even though we're inserting
        at the FRONT. That's the hidden cost of front-insertion
        in a circular list.

        STEPS (order matters!):

        STEP 1: find the last node (walk until next wraps home)

            last->next currently = head — leave it for now.

        STEP 2: wire the new node forward to the old head.

            newNode->next = head;       // [5].next → [10]

        STEP 3: redirect the last node to the NEW first node.

            last->next = newNode;       // [30].next → [5]

        STEP 4: move head.

            head = newNode;

        ⚠ Step 3 must run BEFORE step 4 changes what "head"
          means — otherwise the walk-to-last condition
          (current->next != head) compares against the wrong node
          mid-surgery. Doing steps in this order keeps every
          comparison valid at the moment it runs.
    */

    // STEP 1: find the last node (needed even for front-insert!)
    struct Node *last = head;
    while (last->next != head) {
        last = last->next;
    }

    newNode->next = head;           // STEP 2: new node → old first
    last->next = newNode;           // STEP 3: old last → new first
    head = newNode;                 // STEP 4: new node is the entry point

    return head;
}


// ======================================================
// TRAVERSE — do-while, NOT while!
// ======================================================

/*
    Why do-while?

    WRONG:  while (current != head) { ... }
            The loop STARTS on head, so the test is true
            immediately and NOTHING gets printed.

    RIGHT:  visit head FIRST (do-block), THEN ask
            "am I back home?" after moving. Exactly one
            full lap, each node printed exactly once.
*/

void traverse(struct Node *head) {

    if (head == NULL) {             // empty list guard
        printf("(empty)\n");
        return;
    }

    struct Node *current = head;

    do {
        printf("%d ", current->data);
        current = current->next;
    } while (current != head);      // stop AFTER revisiting head

    printf("\n");
}


// ======================================================
// COUNT NODES — same do-while pattern
// ======================================================

int countNodes(struct Node *head) {

    if (head == NULL) return 0;

    int count = 0;
    struct Node *current = head;

    do {
        count++;
        current = current->next;
    } while (current != head);

    return count;
}


// ======================================================
// FREE THE ENTIRE RING (C-only chore)
// ======================================================

/*
    Start from the SECOND node, free everything up to (not
    including) head, then free head LAST. Freeing head first
    would be fine too as long as we saved it — but this order
    keeps the "stop when current == head" test meaningful,
    since head remains intact while we compare against it.
*/

void freeCircular(struct Node *head) {

    if (head == NULL) return;

    struct Node *current = head->next;

    while (current != head) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    free(head);
}


// ======================================================
// MAIN
// ======================================================

int main() {

    struct Node *head = NULL;

    /*
        First insert exercises the SELF-LOOP case.
    */

    printf("After inserting 10 into empty list:\n  ");
    head = insertAtEnd(head, 10);
    traverse(head);                  // 10
    printf("  node count: %d\n", countNodes(head));   // 1

    /*
        Build up: 10 → then 20, 30 at the end.
    */

    printf("\nAfter inserting 20, 30 at end:\n  ");
    head = insertAtEnd(head, 20);
    head = insertAtEnd(head, 30);
    traverse(head);                  // 10 20 30
    printf("  node count: %d\n", countNodes(head));   // 3

    /*
        Front insertion — including the hidden walk-to-last.
    */

    printf("\nAfter inserting 5 at beginning:\n  ");
    head = insertAtBeginning(head, 5);
    traverse(head);                  // 5 10 20 30
    printf("  node count: %d\n", countNodes(head));   // 4

    /*
        Traverse twice more to show the ring is stable —
        every call does exactly one clean lap.
    */

    printf("\nSecond lap: ");
    traverse(head);                  // 5 10 20 30

    printf("Third lap:  ");
    traverse(head);                  // 5 10 20 30

    freeCircular(head);

    return 0;
}
