package Model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TestGame {
    public static void main(String[] args) {
        //establish game files
        File gameFile = new File("src/main/java/TxtFiles/Games.txt");
        File bucketFile = new File("src/main/java/TxtFiles/Buckets.txt");
        File wordFile = new File("src/main/java/TxtFiles/Words.txt");

        //create game system
        //GameSystem gameSystem = new GameSystem(gameFile, bucketFile, wordFile);

        //make buckets for the game 1 buckets in BucketDB.txt
        Game game = new Game(1,"Grammar",bucketFile, wordFile);
        Round round = new Round(game);

        Scanner scanner = new Scanner(System.in);

        String nextAction = "0";
        List<Bucket> buckets = game.getBuckets();
        while(!nextAction.isEmpty()) {
            System.out.print("Current Score:" + round.getScore()+"\n");
            System.out.print("Current Word:" + round.getCurrentWordDisplayed().getWord()+"\n");
            System.out.println("Action: ");

            nextAction = scanner.nextLine();

            if(nextAction.equals("noun")){
                round.putWordInBucket(buckets.get(0), round.getCurrentWordDisplayed());
            }
            if(nextAction.equals("adjective")){
                round.putWordInBucket(buckets.get(2), round.getCurrentWordDisplayed());
            }
            if(nextAction.equals("verb")){
                round.putWordInBucket(buckets.get(1), round.getCurrentWordDisplayed());
            }
        }
        System.out.print("end game");
        scanner.close();

    }
}
