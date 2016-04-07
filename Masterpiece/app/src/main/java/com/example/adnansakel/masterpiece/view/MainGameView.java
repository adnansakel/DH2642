package com.example.adnansakel.masterpiece.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameView implements Observer{
    View view;
    MasterpieceGameModel gameModel;
    //Map<String, View> PaintingNameToViewMap = new HashMap<>();
    LinearLayout personalImageLayout;
    LinearLayout.LayoutParams layoutParams;
    LayoutInflater layoutInflater;

    public MainGameView(View view, MasterpieceGameModel gameModel) {
        gameModel.addObserver(this);
        this.gameModel = gameModel;
        this.view = view;
        layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        personalImageLayout = (LinearLayout) view.findViewById(R.id.llPersonalImages);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);

        addViewsOfPersonalPaintings();
    }

    private void addViewsOfPersonalPaintings() {
        // Starter menu
        for (Painting personalPainting : gameModel.getPaintingsByPlayerID()) {

            //Replace with real bitmap
            //Bitmap monalisa = BitmapFactory.decodeResource(context.getResources(),
            //        R.drawable.monalisa);

            View paintingView = layoutInflater.inflate(R.layout.item_image, null);
            ImageView PaintingImage = (ImageView) paintingView.findViewById(R.id.imgPainting);
            PaintingImage.setImageBitmap(personalPainting.getImage());

            // TODO: Implement big version view onClick
            // PaintingImage.setOnClickListener(new PaintingOnClickListener(personalPainting));

            //dishNameToViewMap.put(starterDish.getName(), starterView);
            personalImageLayout.addView(paintingView, layoutParams);
        }
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
