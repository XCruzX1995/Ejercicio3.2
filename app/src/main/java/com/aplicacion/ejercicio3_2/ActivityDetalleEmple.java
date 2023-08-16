package com.aplicacion.ejercicio3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ActivityDetalleEmple extends AppCompatActivity {

    String identificador = "";
    DatabaseReference mDatabase;

    EditText txtEmpledoNombre, txtEmpledoApellido, txtEmpledoEdad, txtEmpledoDireccion, txtEmpledoPuesto;
    Button btnActualizar, btnEliminar, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_emple);

        Intent intent = getIntent();
        identificador = (intent.getStringExtra("identificador"));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtEmpledoNombre = (EditText) findViewById(R.id.txtEmpledoNombre);
        txtEmpledoApellido = (EditText) findViewById(R.id.txtEmpledoApellido);
        txtEmpledoEdad = (EditText) findViewById(R.id.txtEmpledoEdad);
        txtEmpledoDireccion = (EditText) findViewById(R.id.txtEmpledoDireccion);
        txtEmpledoPuesto = (EditText) findViewById(R.id.txtEmpledoPuesto);

        btnActualizar = (findViewById(R.id.btnActualizar));
        btnEliminar = (findViewById(R.id.btnEliminar));
        btnAtras = (Button) findViewById(R.id.btnVolver2);

        infoEmpleado();

        //Actualizar datos del Empleado
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatos();
            }
        });

        //Elimina los datos del Empleado
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetalleEmple.this);
                builder.setMessage("¿Desea Eliminar este Registro?")
                        .setTitle(txtEmpledoNombre.getText().toString());

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("empleados").child(identificador).removeValue();
                        Toast.makeText(ActivityDetalleEmple.this, "Registro Eliminado", Toast.LENGTH_LONG).show();
                        onBackPressed();
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // volver a pagina principal
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListaEmple.class);
                startActivity(intent);
            }
        });

    }

    public void infoEmpleado(){
        mDatabase.child("empleados").child(identificador).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    txtEmpledoNombre.setText(dataSnapshot.child("nombre").getValue().toString());
                    txtEmpledoApellido.setText(dataSnapshot.child("apellido").getValue().toString());
                    txtEmpledoEdad.setText(dataSnapshot.child("edad").getValue().toString());
                    txtEmpledoDireccion.setText(dataSnapshot.child("direccion").getValue().toString());
                    txtEmpledoPuesto.setText(dataSnapshot.child("puesto").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void actualizarDatos(){
        Map<String, Object> empleadosInfo = new HashMap<>();
        empleadosInfo.put("nombre", txtEmpledoNombre.getText().toString().trim());
        empleadosInfo.put("apellido", txtEmpledoApellido.getText().toString().trim());
        empleadosInfo.put("edad", txtEmpledoEdad.getText().toString().trim());
        empleadosInfo.put("direccion", txtEmpledoDireccion.getText().toString().trim());
        empleadosInfo.put("puesto", txtEmpledoPuesto.getText().toString().trim());
        mDatabase.child("empleados").child(identificador).updateChildren(empleadosInfo);

        Toast.makeText(ActivityDetalleEmple.this, "Datos Actualizados", Toast.LENGTH_LONG).show();
    }
}