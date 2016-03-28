package com.example.adnansakel.masterpiece.view;

import android.view.View;
import android.widget.Button;

import com.example.adnansakel.masterpiece.R;

/**
 * Created by Adnan Sakel on 3/28/2016.
 */
public class HomeView {

    View view;

    Button button_create_game;
    Button button_join_game;



    public HomeView(View view){// not passing model as this view will not change its data

        this.view = view;
        initialize();
    }

    private void initialize(){
        button_create_game = (Button)view.findViewById(R.id.buttonCreateGame);
        button_join_game = (Button)view.findViewById(R.id.buttonJoinGame);

        button_create_game.setText(R.string.create_game);
        button_join_game.setText(R.string.join_game);
    }
}
