public class InsertAtStart {

    // ==============================
    // NODE CLASS
    // ==============================

    /*
        In C, a node was a struct:

            struct Node {
                int data;
                struct Node *next;
            };

        In Java, we use a class instead.
        There are no pointers — Java uses references.
        A reference works like a pointer, but Java
        manages memory for you automatically.
    */

    static class Node {
        int data;
        Node next;      // "next" is a reference to the next Node
                        // It holds null instead of NULL when there is no next node
    }


    // ==============================
    // CREATE A NEW NODE
    // ==============================

    /*
        In C:
            struct Node *newNode = malloc(sizeof(struct Node));
            newNode->data = data;
            newNode->next = NULL;

        In Java:
            Node newNode = new Node();
            newNode.data = data;
            newNode.next = null;

        Java's "new" allocates memory automatically.
        We don't need malloc() or free().
        The garbage collector cleans up unused nodes for us.

        Also notice:
            C uses  ->  to access fields through a pointer:   newNode->data
            Java uses . to access fields through a reference: newNode.data
    */

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;
        return newNode;
    }


    // ==============================
    // TRAVERSE THE LIST
    // ==============================

    static void traverse(Node head) {
        Node current = head;

        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }

        System.out.println();
    }


    // ==============================
    // INSERT AT THE BEGINNING
    // ==============================

    /*
        In C, this function returned the new head because
        you cannot change the caller's pointer directly
        without using a pointer-to-pointer (**head).

        In Java, the same pattern applies.
        Java passes references by value, meaning the function
        receives a copy of the reference.

        If we wrote:
            head = newNode;    // inside the function

        ...we are only changing our LOCAL copy of head.
        The head variable in main() would be unchanged.

        So just like C, we return the new head and the
        caller does:
            head = insertAtBeginning(head, 5);
    */

    static Node insertAtBeginning(Node head, int data) {

        Node newNode = createNode(data);

        /*
            Why do we follow this order?

            Suppose the list is:

            head
             ↓
            [10] → [20] → [30] → null

            and newNode contains:

            [5] → null


            STEP 1:
            Make the new node point to the current head:

                newNode.next = head;

            Now:

            newNode
               ↓
              [5] → [10] → [20] → [30] → null
                      ↑
                     head


            STEP 2:
            Make head point to the new node:

                head = newNode;

            Now:

                 head
                  ↓
                 [5] → [10] → [20] → [30] → null


            We do STEP 1 first because head currently holds
            the reference to [10]. We need to copy that
            reference into newNode.next BEFORE we change head.

            If we did STEP 2 first:
                head = newNode;        // head now points to [5]
                newNode.next = head;   // newNode.next = [5]  ← [5] points to itself!
                                       // We lost [10] → [20] → [30] forever!
        */

        newNode.next = head;   // STEP 1: link new node to the existing list
        head = newNode;        // STEP 2: new node becomes the new head

        return head;           // return the new head so main() can update its variable
    }


    // ==============================
    // MAIN
    // ==============================

    public static void main(String[] args) {

        /*
            Create the initial list manually:

            10 → 20 → 30 → null
        */

        Node head = createNode(10);
        Node node2 = createNode(20);
        Node node3 = createNode(30);

        // Connect the nodes
        head.next = node2;
        node2.next = node3;

        System.out.print("Before insertion: ");
        traverse(head);

        /*
            Insert 5 at the beginning.

            Before:   10 → 20 → 30
                       ↓
            After:   5 → 10 → 20 → 30
        */

        head = insertAtBeginning(head, 5);

        System.out.print("After insertion:  ");
        traverse(head);

        /*
            No need to free memory in Java.
            When head goes out of scope, the garbage
            collector automatically reclaims all nodes.
        */
    }
}
