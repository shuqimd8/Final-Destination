package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameSystem {
    private File gameFile;
    private File bucketFile;
    private File wordFile;

    private List<Game> games;

    public GameSystem(File gameFile, File bucketFile, File wordFile){
        this.gameFile = gameFile;
        this.wordFile = wordFile;
        this.bucketFile = bucketFile;
        this.games = new ArrayList<>();
        createGames();
        //verification text
        System.out.println("Game System Creates with the following games: "+getGames());
    }

    /**
     * Getter for games list
     * @return unmodifiable list of Game objects in the game system
     */
    public List<Game> getGames(){
        return Collections.unmodifiableList(this.games);
    }

    /**
     * called by constructor: will go through list of games and create the games
     */
    public void createGames(){
        //get list of lines in gameFile txt
        List<String> gameFileLines = turnFileToListOfLines(this.gameFile);
        //go through each line and create the game
        for(String line:gameFileLines){
            //create game object
            Game game = createGame(line);
            //add the game to list of games
            games.add(game);
        }
    }
    /**
     * Method will create a game object for a given line in the gameFile txt
     * @param textFileLine text file line from the gameFile file, as a string
     * @return instance of the Game object from the text file line
     */
    public Game createGame(String textFileLine){
        //get game id
        int gameId = extractGameID(textFileLine);
        //get game name
        String gameName = extractGameName(textFileLine);
        //create instance of game
        return new Game(gameId, gameName, this.bucketFile, this.wordFile);
    }
    /**
     * Given a file it will read the file and return a list with each line of the file as a string
     * @param file File you want to read
     * @return list of lines from the file
     */
    public static List<String> turnFileToListOfLines(File file){
        List<String> fileLines = new ArrayList<>();
        //get file path
        String filePath = file.getPath();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
                // Process each line here
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        //remove the first line because it just shows the line format
        fileLines.removeFirst();
        return fileLines;
    }

    /**
     * Given a line from the gameFile the method will return a list of indexes of each break:'-' in the segment
     * @param textFileLine a line from the gameFile File as a string, which will have the format 'GameID-GameName'
     * @return position of '-' in the textfileLine
     */
    private int positionOfBreak(String textFileLine){
        final String breakCharacter ="-";
        //there are two breaks the first break is after BucketID, so the start position will be 0
        int startPosition = 0;
        //find the first break
        int firstBreak = textFileLine.indexOf(breakCharacter, startPosition);

        return firstBreak;
    }

    /**
     * Given a string from the gameFile it will return the GameID
     * @param textFileLine a line from the gameFile File as a string, which will have the format 'GameID-GameName'
     * @return int value of 'GameID' from the text file line
     */
    public int extractGameID(String textFileLine){
        //get the break position
        int positionOfBreak = positionOfBreak(textFileLine);
        String gameIDSegment = textFileLine.substring(0,positionOfBreak);
        //attempt to turn the gameID string to an int
        int gameId_int;
        try {
            gameId_int = Integer.parseInt(gameIDSegment);
            //System.out.println("Converted integer using parseInt(): " + gameId_int);
        } catch (NumberFormatException e) {
            gameId_int = -1;
            System.out.println("GAMESYSTEM Invalid number format: " + e.getMessage());
        }
        //return int
        return gameId_int;
    }

    /**
     * Given a string from the gameFile it will return the BucketName
     * @param textFileLine a line from the gameFile File as a string, which will have the format 'GameID-GameName'
     * @return string value of 'GameName' from the text file line
     */
    public String extractGameName(String textFileLine){
        //get starting position which will be break position+1
        int startingPosition = positionOfBreak(textFileLine)+1;
        String gameNameSegment = textFileLine.substring(startingPosition);
        return gameNameSegment;
    }
}
