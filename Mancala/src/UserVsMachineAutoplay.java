import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class UserVsMachineAutoplay implements Game{
    public static int getRand() {
        return (int) (Math.random()*(5 - 0 + 1)+0);
    }
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
        int[] cnt = new int[3];
        for(int i=0; i<100; i++) {
            Node node = new Node();
            while (node.isGameOver() < 0) {
//                node.printBoard();
                if(node.curPlayer == 0) {
                    int x;
                    while (true) {
                        x = getRand();
                        if(node.board[node.curPlayer][x] != 0) break;
                    }
                    node = node.getChild(x);
                }
                else {
//                    System.out.println("computer's turn...");
                    node.heuristic = h[1];
                    node.minimax(depth, -Node.INF, Node.INF);
//                    try {
//                        sleep(2000);
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
                    int childIdx = node.getNextNode();
//                    System.out.println(childIdx);
                    node = node.childs[childIdx];
                }
            }
            cnt[node.getWinner()]++;
            System.out.println("Game: " + i);
            System.out.println("winner: " + node.getWinner());
            System.out.println("player0: " + node.board[0][6] + ", player1: " + node.board[1][6]);

            System.out.println();
        }
        System.out.println("Results:");
        System.out.println("User win: " + cnt[0] + ", computer win: " + cnt[1] + ", draw: " + cnt[2]);
    }
}
