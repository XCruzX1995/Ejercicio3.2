package com.aplicacion.ejercicio3_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.ejercicio3_2.Clases.Empleados;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ActivityListaEmple extends AppCompatActivity {

    FirebaseListAdapter adapter;
    ListView listaP;
    CharSequence options[];
    String seleccion;
    Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_emple);

        listaP = (ListView) findViewById(R.id.listaP);
        btnAtras = (Button) findViewById(R.id.btnVolver);

        Query query = FirebaseDatabase.getInstance().getReference().child("empleados");
        FirebaseListOptions<Empleados> options = new FirebaseListOptions.Builder<Empleados>()
                .setLayout(R.layout.item)
                .setQuery(query, Empleados.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView tvId = v.findViewById(R.id.tvId);
                TextView tvNombre = v.findViewById(R.id.tvNombre);
                TextView tvApellido = v.findViewById(R.id.tvApellido);
                TextView tvEdad = v.findViewById(R.id.tvEdad);
                TextView tvDireccion = v.findViewById(R.id.tvDireccion);
                TextView tvPuesto = v.findViewById(R.id.tvPuesto);
                //ImageView imagenFoto = v.findViewById(R.id.imagenFoto);


                Empleados std = (Empleados) model;
                tvId.setText(std.getId().toString());
                tvNombre.setText(std.getNombre().toString());
                tvApellido.setText(std.getApellido().toString());
                tvEdad.setText(std.getEdad().toString());
                tvDireccion.setText(std.getDireccion().toString());
                tvPuesto.setText(std.getPuesto().toString());
                //seleccion = tvId.getText().toString();
                //Picasso.with(ActivityListadoR.this).load(std.getImagenUrl()).into(imagenFoto);
            }
        };
        listaP.setAdapter(adapter);

        //Selecciona el pedido Activo
        listaP.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Empleados c = (Empleados) listaP.getItemAtPosition(i);
                //Toast.makeText(getBaseContext(), c.getNombre(), Toast.LENGTH_LONG).show();

                Empleados c = (Empleados) listaP.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), ActivityDetalleEmple.class);
                intent.putExtra("identificador", c.getId());
                startActivity(intent);
            }
        });

        // volver a pagina principal
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}