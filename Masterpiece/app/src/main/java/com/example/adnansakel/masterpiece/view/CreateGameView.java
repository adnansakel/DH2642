package com.example.adnansakel.masterpiece.view;

import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class CreateGameView implements Observer {
    View view;

    Button button_join_game;
    
    MasterpieceGameModel masterpieceGameModel;

    public CreateGameView(View view, MasterpieceGameModel masterpieceGameModel){
        // DM: Potentially we have to add the model here if we need to adapt the firebase when we create the game
        masterpieceGameModel.addObserver(this);
        this.masterpieceGameModel = masterpieceGameModel;
        this.view = view;
        initialize();
    }

    private void initialize(){
        button_join_game = (Button)view.findViewById(R.id.buttonJoinGame);

        button_join_game.setText(R.string.join_game);
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
