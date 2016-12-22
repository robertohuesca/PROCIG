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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

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


    private static int posicion;
    private static TextView paginas;
    //String a, b, c;
    private byte[][] imagenesEnBytes;
    private String reporte, mes;
    int c = 0;

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
        reporte = getIntent().getStringExtra("reporte");
        mes = getIntent().getStringExtra("mes");
        generarReportePrueba(null);
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

    private void StringAArrayBytes(String[] arrayString) {
        imagenesEnBytes = new byte[arrayString.length][];
        for (int i = 0; i < arrayString.length; i++) {
            imagenesEnBytes[i] = Base64.decode(arrayString[i].getBytes(), Base64.DEFAULT);
        }

    }

    public void imagenSiguiente(View v) {
        byte[] imageAsBytes;
        if (posicion < imagenesEnBytes.length - 1) {
            imageAsBytes = imagenesEnBytes[++posicion];
        } else {
            posicion = 0;
            imageAsBytes = imagenesEnBytes[posicion];
        }
        mContentView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
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
        mContentView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        contador();
    }

    private void contador() {
        paginas.setText((posicion + 1) + " de " + imagenesEnBytes.length);
    }

    public void generarReportePrueba(View v) {

        Thread th = new Thread() {
            @Override
            public void run() {
                String[] imagenesEnString = null;
                String NAMESPACE = "http://saxsoft/MocrosoftWebService/";
                String URL = "http://192.168.1.76/WEBSERVICE/REPORTES.ASMX";
                String METHOD_NAME = "reporte";
                String SOAP_ACTION = "http://saxsoft/MocrosoftWebService/reporte";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("reportex", reporte);
                request.addProperty("mes", mes);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE transporte = new HttpTransportSE(URL);
                try {
                    transporte.call(SOAP_ACTION, envelope);
                    if (envelope.bodyIn instanceof SoapFault) {
                        //Exception
                    } else {
                        SoapObject body = (SoapObject) envelope.getResponse();
                        c = body.getPropertyCount();
                        imagenesEnString = new String[c];
                        for (int i = 0; i < imagenesEnString.length; i++) {
                            imagenesEnString[i] = body.getProperty(i).toString();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                final String[] finalImagenesEnString = imagenesEnString;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringAArrayBytes(finalImagenesEnString);
                        imagenSiguiente(null);
                    }
                });

            }
        };
        th.start();
    }

}
