package com.dtarragol.mensajeator;

import static com.dtarragol.mensajeator.AdminSQLiteOpenHelper.TABLE_MENSAJE_COLOQUIAL;

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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class Activity_CrearMensaje extends AppCompatActivity {

    LinearLayout lista;
    CheckBox BT;
    List<Integer> IDS = new ArrayList<>();
    List<Integer> IDS_linea = new ArrayList<>();
    int numeroBoton=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_crear_mensaje);
        lista = findViewById(R.id.glay);
        initializeUI();
    }
    public void initializeUI() {
        lista.removeAllViews();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase BD = admin.getReadableDatabase();
        String c = "SELECT * FROM "+ TABLE_MENSAJE_COLOQUIAL;
        Cursor consulta = BD.rawQuery(c, null);
        if(consulta.moveToFirst()) {
            do {

                String temp_id = consulta.getString(0);
                String temp_nombre = consulta.getString(1);

                String A = temp_id + ": " + temp_nombre;

                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv = new TextView(this);
                tv.setText(A);
                tv.setTextColor(Color.BLACK);

                rowLayout.setId(View.generateViewId());
                int RLID=rowLayout.getId();
                IDS_linea.add(RLID);

                BT = new CheckBox(this);
                //BT.setBackgroundColor(Color.RED);
                BT.setTextColor(Color.RED);
                BT.setText(" Eliminar ");

                BT.setId(View.generateViewId());
                int btnID= BT.getId();
                IDS.add(btnID);

                rowLayout.addView(tv);
                rowLayout.addView(BT);
                lista.addView(rowLayout);
                numeroBoton++;
            }while (consulta.moveToNext());
        }
        BD.close();
    }
    public void btnMenuNuevoMensaje(View v){
        Intent intent = new Intent(this, Activity_NuevoMensaje.class);
        startActivity(intent);
        finish();
    }
    public void borrarSeleccionados(View v){
        if(numeroBoton<=2){
            borrarTodo(v);
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            for (int i = 1; i<numeroBoton; i++){
                CheckBox RB= findViewById(IDS.get(i-1));
                if (RB.isChecked()){
                    // Obtén el ID de la fila que deseas eliminar
                    int idToDelete = i;
                    Toast.makeText(this, "Paraborrar= "+i, Toast.LENGTH_SHORT).show();
                    // Elimina la fila utilizando una sentencia SQL
                    String whereClause = "id = ?";
                    String[] whereArgs = {String.valueOf(idToDelete)};
                    db.delete(TABLE_MENSAJE_COLOQUIAL, whereClause, whereArgs);
                    // Actualiza los IDs restantes
                    String updateQuery = "UPDATE "+TABLE_MENSAJE_COLOQUIAL+" SET id = id - 1 WHERE id > ?";
                    String[] updateArgs = {String.valueOf(idToDelete)};
                    db.execSQL(updateQuery, updateArgs);

                    //para reinidiar el último ID
                    db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+TABLE_MENSAJE_COLOQUIAL+"'");

                    // Cierra la base de datos
                    db.close();
                }
            }
        }
        initializeUI();
    }
    public void borrarTodo(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de que desea eliminar todos los datos?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(Activity_CrearMensaje.this);
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(TABLE_MENSAJE_COLOQUIAL, null, null);
                        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+TABLE_MENSAJE_COLOQUIAL+"'");

                        database.close();
                        lista.removeAllViews();
                        Toast.makeText(Activity_CrearMensaje.this,"DATOS ELIMINADOS", Toast.LENGTH_LONG).show();
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
    public void btnVolver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}