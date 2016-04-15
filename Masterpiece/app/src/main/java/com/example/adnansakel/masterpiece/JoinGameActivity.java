package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.example.adnansakel.masterpiece.view.JoinGameView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class JoinGameActivity extends Activity implements View.OnClickListener {
    // Variables for checking the internet connection status
    Boolean isConnected = false;
    ConnectionCheck checkConnection;

    // Variable definition
    Button button_join_game;
    ProgressDialog progress;
    EditText editTextUserName;
    EditText editTextGameNumber;

    MasterpieceGameModel masterpieceGameModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_joingame);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(JoinGameActivity.this);

        masterpieceGameModel = masterpieceGameModel = ((MasterpieceApplication)this.getApplication()).getModel();
        // Creating the view class instance
        JoinGameView joinGameView = new JoinGameView(findViewById(R.id.joingame_view),masterpieceGameModel);

        Firebase.setAndroidContext(this);

        initializeComponent();

    }

    private void initializeComponent(){
        button_join_game = (Button)findViewById(R.id.buttonJoinGame);
        button_join_game.setOnClickListener(this);

        editTextUserName = (EditText)findViewById(R.id.edittext_userName);
        editTextGameNumber = (EditText)findViewById(R.id.edittext_GameNumber);

    }

    @Override
    public void onClick(View v) {
        if (v == button_join_game) {
            if (checkConnection.isConnected()) {
                progress = ProgressDialog.show(this, "", "joining game ...", true);
                /*
                * Joing game is done in two steps.
                * 1. Look for the GameID in fire base and get the Firebase key associated with that GameID
                * 2. Use the Firebase key got from step 1 to generate the Firebase reference to add new player in the game
                * */

                /*
                * Step 1 as follows
                * */
                Firebase ref = new Firebase(AppConstants.FireBaseUri+"/"+"Games");
                Query qref = ref.orderByChild("Game").equalTo(editTextGameNumber.getText().toString());
                qref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        System.out.println(snapshot.toString());
                        System.out.println(snapshot.getKey().toString());

                        AppConstants.GameRef = AppConstants.FireBaseUri + "/" + "Games" + "/" + snapshot.getKey().toString();
                        Map<String, Object> player = new HashMap<String, Object>();
                        //String[]paintings = {"1","2","3","4"};
                        player.put("Name", editTextUserName.getText().toString());
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
                                } else {
                                    progress.dismiss();
                                    //textViewGameNumber.setText(game_number);
                                    //lobby activity should come here
                                    AppConstants.PlayerRef = firebase.getRef().toString();//Ref for player who is playing on this device
                                    System.out.println("Player ref:" + AppConstants.PlayerRef);
                                    AppConstants.GameID = editTextGameNumber.getText().toString();
                                    masterpieceGameModel.setGameNumber(AppConstants.GameID);
                                    startActivity(new Intent(JoinGameActivity.this, LobbyActivity.class));
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

            });}
        }
    }
}
