package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.view.CreateGameView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */

public class CreateGameActivity extends Activity implements View.OnClickListener{
    // Variables for checking the internet connection status

    ConnectionCheck checkConnection;

    Firebase masterpieceRef;
    Button buttonJoinGame;
    ProgressDialog progress;
    TextView textViewGameNumber;
    EditText editTextUserName;

    MasterpieceGameModel masterpiecegamemodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_creategame);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(CreateGameActivity.this);

        masterpiecegamemodel = ((MasterpieceApplication)this.getApplication()).getModel();
        // Creating the view class instance
        CreateGameView createGameViewView = new CreateGameView(findViewById(R.id.creategame_view),masterpiecegamemodel);
        initializeComponent();

        // retrieve Internet status
        if (checkConnection.isConnected()) {
            createGame();
        }
    }

    private void initializeComponent(){
        buttonJoinGame = (Button)findViewById(R.id.buttonJoinGame);
        textViewGameNumber = (TextView)findViewById(R.id.textviewGameNumber);
        editTextUserName = (EditText)findViewById(R.id.edittext_userName);
        buttonJoinGame.setOnClickListener(this);

    }

    private void createGame(){
        Firebase.setAndroidContext(this);
        masterpieceRef = new Firebase(AppConstants.FireBaseUri);
        progress = ProgressDialog.show(this,"","creating game ...", true);

        masterpieceRef.child("GameNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String game_number = dataSnapshot.getValue().toString();


                AppConstants.GameID = game_number;
                masterpiecegamemodel.setGameNumber(game_number);
                Map<String, Object> newGameNumber = new HashMap<String, Object>();
                newGameNumber.put(AppConstants.GAMENUMBER, String.valueOf(Integer.valueOf(game_number) + 1));
                masterpieceRef.updateChildren(newGameNumber);
                Map<String, Object> game = new HashMap<String, Object>();
                game.put("GameNr", masterpiecegamemodel.getGameNumber());
                game.put("CountPlayers", "");

                //Map<String,Object>p1 = new HashMap<String, Object>();

                game.put("Players", "");
                game.put("TurnTaker", "");
                game.put("TurnAction", "");
                game.put(AppConstants.GAMESTATE, "SetUp");
                game.put("ShuffledPaintingValues", masterpiecegamemodel.getShuffledPaintingValues());
                game.put("ShuffledPaintings", masterpiecegamemodel.getShuffledPaintingIDs());
                game.put("PaintingBeingAuctioned","");
                game.put("CurrentBidder","");
                game.put(AppConstants.CURRENTBID,"0");
                game.put(AppConstants.COUNTNONBIDDERS,"0");
                game.put("BankPaintings", "");


                Firebase gamesRef = masterpieceRef.child("Games");
                final Firebase newGameRef = gamesRef.push();
                newGameRef.setValue((game), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        //progress.dismiss();
                        if (firebaseError != null) {
                            progress.dismiss();
                            Toast.makeText(CreateGameActivity.this, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
                            //textViewGameNumber.setText(firebaseError.getMessage().toString());
                        } else {
                            progress.dismiss();
                            masterpiecegamemodel.setGameNumber(game_number);
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
                Toast.makeText(CreateGameActivity.this, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onClick(View view){

            if (view == buttonJoinGame) {
                if (checkConnection.isConnected()) {
                Map<String, Object> player = new HashMap<String, Object>();
                //String[]paintings = {"1","2","3","4"};
                masterpiecegamemodel.setUserName(editTextUserName.getText().toString());
                player.put("Name", editTextUserName.getText().toString());
                player.put("Paintings", "");
                player.put("Cash", "1500000");//Initial cash
                player.put("BidAmount", "");
                player.put("Bidding", "");
                progress = ProgressDialog.show(this, "", "joining game ...", true);
                new Firebase(AppConstants.GameRef + "/" + "Players").push().setValue(player, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        //progress.dismiss();
                        if (firebaseError != null) {
                            //textViewGameNumber.setText(firebaseError.getMessage().toString());
                            progress.dismiss();
                            Toast.makeText(CreateGameActivity.this, firebaseError.getMessage().toString(), Toast.LENGTH_LONG);
                        } else {
                            progress.dismiss();
                            //textViewGameNumber.setText(game_number);
                            //lobby activity should come here
                            startActivity(new Intent(CreateGameActivity.this, LobbyActivity.class));
                        }
                    }
                });
            }
        }
    }
}
