package View;

public class GameRounds {
    int RoundID = 0;
    int Game;
    int RoundNumber;
    int AnswerSpeed;
    boolean WasAnswerCorrect;

    public GameRounds (int Game, int RoundNumber, int AnswerSpeed, boolean WasAnswerCorrect) {
        this.RoundID++;
        this.Game = Game;
        this.RoundNumber = RoundNumber;
        this.AnswerSpeed = AnswerSpeed;
        this.WasAnswerCorrect = WasAnswerCorrect;
    }
}
