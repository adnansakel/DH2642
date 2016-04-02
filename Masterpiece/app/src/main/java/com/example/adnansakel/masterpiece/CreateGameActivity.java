package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.view.CreateGameView;
import com.example.adnansakel.masterpiece.view.HomeView;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class CreateGameActivity extends Activity implements View.OnClickListener {

    // Variable definition
    Button button_create_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_creategame);

        // Creating the view class instance
        CreateGameView createGameView = new CreateGameView(findViewById(R.id.creategame_view));

        button_create_game = (Button)findViewById(R.id.buttonCreateGame);
        button_create_game.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button_create_game) {
            //go to lobby activity
            // TODO: add the conditions that it only goes if the Game ID is successfully created in Firebase
            startActivity(new Intent(CreateGameActivity.this, LobbyActivity.class));
        }
    }
}
