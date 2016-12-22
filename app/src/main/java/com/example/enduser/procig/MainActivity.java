package com.example.enduser.procig;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;


import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.*;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private String dependencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spin_login);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dependencia=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        cargarDependencias();

    }

    public void LoginOnClick(View v) {
        Thread th = new Thread() {
            String res;
            EditText usuario = (EditText) findViewById(R.id.user);
            EditText password = (EditText) findViewById(R.id.pass);

            @Override
            public void run() {
                final String NAMESPACE = "http://saxsoft/MocrosoftWebService/";
                final String URL = "http://192.168.1.76/WEBSERVICE/REPORTES.ASMX";
                final String METHOD_NAME_LOGIN = "ACCESO";
                final String SOAP_ACTION = "http://saxsoft/MocrosoftWebService/ACCESO";

                SoapObject soapLogin = new SoapObject(NAMESPACE, METHOD_NAME_LOGIN);
                soapLogin.addProperty("USER", usuario.getText().toString());
                soapLogin.addProperty("PASS", password.getText().toString());
                soapLogin.addProperty("DEPENDENCIA", dependencia);
                soapLogin.addProperty("AEJERCICIO", "2016");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(soapLogin);

                HttpTransportSE transporte = new HttpTransportSE(URL);
                try {
                    transporte.call(SOAP_ACTION, envelope);
                    SoapObject body = (SoapObject) envelope.bodyIn;
                    res = body.getProperty(0).toString();

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!res.equalsIgnoreCase("INVALIDO")) {
                            Intent Abrir = new Intent(MainActivity.this, MenuActivity.class);
                            Abrir.putExtra("Usuario", res);
                            usuario.setText("");
                            password.setText("");
                            startActivity(Abrir);
                            //Toast.makeText(MainActivity.this,"Sesión Iniciada", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario Invalido", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        };
        th.start();
    }

    private void cargarDependencias() {
        Thread th = new Thread() {
            Spinner spinDependencias = (Spinner) findViewById(R.id.spin_login);
            ArrayList<String> dependencias = new ArrayList<String>();

            @Override
            public void run() {
                final String NAMESPACE = "http://saxsoft/MocrosoftWebService/";
                final String URL = "http://192.168.1.76/WEBSERVICE/REPORTES.ASMX";
                final String METHOD_NAME_DEPENDENCIAS = "retornardependecia";
                final String SOAP_ACTION = "http://saxsoft/MocrosoftWebService/retornardependecia";

                SoapObject soapLogin = new SoapObject(NAMESPACE, METHOD_NAME_DEPENDENCIAS);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(soapLogin);

                HttpTransportSE transporte = new HttpTransportSE(URL);
                try {
                    transporte.call(SOAP_ACTION, envelope);
                    SoapObject body;
                    if (envelope.bodyIn instanceof SoapObject) {
                        body = (SoapObject) envelope.getResponse();
                        for (int i = 0; i < body.getPropertyCount(); i++) {
                            String temp=body.getProperty(i).toString();
                            dependencias.add(temp);
                        }
                    }

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (dependencias.size() > 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, dependencias);
                            spinDependencias.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity.this, "No hubo respuesta del servidor", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        };
        th.start();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     * <p>
     * public Action getIndexApiAction() {
     * Thing object = new Thing.Builder()
     * .setName("Main Page") // TODO: Define a title for the content shown.
     * // TODO: Make sure this auto-generated URL is correct.
     * .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
     * .build();
     * return new Action.Builder(Action.TYPE_VIEW)
     * .setObject(object)
     * .setActionStatus(Action.STATUS_TYPE_COMPLETED)
     * .build();
     * }
     */
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.

    }
}