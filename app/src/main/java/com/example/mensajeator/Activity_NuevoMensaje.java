package com.example.mensajeator;

import static com.example.mensajeator.AdminSQLiteOpenHelper.TABLE_MENSAJE_COLOQUIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_NuevoMensaje extends AppCompatActivity {

    EditText nombre, mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_nuevo_mensaje);
        nombre = (EditText) findViewById(R.id.txt_nombre_mensaje);
        mensaje = (EditText) findViewById(R.id.txt_nuevo_mensaje);

    }
    public void GuardarMensajeBD(View v){
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(Activity_NuevoMensaje.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues modificacion = new ContentValues();
        modificacion.put("nombre", nombre.getText().toString());
        modificacion.put("mensaje", mensaje.getText().toString());
        long newRowId = db.insert(TABLE_MENSAJE_COLOQUIAL,null, modificacion);
        if(newRowId==-1){
            Toast.makeText(this, "No se ha guardado el mensaje", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "¡MENSAJE Guardado!", Toast.LENGTH_LONG).show();
        }

        db.close();
        Intent intent = new Intent(this, Activity_CrearMensaje.class);
        startActivity(intent);
        finish();
    }
    public void btnVolver(View view){
        Intent intent = new Intent(this, Activity_CrearMensaje.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Activity_CrearMensaje.class);
        startActivity(intent);
        finish();
    }
}