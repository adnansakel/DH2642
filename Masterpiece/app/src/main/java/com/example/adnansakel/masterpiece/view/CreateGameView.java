package com.example.adnansakel.masterpiece.view;

import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.R;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class CreateGameView {
    View view;

    Button button_create_game;

    public CreateGameView(View view){
        // DM: Potentially we have to add the model here if we need to adapt the firebase when we create the game
        this.view = view;
        initialize();
    }

    private void initialize(){
        button_create_game = (Button)view.findViewById(R.id.buttonCreateGame);

        button_create_game.setText(R.string.create_game);
    }
}
