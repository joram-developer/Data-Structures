#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE + HELPERS
// ==============================

struct Node {
    int data;
    struct Node *prev;
    struct Node *next;
};

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->prev = NULL;
    newNode->next = NULL;
    return newNode;
}

/*
    Build a doubly linked list from an array.
    Every connection is wired in BOTH directions as we go.
*/

struct Node *buildList(int values[], int size) {

    if (size == 0) return NULL;

    struct Node *head = createNode(values[0]);
    struct Node *current = head;

    for (int i = 1; i < size; i++) {
        current->next = createNode(values[i]);
        current->next->prev = current;   // back-link: new node → predecessor
        current = current->next;
    }

    return head;
}

void traverse(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }
    printf("\n");
}

/*
    Walk to the tail, then follow prev backwards.
    Used AFTER merging to prove the seam is correct
    in the backward direction too.
*/

void traverseBackward(struct Node *head) {

    if (head == NULL) { printf("(empty)\n"); return; }

    struct Node *current = head;

    while (current->next != NULL) {      // walk forward to the tail
        current = current->next;
    }

    while (current != NULL) {            // walk back via prev
        printf("%d ", current->data);
        current = current->prev;
    }

    printf("\n");
}


// ======================================================
// MERGE TWO DOUBLY LINKED LISTS
// ======================================================

struct Node *mergeDoubly(struct Node *head1, struct Node *head2) {

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
        STEP 1: Walk to the LAST node of list 1.
        ─────────────────────────────────────────

                head1
                  ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL     ← this list IS linear,
                                                so the normal walk works:
                                                stop when next == NULL.

                last1
                  ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL
    */

    struct Node *last1 = head1;
    while (last1->next != NULL) {
        last1 = last1->next;
    }


    /*
        ─────────────────────────────────────────
        STEP 2: Connect the seam — in BOTH directions.
        ─────────────────────────────────────────

        Before:

            NULL ← [10] ⇄ [20] ⇄ [30] → NULL   ← [30].next dangles
            NULL ← [40] ⇄ [50] → NULL          ← [40].prev dangles

        After:

            NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] ⇄ [50] → NULL


        ARROW 1 (forward):  last1->next = head2;
                            [30].next now points at [40].

        ARROW 2 (backward): head2->prev = last1;
                            [40].prev now points at [30].

        ⚠ FORGETTING ARROW 2 IS THE CLASSIC BUG.
        The forward traversal would look perfect, but any backward
        traversal from the tail would die at [40], because its prev
        still says "nothing comes before me". In an SLL merge there
        is only one arrow — the DLL demands both. This is exactly
        why main() verifies with a BACKWARD traverse.

        No other node changes: every node keeps its own links except
        the two at the seam ([30] and [40]). O(1) rewiring after the
        O(n1) walk to find last1.
    */

    last1->next = head2;           // ARROW 1: forward across the seam
    head2->prev = last1;           // ARROW 2: backward across the seam


    /*
        Return head1 — the merged list starts where list 1 started.
        (list 2's nodes are all still reachable, chained behind it.)
    */

    return head1;
}


// ======================================================
// MAIN
// ======================================================

int main() {

    /*
        Build two independent lists:

            list 1:  NULL ← [10] ⇄ [20] ⇄ [30] → NULL
            list 2:  NULL ← [40] ⇄ [50] → NULL
    */

    int v1[] = {10, 20, 30};
    int v2[] = {40, 50};

    struct Node *list1 = buildList(v1, 3);
    struct Node *list2 = buildList(v2, 2);

    printf("List 1 before merge: ");
    traverse(list1);             // 10 20 30

    printf("List 2 before merge: ");
    traverse(list2);             // 40 50

    /*
        Merge them into one doubly linked list starting at head1.
    */

    struct Node *merged = mergeDoubly(list1, list2);

    printf("\nMerged (forward):    ");
    traverse(merged);            // 10 20 30 40 50

    /*
        THE PROOF: walk backward from the tail.
        If arrow 2 (head2->prev = last1) were missing, this would
        print only "50" — the backward chain would break at the seam.
    */

    printf("Merged (backward):   ");
    traverseBackward(merged);    // 50 40 30 20 10

    /*
        Freeing is a plain linear walk — save next BEFORE free().
    */

    struct Node *current = merged;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
