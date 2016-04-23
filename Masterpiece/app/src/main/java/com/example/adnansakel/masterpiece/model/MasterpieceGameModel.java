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

    private Integer currentPlayerToDisplay;

    private String gameNumber;

    private String userName;

    private List<Painting> allPaintings;
    private List<Integer> allPaintingValues;
    private List<Integer> allPaintingIDs;

    private List<Player> allPlayers;
    private Player myPlayer;
    //private List<String>turnTypes;
    private List<Integer>shuffledPaintingValues;//a list to shuffle the paintings
    private List<Integer>shuffledPaintingIDs;
    private Integer nextBankPainting;

    // private List<Integer>bankPaintingIDs;
    //private List<Integer>bankPaintingValues;

    // private List<Integer>paintingShuffler;//A list to shuffle the paintings
    // private List<Integer>shuffledPaintingID;

    private String paintingBeingAuctioned = "";
    private String turnTaker = "";
    private String turnAction = "setup";

    private String currentBidder = "";

    private String currentBid = "";

    private String CountNonBidders = "";
    //private String color;


    private String popupContent;


    public MasterpieceGameModel(){

        allPlayers = new ArrayList<Player>();
        allPaintings = new ArrayList<Painting>();

        allPaintingValues = new ArrayList<Integer>();
        allPaintingIDs = new ArrayList<Integer>();

        //pre populated with fixed values

        /*turnTypes = new ArrayList<>();
        turnTypes.add("PrivateAuction");
        turnTypes.add("BankAuction");*/

        shuffledPaintingValues = new ArrayList<Integer>();
        shuffledPaintingIDs = new ArrayList<Integer>();

        for(int i = 0; i < AppConstants.TotalNumberofPainting; i++){//assuming there are 20 paintings
            shuffledPaintingIDs.add(i);
            shuffledPaintingValues.add((i + 1) * 100000);
        }
        Collections.shuffle(shuffledPaintingIDs);
        Collections.shuffle(shuffledPaintingValues);
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
    /*public void setAllPaintings(List<Painting> allPaintings){
        this.allPaintings = allPaintings;
    }*/

    /*public List<Painting> getAllPaintings1(){ //name has 1 because of duplicate getAllPaintings below
        return allPaintings;
    }*/

    /*public void setAllPaintingValues(List<Integer> allPaintingValues){
        this.allPaintingValues = allPaintingValues;
    }*/

    public List<Integer> getAllPaintingValues(){
        return allPaintingValues;
    }

    public void setTurnTaker(String turnTaker){
        this.turnTaker = turnTaker;
        setChanged();
        notifyObservers(AppConstants.TURN_TAKER_CHANGED);
    }

    public String getTurnTaker(){

        return turnTaker;
    }

    public void setTurnAction(String turnAction){
        this.turnAction = turnAction;
        setChanged();
        notifyObservers(AppConstants.TURN_ACTION_CHANGED);
    }
    public String getTurnAction() {
        return turnAction;
    }

    public void setPaintingBeingAuctioned(String paintingBeingAuctioned){
        this.paintingBeingAuctioned = paintingBeingAuctioned;
    }


    public String getPaintingBeingAuctioned(){
        return paintingBeingAuctioned;
    }

    public void setCurrentBidder(String currentBidder){
        this.currentBidder = currentBidder;
        setChanged();
        notifyObservers(AppConstants.CURRENT_BIDDER_CHANGED);
    }

    public String getCurrentBidder(){
        return currentBidder;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer = myPlayer;
    }
    //TODO: myPlayer is empty. need to set it when joining the game
    public Player getMyPlayer(){
        return myPlayer;
    }

    /*public void setNextPlayer(){
        Integer myPlayerIndex = allPlayers.indexOf(myPlayer);
        //if I'm player 4, next player will be player 1
        if (myPlayerIndex <= 2) {
            this.nextPlayer = allPlayers.get(myPlayerIndex + 1);
        } else {
            this.nextPlayer = allPlayers.get(0);
        }
    }*/
    //TODO: need to call this when joining the game
    /*public Player getNextPlayer(){
        return nextPlayer;
    }*/

    public List<Painting> getAllPaintings(){
        //TODO: Define proper function with by ID or object

        return allPaintings;
    }

    public Player getPlayer(int position){
        return allPlayers.get(position);
    }

    public void addPlayer(Player player) {
        allPlayers.add(player);
        setChanged();
        notifyObservers();
    }

    public void removeAllPlayer(){
        allPlayers.clear();
        setChanged();
        notifyObservers();
    }

    /*public void removeAllPaintings(){
        allPaintings.clear();
        setChanged();
        notifyObservers();
    }*/

    public void shufflePaintingIDsandValues(){
        Collections.shuffle(shuffledPaintingIDs);
        Collections.shuffle(shuffledPaintingValues);
    }

    /*public String getTurnType(int position){
        return turnTypes.get(position);
    }*/

    /*public String getColor() {
        return color;

    }*/

    /*
    public void setColor(String color) {

        this.color = color;
        setChanged();
        notifyObservers();
    }*/

    public String getCountNonBidders() {
        return CountNonBidders;
    }
    public void setCountNonBidders(String countNonBidders) {
        CountNonBidders = countNonBidders;
        setChanged();
        notifyObservers();
    }

    public String getCurrentBid() {
        return currentBid;
    }
    public void setCurrentBid(String currentBid) {
        this.currentBid = currentBid;
    }

    public void setShuffledPaintingValues(List<Integer> shuffledPaintingValues) {
        this.allPaintingValues = shuffledPaintingValues;
    }

    /*public void addPaintingIDAndValue( Integer paintingID, int value){
        myPlayer.addOwnedPaintingID(paintingID);
        myPlayer.addOenedPaintingValue(value);
    }*/


    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Painting getPaintingbyPosition(int position){
        return allPaintings.get(position);
    }

    /*
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
    */
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

    public void addPaintingtoAllPaintings(Painting painting) {
        allPaintings.add(painting);
    }

    public Painting getPainting(int position){
        return allPaintings.get(position);
    }


    public void setPopupContent(String popupContent) {
        this.popupContent = popupContent;
        setChanged();
        notifyObservers("popupContentChanged");
    }
    public String getPopupContent(){
        return popupContent;
    }

    public Integer getCurrentPlayerToDisplay() {
        return currentPlayerToDisplay;
    }

    public void setCurrentPlayerToDisplay(Integer currentPlayerPositionID) {
        this.currentPlayerToDisplay = currentPlayerPositionID;
        setChanged();
        notifyObservers("currentPlayerToDisplayChanged");
    }

    public void setCash(int cash, int playerPositionID) {
        getPlayer(playerPositionID).setCash(cash);
        setChanged();
        notifyObservers("cashChanged");
    }

    public void addPainting(int playerPositionID, int paintingID, int value) {
        getPlayer(playerPositionID).addOwnedPaintingID(paintingID);
        getPlayer(playerPositionID).addOenedPaintingValue(value);
    }

    public void notifyAllPaintingsAdded(){
        setChanged();
        notifyObservers("PaintingAdded");
    }

    public Integer getNextBankPainting() {
        return nextBankPainting;
    }

    public void setNextBankPainting(Integer nextBankPainting) {
        this.nextBankPainting = nextBankPainting;
    }

    public void notifyViewToShowPopupBid(){
        setChanged();
        notifyObservers("ViewToShowPopupBid");
    }
}
