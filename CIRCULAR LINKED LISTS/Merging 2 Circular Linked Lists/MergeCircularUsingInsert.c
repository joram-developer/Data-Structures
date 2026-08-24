#include <stdio.h>
#include <stdlib.h>


// ======================================================
// This is the SAME merge as MergeCircular.c in this folder —
// identical mergeCircular() function, same two-arrow logic.
//
// THE DIFFERENCE: instead of building the two lists from
// arrays with buildCircular(), we build them ONE NODE AT A
// TIME using insertAtEnd() — the way you'd construct lists
// in a lab test or an exam. See CircularLinkedList.c (one
// level up) for the full ground-up explanation of insertAtEnd;
// condensed comments appear here.
// ======================================================


// ==============================
// NODE STRUCTURE + CREATE
// ==============================

struct Node {
    int data;
    struct Node *next;
};

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->next = NULL;   // caller wires it into a ring immediately
    return newNode;
}


// ======================================================
// INSERT AT THE END — maintains the ring invariant:
// after every call, last node's next == head.
// ======================================================

struct Node *insertAtEnd(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        CASE 1: Empty list → one-node ring that points to ITSELF.

            ┌──────┐
            ↓      │
          [10] ────┘

        ★ The self-loop is what makes it circular. An SLL would
          leave newNode->next as NULL here; we can never allow
          next == NULL anywhere in a ring.
    */

    if (head == NULL) {
        newNode->next = newNode;
        head = newNode;
        return head;
    }


    /*
        CASE 2: Non-empty → walk until current is the LAST node.

            ⚠ Stop condition compares against HEAD, not NULL:

                while (current->next != head)

            then splice: new node points at head first,
            old last points at the new node.
    */

    struct Node *current = head;

    while (current->next != head) {
        current = current->next;
    }

    newNode->next = head;       // wire the NEW node first
    current->next = newNode;    // then close the ring through it

    return head;
}


// ======================================================
// TRAVERSE — do-while for exactly one full lap
// ======================================================

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
// Full step-by-step explanation lives in MergeCircular.c and
// in "EXPLANATION - Merging 2 Circular Linked Lists.md".
// Summary: find each ring's last node, rewire TWO arrows,
// return head1. O(n1 + n2) time, O(1) extra space.

struct Node *mergeCircular(struct Node *head1, struct Node *head2) {

    // Empty-list cases: result is simply the other list.
    if (head1 == NULL) return head2;
    if (head2 == NULL) return head1;

    // STEP 1: last node of ring 1 (next wraps to ITS OWN head)
    struct Node *last1 = head1;
    while (last1->next != head1) {
        last1 = last1->next;
    }

    // STEP 2: last node of ring 2
    struct Node *last2 = head2;
    while (last2->next != head2) {
        last2 = last2->next;
    }

    // STEP 3: the only two wrong arrows get fixed
    last1->next = head2;    // ARROW 1: ring 1 flows into list 2
    last2->next = head1;    // ARROW 2: list 2 closes onto head1

    return head1;           // merged ring starts where list 1 started
}


// ======================================================
// FREE A CIRCULAR LIST
// ======================================================

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
// MAIN — lists built node-by-node with insertAtEnd
// ======================================================

int main() {

    /*
        Build ring 1 by hand, one insertAtEnd call per value:

        first insert → empty case → self-loop:
            ┌──────┐
            ↓      │
          [10] ────┘

        second insert → walk stops at once ([10].next == head,
        so [10] is the last node), splice [20]:
            ┌→ [10] → [20] ─┐

        third insert → walk stops at [20], splice [30]:
            ┌→ [10] → [20] → [30] ─┐
    */

    struct Node *list1 = NULL;

    list1 = insertAtEnd(list1, 10);   // empty case → self-loop
    list1 = insertAtEnd(list1, 20);
    list1 = insertAtEnd(list1, 30);

    /*
        Build ring 2 the same way:

            ┌→ [40] → [50] ─┐
    */

    struct Node *list2 = NULL;

    list2 = insertAtEnd(list2, 40);   // empty case → self-loop
    list2 = insertAtEnd(list2, 50);

    printf("List 1 before merge: ");
    traverse(list1);             // 10 20 30

    printf("List 2 before merge: ");
    traverse(list2);             // 40 50

    /*
        Merge — exactly the same two-arrow surgery as before.
        HOW the rings were BUILT makes no difference to the
        merge function; all it needs is a valid head of each.
    */

    struct Node *merged = mergeCircular(list1, list2);

    printf("\nMerged list:         ");
    traverse(merged);            // 10 20 30 40 50

    printf("Traverse again:      ");
    traverse(merged);            // still one clean lap → truly circular

    freeCircular(merged);

    return 0;
}
