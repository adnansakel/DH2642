package com.example.adnansakel.masterpiece;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.view.InstructionsView;
import com.firebase.client.Firebase;

/**
 * Created by Daniel on 24/04/2016.
 */
public class InstructionsActivity extends Activity implements View.OnClickListener {

    Button button_back;

    InstructionsView instructionsView;
    MasterpieceGameModel masterpieceGameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default call to load previous state
        super.onCreate(savedInstanceState);

        // Set the view for the main activity screen
        // it must come before any call to findViewById method
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_game_instructions);

        masterpieceGameModel = ((MasterpieceApplication)this.getApplication()).getModel();

        // Creating the view class instance
        instructionsView = new InstructionsView(findViewById(R.id.game_instructions_popup),masterpieceGameModel);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == button_back){
            startActivity(new Intent(InstructionsActivity.this, HomeActivity.class));
        }
    }
}
