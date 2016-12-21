package com.example.enduser.procig;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Galeria extends AppCompatActivity {
    /* Atributos*/
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ImageView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    private byte[][] imagenesEnBytes;
    public static int posicion;
    private ImageView visor;
    private static TextView paginas;
    private static String[] imagenesEnString;
    //String a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_galeria);
        posicion = -1;
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (ImageView) findViewById(R.id.img_view_galeria_reporte);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        findViewById(R.id.btn_galeria_sig).setOnTouchListener(mDelayHideTouchListener);
        paginas = (TextView) findViewById(R.id.txv_galeria_paginas);
        visor = (ImageView) findViewById(R.id.img_view_galeria_reporte);

        visor.setImageBitmap(BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("0"), 0, getIntent().getByteArrayExtra("0").length));

        /*imagenesEnString= new String[]{getIntent().getStringExtra("0")};
        //imagenesEnString = getIntent().getStringArrayExtra("reportes");
        imagenesEnBytes = StringAArrayBytes(imagenesEnString);
        //imagenesEnString = getIntent().getStringArrayExtra("reportes");
        imagenSiguiente(null);
        contador();*/


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /*private byte[][] StringAArrayBytes(String[] arrayString) {
        byte[][] imagenes = new byte[arrayString.length][];
        for (int i = 0; i < arrayString.length; i++) {
            imagenes[i] = Base64.decode(arrayString[i].getBytes(), Base64.DEFAULT);
        }
        return imagenes;
    }*/

    public void imagenSiguiente(View v) {
        byte[] imageAsBytes;
        if (posicion < imagenesEnBytes.length - 1) {
            imageAsBytes = imagenesEnBytes[++posicion];
        } else {
            posicion = 0;
            imageAsBytes = imagenesEnBytes[posicion];
        }

        visor.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        contador();
    }

    public void ImagenPrevia(View v) {
        byte[] imageAsBytes;
        if (posicion > 0) {
            imageAsBytes = imagenesEnBytes[--posicion];
        } else {
            posicion = imagenesEnBytes.length - 1;
            imageAsBytes = imagenesEnBytes[posicion];
        }
        visor.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        contador();
    }

    private void contador() {
        paginas.setText((posicion + 1) + " de " + imagenesEnBytes.length);
    }

}
