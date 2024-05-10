import java.util.Scanner;

import static java.lang.Thread.sleep;

public class UserVsMachine implements Game {
    @Override
    public void play() {
        Scanner scn = new Scanner(System.in);
        int[] h = new int[2];
//        System.out.println("set heuristic for you: "); //player 0
//        h[0] = scn.nextInt();
        System.out.print("set heuristic for computer: "); //player 1
        h[1] = scn.nextInt();
        System.out.print("set minimax depth: ");
        int depth = scn.nextInt();

        Node node = new Node();
        while (node.isGameOver() < 0) {
            node.printBoard();
            if(node.curPlayer == 0) {
                System.out.println("Your turn, enter move:");
                int x = scn.nextInt();
                node = node.getChild(x);
            }
            else {
                System.out.println("computer's turn...");
                node.heuristic = h[1];
                node.minimax(depth, -Node.INF, Node.INF);
                try {
                    sleep(2000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                int childIdx = node.getNextNode();
                System.out.println(childIdx);
                node = node.childs[childIdx];
            }
        }
        node.printBoard();
        System.out.println("Game over");
        System.out.println("Your point: " + node.board[0][6]);
        System.out.println("Computer point: " + node.board[1][6]);
    }
}
