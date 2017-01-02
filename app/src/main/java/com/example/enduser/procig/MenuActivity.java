package com.example.enduser.procig;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String reporte, mes;
    private Spinner spinReporte, spinMes;
    private ImageView img;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        String nombre = getIntent().getStringExtra("Usuario");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView) header.findViewById(R.id.name_user);
        name.setText(nombre);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.reportes_contables) {
            setTitle("Reportes contables");
            fm.beginTransaction().replace(R.id.content_menu, new ReportesContablesFragment()).commit();
            /*/ Handle the camera action
            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.content_menu);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_principal, null);
            mainLayout.removeAllViews();
            mainLayout.addView(layout);*/
        } else if (id == R.id.reportes_presupuestales) {
            setTitle("Reportes presupuestales");
            fm.beginTransaction().replace(R.id.content_menu, new ReportesPresupuestalesFragment()).commit();
        } else if (id == R.id.cerrar_sesion) {
            cerrarSesion();
        } /*else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            onBackPressed();
        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void generarReportePrueba(View v) {
        Thread th = new Thread() {
            String pagina1;
            @Override
            public void run() {
                String NAMESPACE = "http://saxsoft/MocrosoftWebService/";
                String URL = "http://192.168.1.76/WEBSERVICE/REPORTES.ASMX";
                String METHOD_NAME = "reporte";
                String SOAP_ACTION = "http://saxsoft/MocrosoftWebService/reporte";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                obtenerDatos();
                request.addProperty("reportex", reporte);
                request.addProperty("mes", mes);

                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE transporte = new HttpTransportSE(URL);
                try {
                    transporte.call(SOAP_ACTION, envelope);
                    if (envelope.bodyIn instanceof SoapFault) {
                        //Exception
                    } else {
                        SoapObject body = (SoapObject) envelope.getResponse();
                        pagina1 = body.getProperty(0).toString();
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] imageAsBytes = Base64.decode(pagina1.getBytes(), Base64.DEFAULT);
                        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        }
                });
            }
        };
        th.start();
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void abrirGaleria(View v) {
        Intent galeria = new Intent(this, Galeria.class);
        galeria.putExtra("reporte", reporte);
        galeria.putExtra("mes", mes);
        startActivity(galeria);
    }

    public void obtenerDatos() {
        String x = getTitle().toString();
        switch (x) {
            case "Reportes presupuestales":
                spinReporte = (Spinner) findViewById(R.id.spin_reporte_presupuestal);
                spinMes = (Spinner) findViewById(R.id.spin_reporte__presupuestal_mes);
                img = (ImageView) findViewById(R.id.img_view_reporte_presupuestal);

                break;
            case "Reportes contables":
                spinReporte = (Spinner) findViewById(R.id.spin_reporte_contable);
                spinMes = (Spinner) findViewById(R.id.spin_reporte_contable_mes);
                img = (ImageView) findViewById(R.id.img_view_reporte_contable);
                break;
        }
        mes = spinMes.getSelectedItem().toString();
        reporte = spinReporte.getSelectedItem().toString();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Menu Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
