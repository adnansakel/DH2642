package com.example.adnansakel.masterpiece.view;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Player;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniel on 02/04/2016.
 */
public class LobbyView implements Observer {
    View view;

    Button button_start_game;

    TextView textViewPlayer1;
    TextView textViewPlayer2;
    TextView textViewPlayer3;
    TextView textViewPlayer4;

    TextView textViewGameNumber;

    MasterpieceGameModel masterpiecegamemodel;
    public LobbyView(View view, MasterpieceGameModel masterpiecegamemodel){
        // DM: Potentially we have to add the model here if we need to adapt the firebase when we create the game
        this.view = view;
        masterpiecegamemodel.addObserver(this);
        this.masterpiecegamemodel = masterpiecegamemodel;
        initialize();
    }

    private void initialize(){
        button_start_game = (Button)view.findViewById(R.id.buttonStartGame);

        textViewPlayer1 = (TextView)view.findViewById(R.id.textviewPlayerOne);
        textViewPlayer2 = (TextView)view.findViewById(R.id.textviewPlayerTwo);
        textViewPlayer3 = (TextView)view.findViewById(R.id.textviewPlayerThree);
        textViewPlayer4 = (TextView)view.findViewById(R.id.textviewPlayerFour);
        textViewGameNumber = (TextView)view.findViewById(R.id.textviewGameNumber);

        textViewGameNumber.setText(AppConstants.GameID);

        button_start_game.setText(R.string.start_game);

        button_start_game.setVisibility(View.INVISIBLE);

        if(AppConstants.TotalNumberofPlayers==2){
            textViewPlayer4.setVisibility(view.INVISIBLE);
            textViewPlayer3.setVisibility(view.INVISIBLE);
        }
        else if(AppConstants.TotalNumberofPlayers == 3){
            textViewPlayer4.setVisibility(view.INVISIBLE);
        }

    }


    @Override
    public void update(Observable observable, Object o) {
        System.out.println("Observer worked");
        if(observable instanceof  MasterpieceGameModel){int i = 1;
            System.out.println("Observer worked");
            for(Player player : ((MasterpieceGameModel) observable).getAllPlayers()){
                if(i == 1){
                    textViewPlayer1.setText(player.getName());
                }
                else if(i == 2){
                    textViewPlayer2.setText(player.getName());
                }
                else if(i == 3){
                    textViewPlayer3.setText(player.getName());
                }
                else if(i == 4){
                    textViewPlayer4.setText(player.getName());
                    //if (AppConstants.IamCreator == true){
                    button_start_game.setVisibility(View.VISIBLE);
                    //}
                }

                if(i == AppConstants.TotalNumberofPlayers){
                    button_start_game.setVisibility(View.VISIBLE);
                }

                i++;
            }
        }
    }
}