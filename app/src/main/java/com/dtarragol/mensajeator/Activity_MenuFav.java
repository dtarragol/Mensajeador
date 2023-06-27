package com.dtarragol.mensajeator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dtarragol.mensajeator.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_MenuFav extends AppCompatActivity {

    LinearLayout lista;
    Button BT;
    int numeroBoton=1;
    List<Integer> IDS = new ArrayList<>();
    List<Integer> IDS_linea = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_menu_fav);
        lista=findViewById(R.id.glay);
        lista.removeAllViews();
        inicicializar();
    }
    public void inicicializar(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase BD = admin.getReadableDatabase();
        String c = "SELECT * FROM "+ AdminSQLiteOpenHelper.TABLE_CONTACTOS;
        Cursor consulta = BD.rawQuery(c, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(consulta.moveToFirst()) {
            do {
                String temp_id = consulta.getString(0);
                String temp_nombre = consulta.getString(1);
                String temp_telefono = consulta.getString(2);


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
                        Intent intent = new Intent(Activity_MenuFav.this, MainActivity.class);
                        intent.putExtra("nombre_fav", temp_nombre);
                        intent.putExtra("telefono_fav", temp_telefono);
                        startActivity(intent);
                        finish();
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
    public void btnVolver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void btnLimpiarListado(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de que desea eliminar todos los datos?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(Activity_MenuFav.this);
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(AdminSQLiteOpenHelper.TABLE_CONTACTOS, null, null);
                        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+ AdminSQLiteOpenHelper.TABLE_CONTACTOS+"'");

                        database.close();
                        lista.removeAllViews();
                        Toast.makeText(Activity_MenuFav.this,"DATOS ELIMINADOS", Toast.LENGTH_LONG).show();

                        inicicializar();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Cerrar el diálogo
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}