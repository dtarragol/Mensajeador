package com.example.mensajeator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_EnviarMensaje extends AppCompatActivity {

    EditText et_mensaje;
    Button btnEnviarWP;
    String nombre, numero, prefijo="";

    public interface OnStringEnteredListener {
        void onStringEntered(String input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        et_mensaje = (EditText) findViewById(R.id.et_mensaje);

        nombre = getIntent().getStringExtra("nombre");
        numero = getIntent().getStringExtra("numero");

        DialogUtils.showInputDialog(this,"Introduce el prefijo.\n*Si el telèfono esta guardado con prefijo, no introduzca nada.", new DialogUtils.OnStringEnteredListener(){
            @Override
            public void onStringEntered(String input) {
                prefijo=input;
            }
        });

        btnEnviarWP= (Button)findViewById(R.id.btn_enviar_wp);
        btnEnviarWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_mensaje.getText().toString().isEmpty()){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, et_mensaje.getText().toString());
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } else{
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    String uri = "https://api.whatsapp.com/send?phone="+prefijo+numero+"&text="+et_mensaje.getText().toString();
                    sendIntent.setData(Uri.parse(uri));
                    startActivity(sendIntent);
                }
            }
        });
    }
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