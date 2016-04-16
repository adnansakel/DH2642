package com.example.adnansakel.masterpiece;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnansakel.masterpiece.model.MasterpieceGameModel;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtHello;
    MasterpieceGameModel masterpieceGameModel;
    int paintingCounter = 0;
    Handler handler;
    boolean mStopHandler = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        masterpieceGameModel = ((MasterpieceApplication) this.getApplication()).getModel();
       // MainView mainView = new MainView(findViewById(R.id.relative_layout_Hello),masterpieceGameModel);
        txtHello = ((TextView)findViewById(R.id.textViewHello));
        txtHello.setOnClickListener(this);
        ProgressDialog progress;
        progress = ProgressDialog.show(this, "", "Downloading paintings ...", true);
        ImageDownloader imageDownloader = new ImageDownloader(MainActivity.this,masterpieceGameModel);
        imageDownloader.downloadImages(progress);
        handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        if(v  == txtHello){
           // txtHello.setTextColor(Color.parseColor("#FF0000"));
            //masterpieceGameModel.setColor("#ff0000");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // do your stuff - don't create a new runnable here!
                    if(paintingCounter == masterpieceGameModel.getAllPaintings().size()){//10 images
                        handler.removeCallbacks(this);
                        //progress.dismiss();
                        System.out.println("done..."+paintingCounter);
                        mStopHandler = true;

                    }
                    else if(paintingCounter<masterpieceGameModel.getAllPaintings().size()){

                        Bitmap imgbmp = BitmapFactory.decodeByteArray(masterpieceGameModel.getPaintingbyPosition(paintingCounter).getImagebytearray()
                                , 0, masterpieceGameModel.getPaintingbyPosition(paintingCounter).getImagebytearray().length);

                        ((ImageView)findViewById(R.id.imgTest)).setImageBitmap(imgbmp);
                        paintingCounter++;


                    }
                    if (!mStopHandler) {
                        handler.postDelayed(this, 1000);
                    }
                }
            };

// start it with:
            handler.post(runnable);
        }

    }
}
