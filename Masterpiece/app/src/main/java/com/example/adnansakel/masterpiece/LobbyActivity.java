package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.model.AppConstants;
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

public class LobbyActivity extends Activity implements View.OnClickListener {

    // Variable definition
    Button button_create_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_lobby);

        // Creating the view class instance
        LobbyView lobbyView = new LobbyView(findViewById(R.id.lobby_view));

        button_create_game = (Button)findViewById(R.id.buttonCreateGame);
        button_create_game.setOnClickListener(this);
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

