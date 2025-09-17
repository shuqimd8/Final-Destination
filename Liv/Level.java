package com.example.finaldestinationgroupproject;

public class Level {
    String LevelName;
    int MinScore;
    int MaxScore;
    String ImageURL;

    public Level(String name, int min, int max, String image) {
        LevelName = name;
        MinScore = min;
        MaxScore = max;
        ImageURL = image;
    }
}
