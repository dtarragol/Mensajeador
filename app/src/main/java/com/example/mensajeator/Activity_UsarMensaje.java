package com.example.mensajeator;

import static com.example.mensajeator.AdminSQLiteOpenHelper.TABLE_MENSAJE_COLOQUIAL;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity_UsarMensaje extends AppCompatActivity {

    LinearLayout lista;
    Button BT;
    List<Integer> IDS = new ArrayList<>();
    List<Integer> IDS_linea = new ArrayList<>();
    int numeroBoton=1;
    String nombre, numero, prefijo="";


    public interface OnStringEnteredListener {
        void onStringEntered(String input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_usar_mensaje);
        lista=findViewById(R.id.glay);
        nombre = getIntent().getStringExtra("nombre");
        numero = getIntent().getStringExtra("numero");

        DialogUtils.showInputDialog(this,"Introduce el prefijo.\n*Si el telèfono esta guardado con prefijo, no introduzca nada.", new DialogUtils.OnStringEnteredListener(){
            @Override
            public void onStringEntered(String input) {
                prefijo=input;
            }
        });
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase BD = admin.getReadableDatabase();
        String c = "SELECT * FROM "+ TABLE_MENSAJE_COLOQUIAL;
        Cursor consulta = BD.rawQuery(c, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(consulta.moveToFirst()) {
            do {

                String temp_id = consulta.getString(0);
                String temp_nombre = consulta.getString(1);
                String temp_mensaje = consulta.getString(2);

                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);


                rowLayout.setId(View.generateViewId());
                int RLID = rowLayout.getId();
                IDS_linea.add(RLID);

                BT = new Button(this);
                BT.setText(temp_nombre);

                BT.setBackgroundColor(Color.DKGRAY);

                int marginInPixels = 10; // Obtén el tamaño del margen desde los recursos
                layoutParams.setMargins(0, 0, 0, marginInPixels); // Establece el margen en la parte inferior

                BT.setLayoutParams(layoutParams);

                BT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_VIEW);
                        String uri = "https://api.whatsapp.com/send?phone="+prefijo+numero+"&text="+temp_mensaje;
                        sendIntent.setData(Uri.parse(uri));
                        startActivity(sendIntent);
                    }
                });
                BT.setId(View.generateViewId());
                int btnID= BT.getId();
                IDS.add(btnID);

                rowLayout.addView(BT);
                lista.addView(rowLayout);
                numeroBoton++;
            }while (consulta.moveToNext());
        }
        BD.close();
    }
    //boton para volver a MAIN
    public void btnVolver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}