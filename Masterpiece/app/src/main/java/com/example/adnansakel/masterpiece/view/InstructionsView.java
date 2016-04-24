package com.example.adnansakel.masterpiece.view;

/**
 * Created by Daniel on 24/04/2016.
 */

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;

import java.util.Observable;
import java.util.Observer;

public class InstructionsView implements Observer {

    View view;

    Button button_back;

    MasterpieceGameModel masterpieceGameModel;


    public InstructionsView(View view, MasterpieceGameModel masterpieceGameModel){// not passing model as this view will not change its data
        masterpieceGameModel.addObserver(this);
        this.masterpieceGameModel = masterpieceGameModel;
        this.view = view;
        initialize();
    }

    private void initialize(){
        button_back = (Button)view.findViewById(R.id.button_back);
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}