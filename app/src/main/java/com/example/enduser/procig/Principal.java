package com.example.enduser.procig;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



    }

   /* public void Btn_Imagen(View v)
    {
        Thread th = new Thread() {
            String res;
            String countpr;
            String counta;

            EditText usuario = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);

            @Override
            public void run() {
                String NAMESPACE = "http://saxsoft/MocrosoftWebService/";
                String URL = "http://192.168.1.76/WEBSERVICE/REPORTES.ASMX";
                String METHOD_NAME = "reporte";
                String SOAP_ACTION = "http://saxsoft/MocrosoftWebService/reporte";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("USER", usuario.getText().toString());
                request.addProperty("PASS", password.getText().toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transporte = new HttpTransportSE(URL);
                try {
                    transporte.call(SOAP_ACTION, envelope);
                    SoapObject body = (SoapObject)envelope.bodyIn;
                    res = body.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (res.equals("1")) {

                        }

                    }
                });
            }
        };

        th.start();


        /*Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);*/


    //}


}
