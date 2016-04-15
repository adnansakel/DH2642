package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Player;
import com.example.adnansakel.masterpiece.view.CreateGameView;
import com.example.adnansakel.masterpiece.view.HomeView;
import com.example.adnansakel.masterpiece.view.LobbyView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Daniel on 02/04/2016.
 */

// TODO: We have to distinguish the LobbyActivity for participants and the game creator, as only the game creator should be able to start/create the game in the lobby?
// TODO: If it is complex, everybody can start it as long as it is 4 people.
/*
*I think lobby activity should finish itself as soon as there are four players are available and should proceed to MainGameActivity
* immediately ---- Commented by Sakel
 *  */



public class LobbyActivity extends Activity implements View.OnClickListener {
    // Variables for checking the internet connection status
    Boolean isConnected = false;
    ConnectionCheck checkConnection;

    // Variable definition
    Button button_create_game;
    MasterpieceGameModel masterpiecegamemodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_lobby);

        // Create Internet Check Instance
        checkConnection = new ConnectionCheck(LobbyActivity.this);

        masterpiecegamemodel = ((MasterpieceApplication)this.getApplication()).getModel();
        // Creating the view class instance
        LobbyView lobbyView = new LobbyView(findViewById(R.id.lobby_view),masterpiecegamemodel);

        button_create_game = (Button)findViewById(R.id.buttonStartGame);
        button_create_game.setOnClickListener(this);

        if (checkConnection.isConnected()) {
            listentoFirebaseforPlayers();
        }
    }

    private void listentoFirebaseforPlayers(){
        Firebase.setAndroidContext(this);
        new Firebase(AppConstants.GameRef+"/"+"Players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                masterpiecegamemodel.removeAllPlayer();
                for(DataSnapshot dsplayer : dataSnapshot.getChildren()){
                    //DataSnapshot dpl = (DataSnapshot)dsplayer.getValue();
                    //System.out.println(dsplayer.child("Name"));
                    Player player = new Player();
                    player.setName(dsplayer.child("Name").getValue().toString());
                    player.setFirebaseid(dsplayer.getKey().toString());
                    masterpiecegamemodel.addPlayer(player);
                   // masterpiecegamemodel.notifyObservers();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == button_create_game) {
            //go to lobby activity
            // TODO: add the conditions that it only moves to MainGameActivity if the Game is successfully setup in Firebase
            startActivity(new Intent(LobbyActivity.this, MainGameActivity.class));
        }
    }
}

