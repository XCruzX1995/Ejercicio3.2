package com.aplicacion.ejercicio3_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnGuardar, btnLista;
    EditText txtNombre, txtApellido, txtEdad, txtDireccion, txtPuesto;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtEdad = (EditText) findViewById(R.id.txtEdad);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtPuesto = (EditText) findViewById(R.id.txtPuesto);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnLista = (Button) findViewById(R.id.btnLista);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Guarda los datos en la BD
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtNombre.getText().toString().isEmpty() || txtApellido.getText().toString().isEmpty() ||
                        txtEdad.getText().toString().isEmpty() || txtDireccion.getText().toString().isEmpty() ||
                        txtPuesto.getText().toString().isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Complete todos los campos!!!")
                            .setTitle("Atención");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    String id = mDatabase.push().getKey();
                    Map<String, Object> empleadosInfo = new HashMap<>();
                    empleadosInfo.put("id", id);
                    empleadosInfo.put("nombre", txtNombre.getText().toString().trim());
                    empleadosInfo.put("apellido", txtApellido.getText().toString().trim());
                    empleadosInfo.put("edad", txtEdad.getText().toString().trim());
                    empleadosInfo.put("direccion", txtDireccion.getText().toString().trim());
                    empleadosInfo.put("puesto", txtPuesto.getText().toString().trim());
                    mDatabase.child("empleados").child(id).setValue(empleadosInfo);

                    Toast.makeText(MainActivity.this, "Datos Guardados con Exito", Toast.LENGTH_LONG).show();

                    limpiar();
                }
            }
        });

        //Ver la lista de empleados.
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list = new Intent(MainActivity.this, ActivityListaEmple.class);
                startActivity(list);
            }
        });
    }

    public void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtPuesto.setText("");
        txtNombre.requestFocus();
    }
}