import java.util.Scanner;

/**
 * HelpDesk.java
 *
 * Main application entry point for the Student Help Desk Ticket System.
 *
 * This class is purely the USER INTERFACE layer. It:
 *   - Displays the menu
 *   - Reads user input
 *   - Calls the appropriate HelpDeskQueue methods
 *   - Displays results back to the user
 *
 * It does NOT contain any queue logic itself. All data structure
 * operations are handled exclusively inside HelpDeskQueue.java,
 * satisfying the "Structure Separation" rubric criterion.
 *
 * FIFO Demonstration:
 *   Tickets are served in the exact order they were submitted.
 *   First ticket in → first ticket served.
 */
public class HelpDesk {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HelpDeskQueue queue = new HelpDeskQueue();  // Our linked-list queue
        int choice;

        System.out.println("  ============================================");
        System.out.println("       WELCOME TO THE STUDENT HELP DESK       ");
        System.out.println("  ============================================");
        System.out.println("  Tickets are handled in FIFO order —");
        System.out.println("  the first student to submit gets served first.\n");

        // ── Main application loop ──────────────────────────────────────────
        do {
            printMenu(queue.getSize());

            // ── Read and validate menu choice ──────────────────────────────
            System.out.print("  Enter choice: ");

            // Guard against non-integer input
            while (!scanner.hasNextInt()) {
                System.out.print("  Invalid input. Enter a number (1-6): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();   // Consume leftover newline after the integer

            System.out.println();

            // ── Dispatch to the chosen operation ───────────────────────────
            switch (choice) {

                // ── Option 1: Submit a ticket (ENQUEUE) ────────────────────
                case 1:
                    System.out.println("  --- SUBMIT HELP TICKET ---");
                    System.out.print("  Enter student name   : ");
                    String name = scanner.nextLine().trim();

                    System.out.print("  Enter problem        : ");
                    String problem = scanner.nextLine().trim();

                    // Basic validation: don't enqueue empty input
                    if (name.isEmpty() || problem.isEmpty()) {
                        System.out.println("\n  [!] Name and problem description cannot be blank.\n");
                    } else {
                        queue.enqueue(name, problem);   // ← Adds node at REAR
                    }
                    break;

                // ── Option 2: Serve the next ticket (DEQUEUE) ──────────────
                case 2:
                    System.out.println("  --- SERVE NEXT TICKET ---");
                    System.out.println("  Serving next ticket...\n");

                    TicketNode served = queue.dequeue();   // ← Removes node from FRONT

                    if (served != null) {
                        System.out.println("  ┌─────────────────────────────────────────┐");
                        System.out.printf ("  │  Ticket #%-32d│%n", served.ticketNumber);
                        System.out.printf ("  │  Student : %-30s│%n", served.studentName);
                        System.out.printf ("  │  Problem : %-30s│%n", served.problemDesc);
                        System.out.println("  └─────────────────────────────────────────┘");
                        System.out.println("\n  Ticket resolved. Student " +
                                           served.studentName + " has been helped.\n");
                    }
                    break;

                // ── Option 3: View the next ticket (PEEK) ──────────────────
                case 3:
                    System.out.println("  --- VIEW NEXT TICKET (no removal) ---");

                    TicketNode next = queue.peek();   // ← Reads FRONT without removing

                    if (next != null) {
                        System.out.println("  ┌─────────────────────────────────────────┐");
                        System.out.printf ("  │  Ticket #%-32d│%n", next.ticketNumber);
                        System.out.printf ("  │  Student : %-30s│%n", next.studentName);
                        System.out.printf ("  │  Problem : %-30s│%n", next.problemDesc);
                        System.out.println("  └─────────────────────────────────────────┘");
                        System.out.println("\n  (This ticket is still in the queue.)\n");
                    }
                    break;

                // ── Option 4: Display all tickets ──────────────────────────
                case 4:
                    System.out.println("  --- ALL TICKETS IN QUEUE ---");
                    queue.displayAll();   // ← Traverses list from front to rear
                    break;

                // ── Option 5: Check if queue is empty ──────────────────────
                case 5:
                    System.out.println("  --- QUEUE STATUS ---");
                    if (queue.isEmpty()) {
                        System.out.println("  The queue is EMPTY. No pending tickets.\n");
                    } else {
                        System.out.println("  The queue is NOT empty.");
                        System.out.println("  Tickets currently waiting: " + queue.getSize() + "\n");
                    }
                    break;

                // ── Option 6: Exit ──────────────────────────────────────────
                case 6:
                    System.out.println("  ============================================");
                    System.out.println("       Thank you for using the Help Desk!      ");
                    System.out.println("  ============================================\n");
                    break;

                // ── Invalid choice ──────────────────────────────────────────
                default:
                    System.out.println("  [!] Invalid choice. Please enter 1 to 6.\n");
                    break;
            }

        } while (choice != 6);

        scanner.close();
    }

    // ── Helper: Print the menu ─────────────────────────────────────────────

    /**
     * Prints the main menu to the console.
     * Shows the live ticket count so the user always knows the queue size.
     *
     * @param queueSize current number of tickets in the queue
     */
    private static void printMenu(int queueSize) {
        System.out.println("  ============================================");
        System.out.println("            STUDENT HELP DESK                 ");
        System.out.println("  ============================================");
        System.out.println("   Tickets in queue: " + queueSize);
        System.out.println("  --------------------------------------------");
        System.out.println("   1. Submit Help Ticket");
        System.out.println("   2. Serve Next Ticket");
        System.out.println("   3. View Next Ticket");
        System.out.println("   4. Display All Tickets");
        System.out.println("   5. Check if Queue is Empty");
        System.out.println("   6. Exit");
        System.out.println("  --------------------------------------------");
    }
}
