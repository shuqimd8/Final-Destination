package Model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestGame {
    public static void main(String[] args) {
        //establish game files
        File gameFile = new File("src/main/java/TxtFiles/Games.txt");
        File bucketFile = new File("src/main/java/TxtFiles/Buckets.txt");
        File wordFile = new File("src/main/java/TxtFiles/Words.txt");

        //create game system
        GameSystem gameSystem = new GameSystem(gameFile, bucketFile, wordFile);
    }
}
