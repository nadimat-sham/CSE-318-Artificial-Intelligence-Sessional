import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scn = new Scanner(System.in);
        while (true) {
            System.out.println("1. Play UserVsMachine");
            System.out.println("2. Play MachineVsMachine");
            System.out.println("3. Play UserVsMachine(Autoplay)");
            System.out.println("4. Exit");
            int choice = scn.nextInt();
            if(choice == 4) break;
            Game game;
            if(choice == 1) {
                game = new UserVsMachine();
            } else if(choice == 2){
                game = new MachineVsMachine();
            } else {
                game = new UserVsMachineAutoplay();
            }
            game.play();
        }
    }
}