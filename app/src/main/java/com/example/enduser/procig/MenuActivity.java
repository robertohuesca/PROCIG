package com.example.enduser.procig;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Html;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
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

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String reporte, mes;
    private Spinner spinReporte, spinMes;
    private ImageView img;
    private boolean abrir;
    PhotoViewAttacher mAttacher;
    private String[] paginas;
    ProgressDialog progressDialog;
    FragmentManager fm = getSupportFragmentManager();
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final File dirReportes = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/reportes Procig");

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

        com.getbase.floatingactionbutton.FloatingActionButton fab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_compartir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartirFloat();
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton fab2 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_guardar);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarFloat();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*poner nombre de usuario*/
        String nombre = getIntent().getStringExtra("Usuario");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.name_user);
        name.setText(nombre);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setTitle("Inicio");
        fm.beginTransaction().replace(R.id.content_menu, new Inicio_Fragment()).commit();

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
            abrirGaleria();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.reportes_financieros) {
            setTitle("Reportes");
            fm.beginTransaction().replace(R.id.content_menu, new ReportesPresupuestalesFragment()).commit();
        } else if (id == R.id.cerrar_sesion) {
            cerrarSesion();
        } else if (id == R.id.Conocenos) {
            Intent Abrir = new Intent(this, Sax.class);
            startActivity(Abrir);
        } else if (id == R.id.Inicio) {
            setTitle("Inicio");
            fm.beginTransaction().replace(R.id.content_menu, new Inicio_Fragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void generarReportePrueba(View v) {
        progressDialog= ProgressDialog.show(MenuActivity.this, "","Cargando...");
        Thread th = new Thread() {
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
                    if ((envelope.bodyIn instanceof SoapFault)) {
                        Toast.makeText(MenuActivity.this, "No hubo respuesta", Toast.LENGTH_SHORT).show();
                    } else {
                        SoapObject body = (SoapObject) envelope.getResponse();
                        int c = body.getPropertyCount();
                        paginas = new String[c];
                        for (int i = 0; i < paginas.length; i++) {
                            paginas[i] = body.getProperty(i).toString();
                        }
                    }
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        byte[] imageAsBytes = Base64.decode(paginas[0].getBytes(), Base64.DEFAULT);
                        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        mAttacher = new PhotoViewAttacher(img);
                        abrir = true;
                        progressDialog.dismiss();

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

    public void abrirGaleria() {
        if (abrir) {
            Intent galeria = new Intent(this, Galeria.class);
            galeria.putExtra("reporte", reporte);
            galeria.putExtra("mes", mes);
            startActivity(galeria);
        }

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        // (not needed unless you are going to change the drawable later)

    }

    public void obtenerDatos() {
        String x = getTitle().toString();
        switch (x) {
            case "Reportes":
                spinReporte = (Spinner) findViewById(R.id.spin_reporte_presupuestal);
                spinMes = (Spinner) findViewById(R.id.spin_reporte__presupuestal_mes);
                img = (ImageView) findViewById(R.id.img_view_reporte_presupuestal);
                break;
        }
        mes = spinMes.getSelectedItem().toString();
        reporte = spinReporte.getSelectedItem().toString();
    }

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


    final void compartirFloat() {
        if (abrir) {
            int length = paginas.length;
            File cache = getApplicationContext().getExternalCacheDir();
            File[] archivos = new File[length];
            for (int i = 0; i < length; i++) {
                archivos[i] = new File(cache, "reporte " + i + ".png");
                byte[] imageAsBytes = Base64.decode(paginas[i].getBytes(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                try {
                    FileOutputStream out = new FileOutputStream(archivos[i]);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Toast.makeText(this, "Error al compartir", Toast.LENGTH_SHORT).show();
                }
            }
            // Now send it out to share

            ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
            for (int i = 0; i < length; i++) {
                imageUriArray.add(Uri.parse("file://" + archivos[i]));
            }
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, imageUriArray);
            try {
                startActivity(Intent.createChooser(share, "Share Report"));
            } catch (Exception e) {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        }

    }

    final void guardarFloat() {
        if (abrir) { /*si hay imagenes en el imageView*/
            int permissionCheck = ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE); /*checa si tiene permiso de escribir*/
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) { /*checa si tiene permiso*/
                guardar();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    guardar();
                } else {
                    Toast.makeText(this, "La aplicación no tiene permiso para almacenar archivos", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void guardar() {
        if (!dirReportes.exists()) {
            dirReportes.mkdir();/*si no existe, crea el directorio*/
            Thread guardar = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean save = true;
                    for (int i = 0; i < paginas.length; i++) {
                        byte[] imageAsBytes = Base64.decode(paginas[i].getBytes(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        String fotoname = "pag " + i + " - reporte " + reporte + "-" + mes + ".jpg";
                        File file = new File(dirReportes, fotoname);
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            save = false;
                        }
                    }
                    if (save) {
                        Toast.makeText(MenuActivity.this, "Guardados en " + Environment.DIRECTORY_DCIM + "/reportes PROCIG", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MenuActivity.this, "Ocurrieron problemas al guardar", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            guardar.run();
        }
    }

}
