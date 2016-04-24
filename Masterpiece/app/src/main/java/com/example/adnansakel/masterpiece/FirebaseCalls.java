package com.example.adnansakel.masterpiece;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Adnan Sakel on 4/21/2016.
 */
public class FirebaseCalls {
    private MasterpieceGameModel masterpieceGameModel;
    private ProgressDialog progress;
    private Context context;
    Firebase masterpieceRef;
    public FirebaseCalls(Context context, MasterpieceGameModel masterPieceGameModel){
        this.context = context;
        this.masterpieceGameModel = masterPieceGameModel;
        Firebase.setAndroidContext(context);
    }

    public void createGame(){
        masterpieceRef = new Firebase(AppConstants.FireBaseUri);
        progress = ProgressDialog.show(context, "", "creating game ...", true);

        masterpieceRef.child("GameNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String game_number = dataSnapshot.getValue().toString();

                AppConstants.GameID = game_number;
                System.out.println("Game number:" + game_number);
                masterpieceGameModel.setGameNumber("" + game_number);
                Map<String, Object> newGameNumber = new HashMap<String, Object>();
                newGameNumber.put(AppConstants.GAMENUMBER, String.valueOf(Integer.valueOf(game_number) + 1));
                masterpieceRef.updateChildren(newGameNumber);
                Map<String, Object> game = new HashMap<String, Object>();
                game.put("GameNr", masterpieceGameModel.getGameNumber());
                game.put("CountPlayers", "");

                //Map<String,Object>p1 = new HashMap<String, Object>();

                game.put("Players", "");
                Random randomplayer = new Random();
                int turntaker = randomplayer.nextInt(4);
                game.put("TurnTaker", String.valueOf(turntaker));
                //game.put("TurnTaker", "3"); // for testing //TODO SET BACK TO Random
                game.put("TurnAction", "pending");
                game.put(AppConstants.GAMESTATE, "SetUp");
                game.put("ShuffledPaintingValues", masterpieceGameModel.getShuffledPaintingValues());
                game.put("ShuffledPaintings", masterpieceGameModel.getShuffledPaintingIDs());
                game.put("PaintingBeingAuctioned", "");
                game.put("CurrentBidder", "100");
                game.put(AppConstants.CURRENTBID, "0");
                game.put(AppConstants.COUNTNONBIDDERS, "0");
                game.put("BankPaintings", "");

                Firebase gamesRef = masterpieceRef.child("Games");
                final Firebase newGameRef = gamesRef.push();
                newGameRef.setValue((game), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        //progress.dismiss();
                        if (firebaseError != null) {
                            progress.dismiss();
                            Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
                            //textViewGameNumber.setText(firebaseError.getMessage().toString());
                        } else {
                            progress.dismiss();
                            masterpieceGameModel.setGameNumber(game_number);
                            AppConstants.GameRef = newGameRef.toString();
                            AppConstants.IamCreator = true;
                        }
                    }


                });
                //System.out.println("New game ref: " + newGameRef.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progress.dismiss();
                Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
            }
        });
    }

    public void joinGamebyCreator(){
        Map<String, Object> player = new HashMap<String, Object>();
        //String[]paintings = {"1","2","3","4"};
        //masterpieceGameModel.setUserName(editTextUserName.getText().toString());
        player.put("Name", masterpieceGameModel.getUserName().toString());
        player.put("Paintings", "");
        player.put("Cash", "800000"); //Initial cash
        player.put("BidAmount", "");
        player.put("Bidding", "true");
        progress = ProgressDialog.show(context, "", "joining game ...", true);
        new Firebase(AppConstants.GameRef + "/" + "Players").push().setValue(player, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                //progress.dismiss();
                if (firebaseError != null) {
                    //textViewGameNumber.setText(firebaseError.getMessage().toString());
                    progress.dismiss();
                    Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
                } else {
                    progress.dismiss();
                    //textViewGameNumber.setText(game_number);
                    //lobby activity should come here
                    context.startActivity(new Intent(context, LobbyActivity.class));
                }
            }
        });
    }

    public void joinGame(){
        progress = ProgressDialog.show(context, "", "joining game ...", true);
                /*
                * Joing game is done in two steps.
                * 1. Look for the GameID in fire base and get the Firebase key associated with that GameID
                * 2. Use the Firebase key got from step 1 to generate the Firebase reference to add new player in the game
                * */

                /*
                * Step 1 as follows
                * */
        Firebase ref = new Firebase(AppConstants.FireBaseUri+"/"+"Games");
        Query qref = ref.orderByChild(AppConstants.GAMENR).equalTo(masterpieceGameModel.getGameNumber());
        qref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                System.out.println(snapshot.toString());
                System.out.println(snapshot.getKey().toString());

                AppConstants.GameRef = AppConstants.FireBaseUri + "/" + "Games" + "/" + snapshot.getKey().toString();
                Map<String, Object> player = new HashMap<String, Object>();
                //String[]paintings = {"1","2","3","4"};
                player.put("Name", masterpieceGameModel.getUserName());
                player.put("Paintings", "");
                player.put("Cash", "800000");
                player.put("BidAmount", "");
                player.put("Bidding", "true");

                        /*
                        * Step 2 as follows
                        * */
                new Firebase(AppConstants.GameRef + "/" + "Players").push().setValue(player, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        //progress.dismiss();
                        if (firebaseError != null) {
                            //textViewGameNumber.setText(firebaseError.getMessage().toString());
                            progress.dismiss();
                            Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        } else {
                            progress.dismiss();
                            //textViewGameNumber.setText(game_number);
                            //lobby activity should come here
                            AppConstants.PlayerRef = firebase.getRef().toString();//Ref for player who is playing on this device
                            System.out.println("Player ref:" + AppConstants.PlayerRef);
                            AppConstants.GameID = masterpieceGameModel.getGameNumber();
                            masterpieceGameModel.setGameNumber(AppConstants.GameID);
                            context.startActivity(new Intent(context, LobbyActivity.class));
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            // ....

        });
    }

    public void downloadMasterpiecePaintings(){
        progress = ProgressDialog.show(context, "", "Downloading masterpiece paintings ...", true);
        new Firebase(AppConstants.FireBaseUri+"/"+AppConstants.PAINTINGS).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Painting painting = new Painting();
                            System.out.println(ds.getValue().toString());
                            System.out.println(ds.child("Artist").getValue().toString());
                            painting.setArtist(ds.child(AppConstants.ARTIST).getValue().toString());
                            painting.setImageURL(ds.child(AppConstants.IMAGE).getValue().toString());
                            painting.setName(ds.child(AppConstants.NAME).getValue().toString());
                            painting.setDescription(ds.child(AppConstants.DESCRIPTION).getValue().toString());
                            masterpieceGameModel.addPaintingtoAllPaintings(painting);
                            progress.dismiss();

                        }

                        ImageDownloader imageDownloader = new ImageDownloader(context, masterpieceGameModel);
                        imageDownloader.downloadImages(progress);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        progress.dismiss();
                        Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void listentoFirebaseforPlayers(){

        new Firebase(AppConstants.GameRef+"/"+"Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // only execute if turnAction is different from "setup", which is set in the beginning in the gameModel
                if (masterpieceGameModel.getTurnAction() != "setup") {
                    return;
                }
                masterpieceGameModel.removeAllPlayer();

                int i = 0;
                for (DataSnapshot dsplayer : dataSnapshot.getChildren()) {
                    //DataSnapshot dpl = (DataSnapshot)dsplayer.getValue();
                    //System.out.println(dsplayer.child("Name"));
                    Player player = new Player();
                    player.setName(dsplayer.child(AppConstants.NAME).getValue().toString());
                    player.setPlayerpositionID(i);
                    player.setFirebaseid(dsplayer.getKey().toString());
                    player.setCash(Integer.valueOf(dsplayer.child(AppConstants.CASH).getValue().toString()));
                    if (dsplayer.child(AppConstants.BIDDING).getValue().toString().equals("true")) {
                        player.setBidding(true);
                    } else {
                        player.setBidding(false);
                    }
                    masterpieceGameModel.addPlayer(player);
                    System.out.println(player.getName() + "/" + player.getPlayerpositionID());
                    if (player.getName().equals(masterpieceGameModel.getUserName())) {
                        masterpieceGameModel.setMyPlayer(player);
                    }
                    // masterpiecegamemodel.notifyObservers();
                    i++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void distributeShuffledPaintingandValues(){
        progress = ProgressDialog.show(context, "", "distributing paintings ...", true);
        //downloading the shuffled painting ids and values then assign a painting and value to my player and rest to bamk
        new Firebase(AppConstants.GameRef).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {
                        };
                        List<Integer> shuffledpaintinglist = new ArrayList<Integer>();
                        List<Integer> shuffledpaintinvalueglist = new ArrayList<Integer>();
                        shuffledpaintinglist = dataSnapshot.child(AppConstants.SHUFFLEDPAINTINGS).getValue(t);
                        masterpieceGameModel.setShuffledPaintingIDs(shuffledpaintinglist);
                        shuffledpaintinvalueglist = dataSnapshot.child(AppConstants.SHUFFLEDPAINTINGVALUES).getValue(t);
                        masterpieceGameModel.setShuffledPaintingValues(shuffledpaintinvalueglist);

                        // Distribute 2 paintings (e.g. first player gets the first in the list and the fifth, second gets the second and the sixth,...
                        for (int i = 0; i < AppConstants.TotalNumberofPlayers; i++) {
                            masterpieceGameModel.addPainting(i, shuffledpaintinglist.get(i), shuffledpaintinvalueglist.get(i));
                            masterpieceGameModel.addPainting(i, shuffledpaintinglist.get(i+AppConstants.TotalNumberofPlayers), shuffledpaintinvalueglist.get(i+AppConstants.TotalNumberofPlayers));
                        }

                        masterpieceGameModel.notifyAllPaintingsAdded();
                        //masterpieceGameModel.setCurrentPlayerToDisplay(masterpieceGameModel.getMyPlayer().getPlayerpositionID());
                        //shuffledpaintinglist.subList(0, AppConstants.TotalNumberofPlayers).clear();//removing distributed paintings
                        //shuffledpaintinvalueglist.subList(0, AppConstants.TotalNumberofPlayers).clear();//removing distributed paintings

                        //set next bank painting to TotalNumberofPlayers + 1
                        masterpieceGameModel.setNextBankPainting(AppConstants.TotalNumberofPlayers + 1);

                        progress.dismiss();


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        progress.dismiss();
                        Toast.makeText(context, firebaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void listentoFireBaseForGameLogic(){
        Firebase firebaseRef = new Firebase(AppConstants.GameRef);
        firebaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Some data changed in Firebase: " + snapshot.getValue());

                //integers
                masterpieceGameModel.setCountNonBidders(snapshot.child(AppConstants.COUNTNONBIDDERS).getValue().toString());
                //masterpieceGameModel.setCountPlayers((Integer) snapshot.child("CountPlayers").getValue()); //maybe we don't need this
                //masterpieceGameModel.setCurrentBid(snapshot.child("CurrentBid").getValue().toString());
                //System.out.println("here?");
                //if I'm the current bidder and Bidding is set to false on my player
                /*if (snapshot.child(AppConstants.CURRENTBIDDER).getValue().toString() == String.valueOf(masterpieceGameModel.getMyPlayer().getPlayerpositionID()) && masterpieceGameModel.getMyPlayer().isBidding() == false) {
                    System.out.println("Set next player as current bidder");
                    //don't set current bidder. instead set next player as current bidder
                    setNextPlayerAsBidder();
                }*/

                //otherwise set the current bidder in the model
                // else {
                //if current bidder value has changed
                if (masterpieceGameModel.getCountNonBidders().equals("3")) {
                    /*if(AppConstants.IamCreator){
                        new Firebase(AppConstants.GameRef+"/"+AppConstants.TURNTAKER).setValue(((Integer
                                .valueOf(masterpieceGameModel.getTurnTaker())+1)%4)+"");

                    }*/
                    new Firebase(AppConstants.GameRef + "/" + AppConstants.PLAYERS).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    // do some stuff once
                                    System.out.println(snapshot.getValue());
                                    int i = 0;
                                    for (DataSnapshot dplayer : snapshot.getChildren()) {
                                        //System.out.println("Player name: " + dplayer.child("Name").getValue().toString());
                                        //System.out.println("Is bidding:" + dplayer.child("Bidding").getValue().toString());
                                        if (dplayer.child(AppConstants.BIDDING).getValue().toString().equals("true")) {
                                            masterpieceGameModel.setWinningbidamount(dplayer.child(AppConstants.BIDAMOUNT).getValue().toString() + "");
                                            masterpieceGameModel.setWinner(i + "");

                                            System.out.println("Winner:" + i);


                                            //updating the cash and painting for winning player
                                            masterpieceGameModel.getAllPlayers().get(i).addOwnedPaintingID(
                                                    Integer.valueOf(masterpieceGameModel.getPaintingBeingAuctioned()));
                                            masterpieceGameModel.getAllPlayers().get(i).addOenedPaintingValue(
                                                    masterpieceGameModel.getAllPaintingValues().get(Integer.valueOf(masterpieceGameModel.getPaintingBeingAuctioned())));
                                            masterpieceGameModel.getAllPlayers().get(i).setCash(masterpieceGameModel.getMyPlayer().getCash()
                                                    - Integer.valueOf(masterpieceGameModel.getCurrentBid()));

                                            if(masterpieceGameModel.getMyPlayer().getPlayerpositionID() == i){
                                                //If winnner is myPlayer; update that as well
                                                masterpieceGameModel.getMyPlayer().addOwnedPaintingID(
                                                        Integer.valueOf(masterpieceGameModel.getPaintingBeingAuctioned()));
                                                masterpieceGameModel.getMyPlayer().addOenedPaintingValue(
                                                        masterpieceGameModel.getAllPaintingValues().get(Integer.valueOf(masterpieceGameModel.getPaintingBeingAuctioned())));
                                                masterpieceGameModel.getMyPlayer().setCash(masterpieceGameModel.getMyPlayer().getCash()
                                                        - Integer.valueOf(masterpieceGameModel.getCurrentBid()));

                                            }
                                            //updating for loosing player or turn taker
                                            int loosingPlayerPosition = Integer.valueOf(masterpieceGameModel.getTurnTaker());
                                            int paintingAuctioned = Integer.valueOf(masterpieceGameModel.getPaintingBeingAuctioned());
                                            int currentCashofTurntaker = masterpieceGameModel.getAllPlayers().get(loosingPlayerPosition).getCash();
                                            masterpieceGameModel.getAllPlayers().get(loosingPlayerPosition).removePaintingIDandValue(paintingAuctioned);
                                           // masterpieceGameModel.getAllPlayers().get(loosingPlayerPosition).removePaintingValues(paintingAuctioned);
                                            masterpieceGameModel.getAllPlayers().get(loosingPlayerPosition).setCash(currentCashofTurntaker
                                                            + Integer.valueOf(masterpieceGameModel.getCurrentBid()));

                                            if(masterpieceGameModel.getMyPlayer().getPlayerpositionID()==loosingPlayerPosition){
                                                //if turn taker is myPlayer update that as well

                                                masterpieceGameModel.getMyPlayer().removePaintingIDandValue(paintingAuctioned);
                                                masterpieceGameModel.getMyPlayer().setCash(currentCashofTurntaker
                                                        + Integer.valueOf(masterpieceGameModel.getCurrentBid()));

                                            }



                                        }

                                        i++;

                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast.makeText(context, "Some error occured while finding winner", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                } else if (masterpieceGameModel.getCountNonBidders().equals("4")) {
                    //do nothing, since somebody won the game
                } else {

                    //if current bidder has changed
                    if (!masterpieceGameModel.getCurrentBidder().equals(snapshot.child(AppConstants.CURRENTBIDDER).getValue().toString())) {
                        //System.out.println("Current bidder has changed and is greater than 0");
                        //set current bidder in model
                        masterpieceGameModel.setCurrentBidder(snapshot.child(AppConstants.CURRENTBIDDER).getValue().toString());
                        //masterpieceGameModel.setPopupContent("privateAuctionBid");
                        System.out.println("CurrentBidder: " + masterpieceGameModel.getCurrentBidder());
                        System.out.println("getPlayerPositionID pointA: " + masterpieceGameModel.getMyPlayer().getPlayerpositionID());
                        //if I am the current bidder
                        if (masterpieceGameModel.getCurrentBidder().equals(masterpieceGameModel.getMyPlayer().getPlayerpositionID() + "")) {
                            //if my player is not set to Bidding
                            if (!masterpieceGameModel.getMyPlayer().isBidding()) {
                                System.out.println("getPlayerPositionID pointB: " + masterpieceGameModel.getMyPlayer().getPlayerpositionID());
                                //increase current bidder by one in firebase
                                new Firebase(AppConstants.GameRef + "/" + AppConstants.CURRENTBIDDER).setValue(((Integer
                                        .valueOf(masterpieceGameModel.getCurrentBidder()) + 1) % 4) + "");
                            }
                        }
                    }

                    // }
                    //I skipped GameNumber, I don't think we need to set that from here
                    if (!masterpieceGameModel.getPaintingBeingAuctioned().equals(snapshot.child(AppConstants.PAINTINGBEINGAUCTIONED).getValue().toString())) {
                        masterpieceGameModel.setPaintingBeingAuctioned(snapshot.child(AppConstants.PAINTINGBEINGAUCTIONED).getValue().toString());
                    }

                    if (!masterpieceGameModel.getTurnTaker().equals(snapshot.child(AppConstants.TURNTAKER).getValue().toString())) {
                        masterpieceGameModel.setTurnTaker(snapshot.child(AppConstants.TURNTAKER).getValue().toString());
                        //System.out.println("From firebasecalls: Turn taker set : " + snapshot.child(AppConstants.TURNTAKER).getValue().toString());
                    }

                    //strings
                    //I skipped GameState, maybe we won't need it?

                    //if turn action has changed
                    if (!masterpieceGameModel.getTurnAction().equals(snapshot.child(AppConstants.TURNACTION).getValue())) {
                        masterpieceGameModel.setTurnAction((String) snapshot.child(AppConstants.TURNACTION).getValue());
                    }


                    //reset playerNumber after each onDataChange
                    int playerNumber = 0;
                    if (!masterpieceGameModel.getCurrentBid().equals(snapshot.child(AppConstants.CURRENTBID).getValue().toString())) {
                        masterpieceGameModel.setCurrentBid(snapshot.child(AppConstants.CURRENTBID).getValue().toString());
                    }

                    String biddingstatus = "";
                    if (masterpieceGameModel.getMyPlayer().isBidding()) biddingstatus = "true";
                    else {
                        biddingstatus = "false";
                    }
                    if (!biddingstatus.equals(snapshot.child(AppConstants.PLAYERS).child(masterpieceGameModel.getMyPlayer().getFirebaseid()).child(AppConstants.BIDDING).getValue().toString())) {
                        if (biddingstatus.equals("true")) {
                            masterpieceGameModel.getMyPlayer().setBidding(false);
                        } else {
                            masterpieceGameModel.getMyPlayer().setBidding(true);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Firebase read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void setNextPlayerAsBidder() {
        new Firebase(AppConstants.GameRef + "/" + "CurrentBidder").setValue(Integer.toString((masterpieceGameModel.getMyPlayer().getPlayerpositionID() + 1) % 4));
    }

    public void resetFirebaseforNextRound(){
        masterpieceGameModel.setWinner("");
        progress = ProgressDialog.show(context, "", "Loading next round...", true);
        //reset a lot of Firebase values, increase turntaker, and finally reset countnonbidders at the end
        //1: reset current bid

        new Firebase(AppConstants.GameRef + "/" + AppConstants.COUNTNONBIDDERS).setValue("4", new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    progress.dismiss();
                    Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    new Firebase(AppConstants.GameRef + "/" + AppConstants.CURRENTBID).setValue("0", new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                progress.dismiss();
                                Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                //2: set turntaker to next player
                                new Firebase(AppConstants.GameRef+"/"+AppConstants.TURNTAKER).setValue(((Integer.valueOf(masterpieceGameModel.getTurnTaker())+1)%4)+"",new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {
                                            progress.dismiss();
                                            Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                        } else {
                                            //3: reset current bidder
                                            new Firebase(AppConstants.GameRef+"/"+AppConstants.CURRENTBIDDER).setValue("100",new Firebase.CompletionListener() {
                                                @Override
                                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                    if (firebaseError != null) {
                                                        progress.dismiss();
                                                        Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        //4: reset painting being auctioned
                                                        new Firebase(AppConstants.GameRef+"/"+AppConstants.PAINTINGBEINGAUCTIONED).setValue("",new Firebase.CompletionListener() {
                                                            @Override
                                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                if (firebaseError != null) {
                                                                    progress.dismiss();
                                                                    Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    //5: reset turn action
                                                                    new Firebase(AppConstants.GameRef+"/"+AppConstants.TURNACTION).setValue("pending",new Firebase.CompletionListener() {
                                                                        @Override
                                                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                            if (firebaseError != null) {
                                                                                progress.dismiss();
                                                                                Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                            } else {
                                                                                //6: set bidding to true for player 0
                                                                                new Firebase(AppConstants.GameRef+"/"+AppConstants.PLAYERS+"/"+masterpieceGameModel.getAllPlayers().get(0).getFirebaseid()+"/"+AppConstants.BIDDING).setValue("true",new Firebase.CompletionListener() {
                                                                                    @Override
                                                                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                        if (firebaseError != null) {
                                                                                            progress.dismiss();
                                                                                            Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                                        } else {
                                                                                            //7: set bidding to true for player 1
                                                                                            new Firebase(AppConstants.GameRef+"/"+AppConstants.PLAYERS+"/"+masterpieceGameModel.getAllPlayers().get(1).getFirebaseid()+"/"+AppConstants.BIDDING).setValue("true",new Firebase.CompletionListener() {
                                                                                                @Override
                                                                                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                                    if (firebaseError != null) {
                                                                                                        progress.dismiss();
                                                                                                        Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                    } else {
                                                                                                        //8: set bidding to true for player 2
                                                                                                        new Firebase(AppConstants.GameRef+"/"+AppConstants.PLAYERS+"/"+masterpieceGameModel.getAllPlayers().get(2).getFirebaseid()+"/"+AppConstants.BIDDING).setValue("true",new Firebase.CompletionListener() {
                                                                                                            @Override
                                                                                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                                                if (firebaseError != null) {
                                                                                                                    progress.dismiss();
                                                                                                                    Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                                } else {
                                                                                                                    //9: set bidding to true for player 3
                                                                                                                    new Firebase(AppConstants.GameRef+"/"+AppConstants.PLAYERS+"/"+masterpieceGameModel.getAllPlayers().get(3).getFirebaseid()+"/"+AppConstants.BIDDING).setValue("true",new Firebase.CompletionListener() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                                                                            if (firebaseError != null) {
                                                                                                                                progress.dismiss();
                                                                                                                                Toast.makeText(context, "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                                            } else {
                                                                                                                                //10: reset countnonbidders
                                                                                                                                new Firebase(AppConstants.GameRef+"/"+AppConstants.COUNTNONBIDDERS).setValue("0");
                                                                                                                                //hide progress dialog
                                                                                                                                progress.dismiss();
                                                                                                                                //hide end round button
                                                                                                                                //button_end_round.setVisibility(View.INVISIBLE);
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

        });
    }

}
