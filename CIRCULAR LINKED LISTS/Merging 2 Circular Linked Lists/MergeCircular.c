#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE + HELPERS
// ==============================

struct Node {
    int data;
    struct Node *next;
};

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->next = NULL;   // caller closes the circle
    return newNode;
}

/*
    Build a circular list from an array of values.

    Walk forward linking nodes, then CLOSE THE RING:
    the last node's next points back to head.
*/

struct Node *buildCircular(int values[], int size) {

    if (size == 0) return NULL;

    struct Node *head = createNode(values[0]);
    struct Node *current = head;

    for (int i = 1; i < size; i++) {
        current->next = createNode(values[i]);
        current = current->next;
    }

    current->next = head;   // ← close the ring

    return head;
}

/*
    Traversal MUST use do-while, not while!

        while (current != head)   ← WRONG: skips head entirely,
                                    because the loop starts ON head
                                    and the test is true immediately.

    A do-while visits head FIRST, then checks "am I back home?"
    after moving. That gives exactly one visit per node.
*/

void traverse(struct Node *head) {

    if (head == NULL) {
        printf("(empty)\n");
        return;
    }

    struct Node *current = head;

    do {
        printf("%d ", current->data);
        current = current->next;
    } while (current != head);

    printf("\n");
}


// ======================================================
// MERGE TWO CIRCULAR LINKED LISTS
// ======================================================

struct Node *mergeCircular(struct Node *head1, struct Node *head2) {

    /*
        ─────────────────────────────────────────
        CASE 1: One of the lists is empty.
        ─────────────────────────────────────────

            The merged result is simply the other list.
            (If BOTH are empty, head2 is NULL → returns NULL. ✓)
    */

    if (head1 == NULL) return head2;
    if (head2 == NULL) return head1;


    /*
        ─────────────────────────────────────────
        STEP 1: Find the LAST node of list 1.
        ─────────────────────────────────────────

                ┌→ [10] → [20] → [30] ─┐
                └────── head1 ─────────┘

            ⚠ THE KEY CONDITION: `last1->next != head1`
              (compare against HEAD — there IS no NULL in a ring!)

            When last1 = [30]: last1->next == head1 → stop.
            [30] is the node whose next leads back home,
            i.e. the last node.

            Walking with `current != head1` would be wrong:
            it stops as soon as we come BACK to the start,
            which happens only after a full lap — and it
            identifies head itself as "last".
    */

    struct Node *last1 = head1;
    while (last1->next != head1) {     // stop when next wraps to ITS OWN head
        last1 = last1->next;
    }


    /*
        ─────────────────────────────────────────
        STEP 2: Find the LAST node of list 2.
        ─────────────────────────────────────────

            Same idea, comparing against head2.
    */

    struct Node *last2 = head2;
    while (last2->next != head2) {
        last2 = last2->next;
    }


    /*
        ─────────────────────────────────────────
        STEP 3: Rewire exactly TWO arrows.
        ─────────────────────────────────────────

        Before:

            ring 1:   ┌→ [10] → [20] → [30] ─┐      [30].next = [10] ✗
                      └───────────────────────┘
            ring 2:   ┌→ [40] → [50] ─┐             [50].next = [40] ✗
                      └───────────────┘

        After:

            merged:   ┌→ [10] → [20] → [30] → [40] → [50] ─┐
                      └─────────────────────────────────────┘

        ARROW 1:  last1->next = head2;
                  [30].next now points at [40].
                  Ring 1 opens up and flows into list 2.

        ARROW 2:  last2->next = head1;
                  [50].next now points at [10].
                  List 2's end closes onto list 1's start.

        These two arrows were the ONLY ones that were wrong for
        the merged configuration — every other node keeps its
        existing next pointer untouched. That is why merging costs
        just O(1) rewiring after two O(n) walks.

        Boundary check afterwards:
            every node reachable from head1, and the walk returns
            to head1 exactly once ✓ (traverse() will prove it).
    */

    last1->next = head2;           // ARROW 1: ring 1 flows into list 2
    last2->next = head1;           // ARROW 2: list 2 closes onto head1


    /*
        Return head1 — the merged ring starts where list 1 started.
        (We could equally return any node; head is just convention.)
    */

    return head1;
}


// ======================================================
// FREE A CIRCULAR LIST (C-specific chore)
// ======================================================

/*
    Freeing needs the same do-while care as traversal:
    free nodes until we would revisit head, then free head LAST
    (freeing it earlier would make the comparison unsafe).
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

    /*
        Build two independent rings:

            list 1:  ┌→ [10] → [20] → [30] ─┐
                     └──────────────────────┘
            list 2:  ┌→ [40] → [50] ─┐
                     └───────────────┘
    */

    int v1[] = {10, 20, 30};
    int v2[] = {40, 50};

    struct Node *list1 = buildCircular(v1, 3);
    struct Node *list2 = buildCircular(v2, 2);

    printf("List 1 before merge: ");
    traverse(list1);             // 10 20 30

    printf("List 2 before merge: ");
    traverse(list2);             // 40 50

    /*
        Merge them into one ring starting at list1's head.
    */

    struct Node *merged = mergeCircular(list1, list2);

    printf("\nMerged list:         ");
    traverse(merged);            // 10 20 30 40 50

    /*
        Proof it is truly circular: traverse() above STOPPED by
        itself. In a broken ring it would either loop forever or
        print only part of the list.
    */

    printf("Traverse again:      ");
    traverse(merged);            // 10 20 30 40 50  (still one full lap)

    freeCircular(merged);

    return 0;
}
