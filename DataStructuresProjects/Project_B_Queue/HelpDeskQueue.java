/**
 * HelpDeskQueue.java
 *
 * Implements a Queue using a Singly Linked List — strictly following
 * the project brief's requirement:
 *
 *   "Maintain a head (front) and a tail (rear) pointer.
 *    enqueue must add new nodes at the TAIL.
 *    dequeue must remove nodes from the HEAD."
 *
 * Visual model after enqueuing Phoebe → Sarah → John:
 *
 *   front                           rear
 *    ↓                               ↓
 *  [Phoebe|next] --> [Sarah|next] --> [John|null]
 *
 * Operations provided:
 *   enqueue()  – add a ticket at the rear
 *   dequeue()  – remove and return the ticket at the front
 *   peek()     – view the front ticket without removing it
 *   displayAll() – print every ticket currently in the queue
 *   isEmpty()  – check whether the queue has no tickets
 */
public class HelpDeskQueue {

    // ── Internal state ───────────────────────────────────────────────────────

    private TicketNode front;        // Points to the FIRST node (head) — served next
    private TicketNode rear;         // Points to the LAST  node (tail) — most recently added
    private int        size;         // Number of tickets currently in the queue
    private int        ticketCounter; // Auto-increments to assign unique ticket numbers

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Creates an empty queue.
     * Both front and rear start as null because there are no nodes yet.
     */
    public HelpDeskQueue() {
        front         = null;
        rear          = null;
        size          = 0;
        ticketCounter = 1;       // First ticket will be #1
    }

    // ── Core Operations ──────────────────────────────────────────────────────

    /**
     * ENQUEUE – Add a new ticket at the REAR of the queue.
     *
     * Step-by-step mechanics:
     *   1. Create a brand-new TicketNode with the given data.
     *   2. If the queue is currently EMPTY:
     *        Both front and rear must point to this single new node,
     *        because it is simultaneously the first AND last element.
     *   3. If the queue already has nodes:
     *        a. Attach the new node after the current rear:
     *               rear.next = newNode
     *        b. Move the rear pointer forward to the new node:
     *               rear = newNode
     *        The front pointer is NOT touched — the order of existing
     *        nodes does not change.
     *
     * Before (Phoebe → Sarah):
     *   front → [Phoebe] → [Sarah] ← rear
     *
     * After enqueue(John):
     *   front → [Phoebe] → [Sarah] → [John] ← rear
     *
     * @param studentName the student's name
     * @param problemDesc a description of the student's problem
     */
    public void enqueue(String studentName, String problemDesc) {

        // Step 1 – create the new node
        TicketNode newNode = new TicketNode(studentName, problemDesc, ticketCounter++);

        if (isEmpty()) {
            // Step 2 – queue was empty: new node is both front and rear
            front = newNode;
            rear  = newNode;
        } else {
            // Step 3a – link new node onto the end of the current last node
            rear.next = newNode;
            // Step 3b – advance the rear pointer to the new last node
            rear      = newNode;
        }

        size++;
        System.out.println("\n  Ticket #" + newNode.ticketNumber +
                           " submitted successfully for " + studentName + ".\n");
    }

    /**
     * DEQUEUE – Remove and return the ticket at the FRONT of the queue.
     *
     * Step-by-step mechanics:
     *   1. Check isEmpty() first. If empty, print a warning and return null
     *      (never crash on an empty queue — Edge Case Robustness criterion).
     *   2. Save a reference to the current front node so we can return its data.
     *   3. Move the front pointer one step forward:
     *          front = front.next
     *      The old front node is now unreferenced — Java's garbage collector
     *      will reclaim its memory automatically.
     *   4. Special case: if front is now null, the queue just became empty,
     *      so rear must also be set to null (both pointers must agree).
     *
     * Before (Phoebe → Sarah → John):
     *   front → [Phoebe] → [Sarah] → [John] ← rear
     *
     * After dequeue():
     *   front → [Sarah] → [John] ← rear
     *   (Phoebe node is returned and detached)
     *
     * @return the TicketNode that was at the front, or null if empty
     */
    public TicketNode dequeue() {

        // Step 1 – Edge case: queue is empty
        if (isEmpty()) {
            System.out.println("\n  [!] Queue is empty. No tickets to serve.\n");
            return null;
        }

        // Step 2 – Save the front node before moving the pointer
        TicketNode servedNode = front;

        // Step 3 – Advance front to the next node in the chain
        front = front.next;

        // Step 4 – If the queue is now empty, rear must also become null
        if (front == null) {
            rear = null;
        }

        size--;
        return servedNode;   // Return ticket data to the caller (main menu)
    }

    /**
     * PEEK – View the front ticket WITHOUT removing it.
     *
     * Simply reads front's data without moving any pointer.
     * The queue state is completely unchanged after this call.
     *
     * @return the TicketNode at the front, or null if empty
     */
    public TicketNode peek() {

        if (isEmpty()) {
            System.out.println("\n  [!] Queue is empty. No tickets waiting.\n");
            return null;
        }
        return front;
    }

    /**
     * DISPLAY ALL – Traverse the linked list and print every ticket.
     *
     * Uses a temporary traversal pointer (current) starting at front.
     * The front and rear pointers are NEVER moved — this is read-only.
     *
     * Traversal logic:
     *   current = front
     *   while (current != null):
     *       print current's data
     *       current = current.next     ← follow the link to the next node
     */
    public void displayAll() {

        if (isEmpty()) {
            System.out.println("\n  [!] Queue is empty. No tickets to display.\n");
            return;
        }

        System.out.println("\n  -----------------------------------------------");
        System.out.println("   CURRENT TICKET QUEUE  (" + size + " ticket(s) waiting)");
        System.out.println("  -----------------------------------------------");
        System.out.println("   FRONT");

        TicketNode current = front;   // Start traversal at the head
        int position = 1;

        while (current != null) {
            System.out.println("    " + position + ". [Ticket #" + current.ticketNumber + "]"
                    + "  Student : " + current.studentName);
            System.out.println("       " + "Problem  : " + current.problemDesc);

            if (current.next != null) {
                System.out.println("         ↓");       // Visual arrow showing linked chain
            }

            current = current.next;   // Move pointer forward to the next node
            position++;
        }

        System.out.println("   REAR");
        System.out.println("  -----------------------------------------------\n");
    }

    /**
     * IS EMPTY – Returns true when there are no nodes in the queue.
     *
     * Checking (front == null) is sufficient because:
     *   - enqueue always sets front when adding to an empty queue.
     *   - dequeue always sets front = null when the last node is removed.
     *
     * @return true if the queue contains no tickets
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * SIZE – Returns the current number of tickets in the queue.
     *
     * @return number of tickets waiting
     */
    public int getSize() {
        return size;
    }
}
