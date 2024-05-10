import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;

public class Node {
    public static final int INF = 1000_000_000;
    int[][] board;
    int curPlayer;
    int freeMoveAvailable;
    int nextPlayer;
    int value;
    int stoneCapturedInChild;
    int stoneCaptured;
    int heuristic;
    Node[] childs;
    int w[] = {0, 5, 2, 3, 4};

    public Node() {
        board = new int[2][7];
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 6; j++) board[i][j] = 4;
        }
        curPlayer = 0;
        freeMoveAvailable = 0;
        childs = new Node[6];
    }
    public Node(int[][] board, int curPlayer, int freeMoveAvailable, int stoneCaptured) {
        this.board = board;
        this.curPlayer = curPlayer;
        this.freeMoveAvailable = freeMoveAvailable;
        this.stoneCaptured = stoneCaptured;
        childs = new Node[6];
    }

    void printBoard() {
        System.out.print("\t ");
        for(int i=0; i<6; i++) System.out.print((5-i) + " ");
        System.out.print("\n\t ");
        for(int i=0; i<6; i++) System.out.print(board[1][5-i] + " ");
        System.out.print("\n" + board[1][6] + "\t\t\t\t\t" + board[0][6] + "\n\t ");
        for(int i=0; i<6; i++) System.out.print(board[0][i] + " ");
        System.out.print("\n\t ");
        for(int i=0; i<6; i++) System.out.print(i + " ");
        System.out.println();
    }
    int getWinner() {
        if(isGameOver() < 0) { //return kar ta all 0
            System.out.println("game not over yet");
            exit(0);
        }
        int other = (isGameOver() ^ 1);
        for(int i=0; i<6; i++) {
            board[other][6] += board[other][i];
            board[other][i] = 0;
        }
        if(board[0][6] > board[1][6]) return 0;
        if(board[1][6] > board[0][6]) return 1;
        return 2;
    }
    int max(int a, int b) {
        return a > b? a : b;
    }
    int min(int a, int b) {
        return a < b? a : b;
    }
    int minimax(int depth, int alpha, int beta) { //will determine value of this node and next move
        if(isGameOver() > -1) {
            int other = (isGameOver() ^ 1);
            for(int i=0; i<6; i++) {
                board[other][6] += board[other][i];
                board[other][i] = 0;
            }
            return value = getHeuristic();
        }
        if(depth == 0) {
            return value = getHeuristic();
        }
        value = curPlayer == 0? -INF : INF; //value of this node
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<6; i++) list.add(i);
        Collections.shuffle(list);
        for(int i: list) { //use randomized here??
            if(board[curPlayer][i] == 0) continue;
            childs[i] = getChild(i);
            childs[i].heuristic = this.heuristic;
            int childVal = childs[i].minimax(depth-1, alpha, beta);
            if(curPlayer == 0) {
                value = max(value, childVal);
                alpha = max(alpha, childVal);
            }
            else {
                value = min(value, childVal);
                beta = min(beta, childVal);
            }
            if(alpha >= beta) break; //pruning
        }
        return value;
    }
    Node getChild(int i) {
        int[][] newBoard = nextBoard(curPlayer, i);
        int freeTurnInNext = curPlayer == nextPlayer ? 1 : 0;
        return new Node(newBoard, nextPlayer, freeTurnInNext, stoneCapturedInChild);
    }
    int getNextNode() {
        List<Integer> indexes = new ArrayList<>();
        for(int i=0; i<6; i++) {
            if(childs[i] == null) continue;
            if(childs[i].value == this.value) {
                indexes.add(i);
            }
        }
        Collections.shuffle(indexes);
        return indexes.get(0);
    }
    int getHeuristic() {
        if(heuristic == 0) {
            System.out.println("Please set heuristic first");
            exit(0);
        }
        if(heuristic == 1) return h1();
        if(heuristic == 2) return h2();
        if(heuristic == 3) return h3();
        assert heuristic == 4;
        return h4();
    }
    int h1() {
        return board[0][6] - board[1][6];
    }
    int h2() {
        int ret = w[1] * h1();
        int stoneMySide = 0, stoneOpponentSide = 0;
        for(int i=0; i<6; i++) {
            stoneMySide += board[0][i];
            stoneOpponentSide += board[1][i];
        }
        ret += w[2] * (stoneMySide - stoneOpponentSide);
        return ret;
    }
    int h3() {
        return h2() + w[3] * freeMoveAvailable;
    }
    int h4() {
        return h3() + w[4] * stoneCaptured;
    }
    int isGameOver() {
        for(int i=0; i<2; i++) {
            int ok = 0;
            for(int j=0; j<6; j++) ok += board[i][j];
            if(ok == 0) return i;
        }
        return -1;
    }
    int[][] nextBoard(int I, int J) {
        int[][] newBoard = new int[2][7];
        for(int i=0; i<2; i++)
            for(int j=0; j<7; j++) newBoard[i][j] = board[i][j];

        int curI = I, curJ = J+1, curAmount = board[I][J];
        newBoard[I][J] = 0;
        while (true) {
            if(curJ == 6 && curI != I) { //opponent storage, move to my first cell
                curI ^= 1;
                curJ = 0;
            }
            newBoard[curI][curJ]++;
            curAmount--;
            if(curAmount == 0) {
                if(curI == I && curJ == 6) nextPlayer = curPlayer; //free move
                else nextPlayer = (curPlayer ^ 1);

                if(curI == I && curJ < 6 && newBoard[I][curJ] == 1 && newBoard[(I ^ 1)][5 - curJ] > 0) { //capture
                    stoneCapturedInChild = newBoard[I][curJ] + newBoard[(I ^ 1)][5 - curJ];
                    newBoard[I][6] += newBoard[I][curJ] + newBoard[(I ^ 1)][5 - curJ];
                    newBoard[I][curJ] = newBoard[(I ^ 1)][5 - curJ] = 0;
                }
                else stoneCapturedInChild = 0;
                break;
            }
            if(curJ == 6 && curI == I) { //my storage, move to opponent first cell
                curI ^= 1;
                curJ = 0;
            }
            else curJ++;
        }
        return newBoard;
    }
}
