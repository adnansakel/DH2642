package com.example.adnansakel.masterpiece;

import android.app.Application;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;

/**
 * Created by Daniel on 02/04/2016.
 * Responsible for getting and setting the model (MasterpieceGameModel)
 */
public class MasterpieceApplication extends Application{

    private MasterpieceGameModel model = new MasterpieceGameModel();

    public MasterpieceGameModel getModel() {
        return model;
    }

    public void setModel(MasterpieceGameModel model) {
        this.model = model;
}

}


