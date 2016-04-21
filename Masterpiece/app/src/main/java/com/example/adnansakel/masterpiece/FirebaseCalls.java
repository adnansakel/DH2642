package com.example.adnansakel.masterpiece;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
                game.put("TurnTaker", turntaker);
                game.put("TurnAction", "");
                game.put(AppConstants.GAMESTATE, "SetUp");
                game.put("ShuffledPaintingValues", masterpieceGameModel.getShuffledPaintingValues());
                game.put("ShuffledPaintings", masterpieceGameModel.getShuffledPaintingIDs());
                game.put("PaintingBeingAuctioned", "");
                game.put("CurrentBidder", 100);
                game.put(AppConstants.CURRENTBID, 0);
                game.put(AppConstants.COUNTNONBIDDERS, 0);
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
        player.put("Cash", "1500000"); //Initial cash
        player.put("BidAmount", "");
        player.put("Bidding", "");
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
                player.put("Cash", "");
                player.put("BidAmount", "");

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
                masterpieceGameModel.removeAllPlayer();
                int i = 0;
                for (DataSnapshot dsplayer : dataSnapshot.getChildren()) {
                    //DataSnapshot dpl = (DataSnapshot)dsplayer.getValue();
                    //System.out.println(dsplayer.child("Name"));
                    Player player = new Player();
                    player.setName(dsplayer.child("Name").getValue().toString());
                    player.setPlayerpositionID(i);
                    player.setFirebaseid(dsplayer.getKey().toString());
                    masterpieceGameModel.addPlayer(player);
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

                        for (int i = 0; i < AppConstants.TotalNumberofPlayers; i++) {//masterpiecegamemodel.getAllPlayers().size() should be used instead of 4
                            masterpieceGameModel.getAllPlayers().get(i).addOwnedPaintingID(shuffledpaintinglist.get(i));
                            masterpieceGameModel.getAllPlayers().get(i).addOenedPaintingValue(shuffledpaintinvalueglist.get(i));
                        }
                        shuffledpaintinglist.subList(0, AppConstants.TotalNumberofPlayers).clear();//removing distributed paintings
                        shuffledpaintinvalueglist.subList(0, AppConstants.TotalNumberofPlayers).clear();//removing distributed paintings
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
                masterpieceGameModel.setCountNonBidders((Integer) snapshot.child("CountNonBidders").getValue());
                //masterpieceGameModel.setCountPlayers((Integer) snapshot.child("CountPlayers").getValue()); //maybe we don't need this
                masterpieceGameModel.setCurrentBid((Integer) snapshot.child("CurrentBid").getValue());
                masterpieceGameModel.setCurrentBidder((Integer) snapshot.child("CurrentBidder").getValue());
                //I skipped GameNumber, I don't think we need to set that from here
                masterpieceGameModel.setPaintingBeingAuctioned((Integer) snapshot.child("PaintingBeingAuctioned").getValue());
                masterpieceGameModel.setTurnTaker((Integer) snapshot.child("TurnTaker").getValue());

                //strings
                //I skipped GameState, maybe we won't need it?
                masterpieceGameModel.setTurnAction((String) snapshot.child("TurnAction").getValue());

                //reset playerNumber after each onDataChange
                Integer playerNumber = 0;

                //loop through each player
                for (DataSnapshot playersSnapshot: snapshot.child("Players").getChildren()) {
                   // masterpieceGameModel.getPlayer(playerNumber).setBidAmount((Integer) snapshot.child("BidAmount").getValue());
                    masterpieceGameModel.getPlayer(playerNumber).setBidding((Boolean) snapshot.child("Bidding").getValue());
                    masterpieceGameModel.getPlayer(playerNumber).setCash((Integer) snapshot.child("Cash").getValue());
                    masterpieceGameModel.getPlayer(playerNumber).setName((String) snapshot.child("Name").getValue());
                   // masterpieceGameModel.getPlayer(playerNumber).setOwnedPaintings((List<Integer>) snapshot.child("Paintings").getValue());
                    playerNumber++;
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Firebase read failed: " + firebaseError.getMessage());
            }
        });
    }
}
