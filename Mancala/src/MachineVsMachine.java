import java.util.Scanner;

public class MachineVsMachine implements Game {
    @Override
    public void play() {
        Scanner scn = new Scanner(System.in);
        int[] h = new int[2];
        for(int i=0; i<2; i++) {
            System.out.print("set heuristic for player" + i + ": ");
            h[i] = scn.nextInt();
        }
        int[] cnt = new int[3];
        for(int i=0; i<3; i++) cnt[i] = 0;

        for(int i=0; i<100; i++) {
            Node node = new Node();
            while (node.isGameOver() < 0) {
                node.heuristic = h[node.curPlayer];
                node.minimax(8, -Node.INF, Node.INF);
                int childIdx = node.getNextNode();
                node = node.childs[childIdx];
            }
            System.out.println("Game: " + i);
            System.out.println("winner: " + node.getWinner());
            System.out.println("player0: " + node.board[0][6] + ", player1: " + node.board[1][6]);
            System.out.println();
            cnt[node.getWinner()]++;
        }
        System.out.println("Result:\nplayer0 wins: " + cnt[0] + ", player1 wins: " + cnt[1] + ", draw: " + cnt[2]);

    }
}
