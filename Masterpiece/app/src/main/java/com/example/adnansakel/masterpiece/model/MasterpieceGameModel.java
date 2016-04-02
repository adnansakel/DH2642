package com.example.adnansakel.masterpiece.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class MasterpieceGameModel {
    private String gameNumber;

    public MasterpieceGameModel(){
        // TEMPORARY: This is just for testing, will be replaced by firebase data model
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg","A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg","A description",300000);
        Set<Painting> setofpaintings = new HashSet<Painting>(Arrays.asList(pa1,pa2));

        Player p1 = new Player("P1","1",200000,setofpaintings);
        Player p2 = new Player("P2","2",200000,setofpaintings);
        Player p3 = new Player("P3","3",200000,setofpaintings);
        Player p4 = new Player("P4","4",200000,setofpaintings);

        System.out.println("Player 1: " + p1);
        System.out.println("Player 1 Paintings: " + p1.ownedPaintings);
        System.out.println("Player 2: " + p2);
        System.out.println("Player 3: " + p3);
        System.out.println("Player 4: " + p4);
    }

    public void setGameNumber(String gameNumber){
        this.gameNumber = gameNumber;
    }

    public String getGameNumber(){
        return gameNumber;
    }
}
