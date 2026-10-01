/**
 * TicketNode.java
 *
 * Represents a single node in the linked list queue.
 * Each node holds one student's support ticket and a
 * reference (next) pointing to the node behind it in the queue.
 *
 * Structure of one node:
 *
 *   +------------------+------+
 *   | studentName      | next |----> (next node, or null if rear)
 *   | problemDesc      |      |
 *   | ticketNumber     |      |
 *   +------------------+------+
 */
public class TicketNode {

    // ── Data fields ──────────────────────────────────────────
    String studentName;   // Name of the student who submitted the ticket
    String problemDesc;   // Description of the student's problem
    int    ticketNumber;  // Auto-assigned ticket ID (1, 2, 3 …)

    // ── Pointer field ────────────────────────────────────────
    TicketNode next;      // Reference to the next node in the queue
                          // null when this node is at the REAR

    /**
     * Constructor – creates a new ticket node.
     *
     * @param studentName  the student's name
     * @param problemDesc  description of the problem
     * @param ticketNumber the unique ticket number assigned at submission
     */
    TicketNode(String studentName, String problemDesc, int ticketNumber) {
        this.studentName  = studentName;
        this.problemDesc  = problemDesc;
        this.ticketNumber = ticketNumber;
        this.next         = null;   // A brand-new node has no successor yet
    }
}
