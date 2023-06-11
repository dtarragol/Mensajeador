package com.example.mensajeator;

import static com.example.mensajeator.AdminSQLiteOpenHelper.TABLE_CONTACTOS;
import static com.example.mensajeator.AdminSQLiteOpenHelper.TABLE_MENSAJE_COLOQUIAL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText et, et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et=(EditText) findViewById(R.id.et_nombre);
        et2=(EditText) findViewById(R.id.et_numero);
        btn=(Button) findViewById(R.id.button);

        Intent intent = getIntent();
        if (intent.hasExtra("nombre_fav") && intent.hasExtra("telefono_fav")) {
            String nombre_contacto = intent.getStringExtra("nombre_fav");
            String telefono_contacto = intent.getStringExtra("telefono_fav");
            et.setText(nombre_contacto);
            et2.setText(telefono_contacto);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Cursor c=getContentResolver().query(uri, null, null,null,null);
            if(c != null && c.moveToFirst()){
                int indiceName=c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indiceNumber=c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String nombre = c.getString(indiceName);
                String number = c.getString(indiceNumber);

                number = number.replace("(", "").replace(")","").replace("-","");
                et.setText(nombre);
                et2.setText(number);
            }
        }
    }
    public void btn_ir_mensajes(View v){
        String nombre = et.getText().toString();
        String numero = et2.getText().toString();
        if(!nombre.equals("")&&!numero.equals("")){
            Intent intent = new Intent(this, Activity_EnviarMensaje.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("numero", numero);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_LONG).show();
            if(nombre.equals("")){
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
            if(numero.equals("")){
                et2.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et2, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    }
    public void btnMenuMensajes(View v){
        Intent intent = new Intent(this, Activity_CrearMensaje.class);
        startActivity(intent);
        finish();
    }
    public void btnMenuFav(View v){
        Intent intent = new Intent(this, Activity_MenuFav.class);
        startActivity(intent);
        finish();
    }
    public void btnUsarMensajes(View v){
        String nombre = et.getText().toString();
        String numero = et2.getText().toString();
        if(!nombre.equals("")&&!numero.equals("")){
            Intent intent = new Intent(this, Activity_UsarMensaje.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("numero", numero);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_LONG).show();
            if(nombre.equals("")){
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
            if(numero.equals("")){
                et2.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et2, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
    public void AgregarFav(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase BD = admin.getWritableDatabase();

        ContentValues modificacion = new ContentValues();
        modificacion.put("nombre", et.getText().toString());
        modificacion.put("telefono", et2.getText().toString());

        long newRowId = BD.insert(TABLE_CONTACTOS,null, modificacion);
        if(newRowId==-1){
            Toast.makeText(this, "No se ha guardado el contacto", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "¡CONTACTO Guardado!", Toast.LENGTH_LONG).show();
        }
        BD.close();
    }
}