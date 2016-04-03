package com.example.adnansakel.masterpiece.view;

import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.R;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class JoinGameView {
    View view;

    Button button_join_game;

    public JoinGameView(View view){
        // DM: TODO: Potentially we have to add the model here if we need to adapt the firebase when we create the game
        this.view = view;
        initialize();
    }

    private void initialize(){
        button_join_game = (Button)view.findViewById(R.id.buttonJoinGame);

        button_join_game.setText(R.string.join_game);
    }
}
