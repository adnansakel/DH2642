package com.example.adnansakel.masterpiece.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import org.w3c.dom.Text;

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
    TextView textSecretValue;
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

    public void populatePaintingsOtherPlayers(Integer selectedPlayerID){
        System.out.println("TEST");
        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfOtherPlayers);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        for (int paintingID : model.getPlayer(selectedPlayerID).getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 200, 200, true));

            layoutPaintingsOtherPlayers.addView(singlePainting, layoutParams);
        }
    }

    public void populatePaintingsMyPlayer(Integer myPlayerID){
        LinearLayout layoutPaintingsOtherPlayers = (LinearLayout)view.findViewById(R.id.llPaintingsOfMyPlayer);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 0, 2, 0);

        for(int paintingID: model.getPlayer(myPlayerID).getOwnedPaintingIDs()) {
            View singlePainting = layoutInflater.inflate(R.layout.item_image_value, null);
            ImageView image = (ImageView) singlePainting.findViewById(R.id.imgPainting);
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(
                    model.getPaintingbyPosition(paintingID).getImagebytearray(), 0,
                    model.getPaintingbyPosition(paintingID).getImagebytearray().length), 200, 200, true));

            TextView textSecretValue = (TextView) singlePainting.findViewById(R.id.txtSecretValue);
            System.out.println("TextView text: " + textSecretValue.getText());
            System.out.println(model.getPainting(paintingID).getValue());
            String test = String.valueOf(model.getPainting(paintingID).getValue());
            textSecretValue.setText(test);
            textSecretValue.setWidth(200);

            layoutPaintingsOtherPlayers.addView(singlePainting, layoutParams);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof  MasterpieceGameModel){

            //if model's popupContent changed
            if(data.toString()=="popupContentChanged"){

                //TODO: set all popup layouts to invisible before setting one to visible (otherwise multiple layouts will be visible)

                //set layout as visible for each case
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

            if(data.toString()=="currentPlayerToDisplayChangedID") {

                //populate images
                populatePaintingsOtherPlayers(model.getCurrentPlayerToDisplayID());

                //TODO maybe add the highlighting of the buttons
            }

            //TODO cashChanged - observable in Player
            if(data.toString()=="cashChanged") {
                TextView textCash = (TextView) view.findViewById(R.id.txtPlayerCash);
                textCash.setText(model.getMyPlayer().getCash());
            }

        }
    }
}
