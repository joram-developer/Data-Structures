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

void traverse(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }
    printf("\n");
}

void traverseBackward(struct Node *head) {
    if (head == NULL) { printf("(empty)\n"); return; }
    struct Node *current = head;
    while (current->next != NULL) current = current->next;
    while (current != NULL) { printf("%d ", current->data); current = current->prev; }
    printf("\n");
}


// ======================================================
// 1. DELETE AT THE BEGINNING
// ======================================================

/*
    Core pointer ideas:

        temp = head;                 // save for free()
        head = head->next;           // second node becomes head
        IF head != NULL:             ⚠ guard (single-node list)
            head->prev = NULL;       // restore boundary rule 1
        free(temp);

    The guarded line is the DLL's extra step compared to an SLL:
    the new head's prev must stop pointing at the freed node.
*/

struct Node *deleteAtBeginning(struct Node *head) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    struct Node *temp = head;      // save the old head
    head = head->next;             // advance head

    if (head != NULL) {            // guard against single-node list
        head->prev = NULL;         // nothing before the new first node
    }

    free(temp);

    return head;
}


// ======================================================
// 2. DELETE AT THE END
// ======================================================

/*
    Core pointer ideas:

        walk until current is last, then:

            current->prev->next = NULL;   // cut from the SURVIVING side
            free(current);

        ⭐ No `previous` walker needed — current->prev IS it.
           The doomed node's own stale prev disappears with free().
*/

struct Node *deleteAtEnd(struct Node *head) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    if (head->next == NULL) {      // single node IS the last node
        free(head);
        head = NULL;
        return head;
    }

    struct Node *current = head;

    while (current->next != NULL) {   // walk to the last node
        current = current->next;
    }

    current->prev->next = NULL;    // cut forward link on survivor
    free(current);                 // release the old tail

    return head;
}


// ======================================================
// 3. DELETE AFTER A TARGET
// ======================================================

/*
    Core pointer ideas — cut the connection in BOTH directions:

        toDelete = current->next;          // save FIRST
        current->next = toDelete->next;    // forward cut
        IF toDelete->next != NULL:         ⚠ guard (deleted last node)
            toDelete->next->prev = current;// backward cut
        free(toDelete);
*/

struct Node *deleteAfter(struct Node *head, int target) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    struct Node *current = head;

    while (current != NULL && current->data != target) {
        current = current->next;
    }

    if (current == NULL) {
        printf("Target %d was not found.\n", target);
        return head;
    }

    if (current->next == NULL) {
        printf("No node after %d. Nothing to delete.\n", target);
        return head;
    }

    struct Node *toDelete = current->next;   // STEP 1: save it

    current->next = toDelete->next;          // STEP 2: forward cut

    if (toDelete->next != NULL) {            // STEP 3: guarded backward cut
        toDelete->next->prev = current;      //         survivor looks back
    }

    free(toDelete);                          // STEP 4: release it

    return head;
}


// ======================================================
// MAIN — all three operations in sequence
// ======================================================

int main() {

    /*
        Build:  NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] ⇄ [50] → NULL
    */

    struct Node *nodes[5];
    for (int i = 0; i < 5; i++) nodes[i] = createNode((i + 1) * 10);
    for (int i = 0; i < 4; i++) {
        nodes[i]->next = nodes[i + 1];
        nodes[i + 1]->prev = nodes[i];
    }

    struct Node *head = nodes[0];

    printf("Original list (forward):     ");
    traverse(head);              // 10 20 30 40 50


    // ──────────────────────────────────────────────────
    // 1. DELETE AFTER 30 → removes 40
    // ──────────────────────────────────────────────────

    head = deleteAfter(head, 30);

    printf("After deleteAfter(30):       ");
    traverse(head);              // 10 20 30 50


    // ──────────────────────────────────────────────────
    // 2. DELETE AT START → removes 10
    // ──────────────────────────────────────────────────

    head = deleteAtBeginning(head);

    printf("After deleteAtBeginning:     ");
    traverse(head);              // 20 30 50


    // ──────────────────────────────────────────────────
    // 3. DELETE AT END → removes 50
    // ──────────────────────────────────────────────────

    head = deleteAtEnd(head);

    printf("After deleteAtEnd:           ");
    traverse(head);              // 20 30

    /*
        Final proof that all remaining prev links are correct.
        If any backward link were broken by a deletion,
        this walk would print wrong values or stop early.
    */

    printf("Final list (backward):       ");
    traverseBackward(head);      // 30 20


    // ──────────────────────────────────────────────────
    // EDGE CASES
    // ──────────────────────────────────────────────────

    head = deleteAfter(head, 30);      // nothing after last node
    head = deleteAfter(head, 99);      // target not found

    head = deleteAtEnd(head);          // removes 30
    head = deleteAtEnd(head);          // removes 20 (single-node case)
    head = deleteAtEnd(head);          // "List is empty."

    return 0;
}
