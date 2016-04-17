package com.example.adnansakel.masterpiece.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class MasterpieceGameModel extends Observable{

    private String gameNumber;
    private List<Player> allPlayers;
    private String CurrentBid;

    private String userName;
    private List<Painting> allPaintings;
    private List<Integer> allPaintingValues;
    private List<Integer> allPaintingIDs;
    private Player myPlayer;
    private Player nextPlayer;
    //private List<String>turnTypes;
    private List<Integer>shuffledPaintingValues;//a list to shuffle the paintings
    private List<Integer>shuffledPaintingIDs;

    private List<Integer>bankPaintingIDs;
    private List<Integer>bankPaintingValues;

   // private List<Integer>paintingShuffler;//A list to shuffle the paintings
   // private List<Integer>shuffledPaintingID;
    private Painting paintingBeingAuctioned;
    private Player turnTaker;
    private String turnAction;

    private Player currentBidder;

    private String CountNonBidders;
    private String color;


    public MasterpieceGameModel(){

        allPlayers = new ArrayList<Player>();
        allPaintings = new ArrayList<Painting>();

        allPaintingValues = new ArrayList<Integer>();
        allPaintingIDs = new ArrayList<Integer>();

         //prepopulated with fixed values

        /*turnTypes = new ArrayList<>();
        turnTypes.add("PrivateAuction");
        turnTypes.add("BankAuction");*/

        shuffledPaintingValues = new ArrayList<Integer>();
        shuffledPaintingIDs = new ArrayList<Integer>();

        for(int i = 0; i < 20; i++){//assuming there are 20 paintings
            shuffledPaintingIDs.add(i);
            shuffledPaintingValues.add((i + 1) * 100000);
        }
        Collections.shuffle(shuffledPaintingIDs);
        Collections.shuffle(shuffledPaintingValues);

        /*
        // TEMPORARY: This is just for testing, will be replaced by firebase data model
        Bitmap test = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Painting pa1 = new Painting("Painting 1","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test, "A description",100000);
        Painting pa2 = new Painting("Painting 2","http://res.cloudinary.com/masterpiece/image/upload/v1459241655/1.jpg", test,"A description",300000);
        List<Painting> listOfPaintings = new ArrayList<Painting>();
        listOfPaintings.add(pa1);
        listOfPaintings.add(pa2);

        Player p1 = new Player("P1","1",200000,listOfPaintings);
        Player p2 = new Player("P2","2",200000,listOfPaintings);
        Player p3 = new Player("P3","3",200000,listOfPaintings);
        Player p4 = new Player("P4","4",200000,listOfPaintings);
        allPlayers.add(p1);
        allPlayers.add(p2);
        allPlayers.add(p3);
        allPlayers.add(p4);

        System.out.println("Player 1: " + p1);
        System.out.println("Player 1 Paintings: " + p1.ownedPaintings);
        System.out.println("Player 2: " + p2);
        System.out.println("Player 3: " + p3);
        System.out.println("Player 4: " + p4);
        */
    }

    public void setGameNumber(String gameNumber){
        this.gameNumber = gameNumber;
        setChanged();
        notifyObservers();
    }

    public String getGameNumber(){
        return gameNumber;
    }

    public List<Player> getAllPlayers(){
        return allPlayers;
    }

    //the paintings and values are in a list in order to easily randomize them
    public void setAllPaintings(List<Painting> allPaintings){
        this.allPaintings = allPaintings;
    }

    public List<Painting> getAllPaintings1(){ //name has 1 because of duplicate getAllPaintings below
        return allPaintings;
    }

    public void setAllPaintingValues(List<Integer> allPaintingValues){
        this.allPaintingValues = allPaintingValues;
    }

    public List<Integer> getAllPaintingValues(){
        return allPaintingValues;
    }

    public void setTurnTaker(Player turnTaker){
        this.turnTaker = turnTaker;
    }

    public Player getTurnTaker(){
        return turnTaker;
    }

    public void setTurnAction(String turnAction){
        this.turnAction = turnAction;
    }

    public String getTurnAction() {
        return turnAction;
    }

    public void setPaintingBeingAuctioned(Painting paintingBeingAuctioned){
        this.paintingBeingAuctioned = paintingBeingAuctioned;
    }

    public Painting getPaintingBeingAuctioned(){
        return paintingBeingAuctioned;
    }

    public void setCurrentBidder(Player currentBidder){
        this.currentBidder = currentBidder;
    }

    public Player getCurrentBidder(){
        return currentBidder;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer = myPlayer;
    }
    //TODO: myPlayer is empty. need to set it when joining the game

    public Player getMyPlayer(){
        return myPlayer;
    }

    public void setNextPlayer(){
        Integer myPlayerIndex = allPlayers.indexOf(myPlayer);
        //if I'm player 4, next player will be player 1
        if (myPlayerIndex <= 2) {
            this.nextPlayer = allPlayers.get(myPlayerIndex + 1);
        } else {
            this.nextPlayer = allPlayers.get(0);
        }
    }
    //TODO: need to call this when joining the game

    public Player getNextPlayer(){
        return nextPlayer;
    }

    public List<Painting> getAllPaintings(){
        //TODO: Define proper function with by ID or object

        return allPaintings;
    }

    public Player getPlayer(int position){
        return allPlayers.get(position);
    }

    public void addPlayer(Player player){
        allPlayers.add(player);
        setChanged();
        notifyObservers();
    }

    public void removeAllPlayer(){
        allPlayers.clear();
        setChanged();
        notifyObservers();
    }

    /*public String getTurnType(int position){
        return turnTypes.get(position);
    }*/

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        setChanged();
        notifyObservers();
    }

    public String getCountNonBidders() {
        return CountNonBidders;
    }

    public void setCountNonBidders(String countNonBidders) {
        CountNonBidders = countNonBidders;
    }

    public String getCurrentBid() {
        return CurrentBid;
    }

    public void setCurrentBid(String currentBid) {
        CurrentBid = currentBid;
    }

    public void setShuffledPaintingValues(List<Integer> shuffledPaintingValues) {
        this.allPaintingValues = shuffledPaintingValues;
    }

    public void addPaintingIDAndValue( Integer paintingID, int value){
        myPlayer.addOwnedPaintingID(paintingID);
        myPlayer.addOenedPaintingValue(value);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Painting getPaintingbyPosition(int position){
        return allPaintings.get(position);
    }

    public List<Integer> getBankPaintingIDs() {
        return bankPaintingIDs;
    }

    public void setBankPaintingIDs(List<Integer> bankPaintingIDs) {
        this.bankPaintingIDs = bankPaintingIDs;
    }

    public List<Integer> getBankPaintingValues() {
        return bankPaintingValues;
    }

    public void setBankPaintingValues(List<Integer> bankPaintingValues) {
        this.bankPaintingValues = bankPaintingValues;
    }

    public void addBankPaintingID(int paintingID){
        bankPaintingIDs.add(paintingID);
    }

    public void addBankPaintingValue(int paintingValue){
        bankPaintingIDs.add(paintingValue);
    }

    public List<Integer> getShuffledPaintingIDs() {
        return shuffledPaintingIDs;
    }

    public List<Integer> getShuffledPaintingValues() {
        return shuffledPaintingValues;
    }

    public void setShuffledPaintingIDs(List<Integer> shuffledPaintingIDs) {
        this.shuffledPaintingIDs = shuffledPaintingIDs;
    }



    public List<Integer> getAllPaintingIDs() {
        return allPaintingIDs;
    }

    public void setAllPaintingIDs(List<Integer> allPaintingIDs) {
        this.allPaintingIDs = allPaintingIDs;
    }

    public void addPaintingtoAllPaintings(Painting painting){
        allPaintings.add(painting);
    }

    public Painting getPainting(int position){
        return allPaintings.get(position);
    }
}
