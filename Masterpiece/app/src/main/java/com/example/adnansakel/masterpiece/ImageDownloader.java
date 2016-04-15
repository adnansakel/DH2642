package com.example.adnansakel.masterpiece;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

import android.graphics.BitmapFactory;
import android.os.Handler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Adnan Sakel on 4/14/2016.
 */
public class ImageDownloader {
    Context context;
    List<Bitmap> images;
    AsyncHttpClient client;
    Handler handler;
    MasterpieceGameModel masterpieceGameModel;
    boolean IsDownloadCompleted;
    boolean downloadFailed;
    int paintingCounter = 0;
    boolean mStopHandler = false;

    ImageDownloader(Context context, MasterpieceGameModel masterpieceGameModel){
        this.context = context;
        this.masterpieceGameModel = masterpieceGameModel;
        images = new ArrayList<Bitmap>();
        client = new AsyncHttpClient();
        handler = new Handler();
        IsDownloadCompleted = true;
        downloadFailed = false;

    }

    public void downloadImages( final ProgressDialog progress){


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if(paintingCounter == 10 || downloadFailed){//10 images
                    handler.removeCallbacks(this);
                    progress.dismiss();
                    System.out.println("done..."+images.size());
                    mStopHandler = true;

                }
                else if(IsDownloadCompleted){

                    downloadImage(masterpieceGameModel.getPaintingbyPosition(paintingCounter).getImageURL());

                }
                if (!mStopHandler) {
                    handler.postDelayed(this, 500);
                }
            }
        };

// start it with:
        handler.post(runnable);

    }

    private void downloadImage(String imageUrl){
        client = new AsyncHttpClient();
        client.get(imageUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                IsDownloadCompleted = false;
                downloadFailed = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                //add painting in game model;
                Bitmap imgbmp = BitmapFactory.decodeByteArray(response, 0, response.length);
                //images.add(imgbmp);
                masterpieceGameModel.getPaintingbyPosition(paintingCounter).setImage(imgbmp);
                IsDownloadCompleted = true;
                System.out.println("Success");
                paintingCounter ++;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                downloadFailed = true;
                System.out.println("Failed"+statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
