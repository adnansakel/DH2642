package com.example.adnansakel.masterpiece.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.R;
import com.example.adnansakel.masterpiece.model.AppConstants;
import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.example.adnansakel.masterpiece.model.Painting;
import com.example.adnansakel.masterpiece.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniel on 02/04/2016.
 */
public class MainGameView implements Observer{
    View view;
    MasterpieceGameModel model;
    //Map<String, View> PaintingNameToViewMap = new HashMap<>();
    LinearLayout personalImageLayout;
    LinearLayout.LayoutParams layoutParams;
    LayoutInflater layoutInflater;

    RelativeLayout layoutStatusPopup;
    RelativeLayout layoutPopupGameModelSelection;
    RelativeLayout layoutPopupPrivateAuctionInProgress;
    RelativeLayout layoutPopupBankAuctionInProgress;
    RelativeLayout layoutPopupPrivateAuctionSelectPainting;
    RelativeLayout layoutPopupBankAuctionBegin;
    RelativeLayout layoutPopupPrivateAuctionBid;
    RelativeLayout layoutPopupBankAuctionBid;


    public MainGameView(View view, MasterpieceGameModel model) {
        model.addObserver(this);
        this.model = model;
        this.view = view;

        layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initialize(){

        //find the views that will be needed
        layoutStatusPopup = (RelativeLayout)view.findViewById(R.id.fullscreenStatusPopup);
        layoutPopupGameModelSelection = (RelativeLayout)view.findViewById(R.id.game_mode_selection_view);
        layoutPopupPrivateAuctionInProgress = (RelativeLayout)view.findViewById(R.id.privateauction_inprogress_view);
        layoutPopupBankAuctionInProgress = (RelativeLayout)view.findViewById(R.id.bankauction_inprogress_view);
        layoutPopupPrivateAuctionSelectPainting = (RelativeLayout)view.findViewById(R.id.privateauction_select_painting_view);
        layoutPopupBankAuctionBegin = (RelativeLayout)view.findViewById(R.id.start_bankauction_view);
        layoutPopupPrivateAuctionBid = (RelativeLayout)view.findViewById(R.id.privateauction_bid_view);
        layoutPopupBankAuctionBid = (RelativeLayout)view.findViewById(R.id.bankauction_bid_view);

        //set the popup content views to invisible
        layoutPopupGameModelSelection.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionInProgress.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionInProgress.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionSelectPainting.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionBegin.setVisibility(View.INVISIBLE);
        layoutPopupPrivateAuctionBid.setVisibility(View.INVISIBLE);
        layoutPopupGameModelSelection.setVisibility(View.INVISIBLE);
        layoutPopupBankAuctionBid.setVisibility(View.INVISIBLE);
    }

    public void populatePaintingsOtherPlayers(Player selectedPlayer){

        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfMyPlayer);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        for (int paintingID : selectedPlayer.getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 100, 100, true));
            layoutPaintingsOtherPlayers.addView(singlePainting, layoutParams);
        }
        /*
        Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.monalisa);
        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfOtherPlayers);

        // Cleanup if paintings are already added to the layout
        if(layoutPaintingsOtherPlayers.getChildCount() > 0) layoutPaintingsOtherPlayers.removeAllViews();
        System.out.println("Removed all views from HSV");

        // DM TODO: use actual information from local model
        for (Integer paintingID : selectedPlayer.getOwnedPaintingIDs()) {
            System.out.println("paintingID = " + paintingID);
            createLayoutWithBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPainting(0).getImagebytearray(), 0,
                    model.getPainting(0).getImagebytearray().length), 100, 100, true));
            layoutPaintingsOtherPlayers.addView(createLayoutWithBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPainting(paintingID).getImagebytearray(), 0,
                    model.getPainting(paintingID).getImagebytearray().length), 100, 100, true)));
            System.out.println("ADDING OTHER PLAYER PAINTINGS");
        }

        // DM TODO: remove old paintings if player is switched
        */
    }

    public void populatePaintingsMyPlayer(Player myPlayer){
        /*for(Painting painting : model.getAllPaintings()){
            System.out.println("Image size: "+painting.getImagebytearray().length);
        }*/

        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfMyPlayer);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2,0,2,0);

        for(int paintingID: myPlayer.getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 100, 100, true));
            layoutPaintingsOtherPlayers.addView(singlePainting, layoutParams);
        }
        /*
        Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.monalisa);
        LinearLayout layoutPaintingsMyPlayer = (LinearLayout)view.findViewById(R.id.llPaintingsOfMyPlayer);

        // Cleanup if paintings are already added to the layout
        if(layoutPaintingsMyPlayer.getChildCount() > 0) layoutPaintingsMyPlayer.removeAllViews();

        // DM TODO: use actual information from local model
        for (Integer paintingID : myPlayer.getOwnedPaintingIDs()) {

            layoutPaintingsMyPlayer.addView(createLayoutWithBitmapAndValue(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPainting(paintingID).getImagebytearray(), 0,
                    model.getPainting(paintingID).getImagebytearray().length), 100, 100, true),
                    model.getPainting(paintingID).getValue()));
            System.out.println("ADDING MYPLAYER PAINTINGS");
        }
        */
    }

    public LinearLayout createLayoutWithBitmap(Bitmap bm){
        LinearLayout layout = new LinearLayout(view.getContext());
        //layout.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(view.getContext());
        LinearLayout.LayoutParams ivLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(ivLayout);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        layout.addView(imageView);
        return layout;
    }

    public LinearLayout createLayoutWithBitmapAndValue(Bitmap bm, Integer sv){
        // Creating a layout which will include an imageView and a textView
        LinearLayout layout = new LinearLayout(view.getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);

        // defining the ImageView with the Bitmap
        ImageView imageView = new ImageView(view.getContext());
        LinearLayout.LayoutParams ivLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(ivLayout);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        // defining the textView for the SecretValue
        TextView textView = new TextView(view.getContext());
        textView.setLayoutParams(ivLayout);
        textView.setText(sv);
        textView.setTextColor(AppConstants.MAINCOLORINT);

        // adding both views
        layout.addView(imageView);
        layout.addView(textView);
        return layout;
    }

    // TODO Remove if not needed
    /*private void addViewsOfPersonalPaintings() {
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
    */

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof  MasterpieceGameModel){

            //if model's popupContent changed
            if(data.toString()=="popupContentChanged"){

                //set layout as visible for each case (and probably need to set all others to invisible)
                if(model.getPopupContent()== "startTurn") {
                    //show the start turn popup layout (game model selection)
                    layoutPopupGameModelSelection.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "privateAuctionInProgress") {
                    layoutPopupPrivateAuctionInProgress.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "bankAuctionInProgress") {
                    layoutPopupBankAuctionInProgress.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "privateAuctionSelectPainting") {
                    layoutPopupPrivateAuctionSelectPainting.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "bankAuctionBegin") {
                    layoutPopupBankAuctionBegin.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "privateAuctionBid") {
                    layoutPopupPrivateAuctionBid.setVisibility(View.VISIBLE);
                }
                else if(model.getPopupContent()== "bankAuctionBid") {
                    layoutPopupBankAuctionBid.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
