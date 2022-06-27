package com.hellokh.sovary.firetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class FormActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Spinner cbxSeveridad = (Spinner) findViewById(R.id.cbxSeveridad);
        Spinner cbxEstado = (Spinner) findViewById(R.id.cbxEstado);
        ArrayAdapter<CharSequence> adapterSeveridad = ArrayAdapter.createFromResource(this,
                R.array.severidad_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(this,
                R.array.estado_array, android.R.layout.simple_spinner_item);
        cbxSeveridad.setAdapter(adapterSeveridad);
        cbxEstado.setAdapter(adapterEstado);

        final String[] severidad = new String[1];
        final String[] estado = new String[1];
        final EditText edit_canton = findViewById(R.id.txtCanton);
        final EditText edit_distrito = findViewById(R.id.txtDistrito);
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnLista = findViewById(R.id.btnLista);


        cbxSeveridad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               severidad[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cbxEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnLista.setOnClickListener(v->
        {
            Intent intent = new Intent(FormActivity.this, RVActivity.class);
            startActivity(intent);
            finish();
        });

        DAODerrumbeHueco dao = new DAODerrumbeHueco();
        DerrumbeHueco dh_edit = (DerrumbeHueco)getIntent().getSerializableExtra("EDITAR");
        if(dh_edit != null)
        {
            btnGuardar.setText("Editar");
            edit_canton.setText(dh_edit.getCanton());
            edit_distrito.setText(dh_edit.getDistrito());
            int severidadPosition = adapterSeveridad.getPosition(dh_edit.getSeveridad());
            cbxSeveridad.setSelection(severidadPosition);
            int estadoPosition = adapterEstado.getPosition(dh_edit.getEstado());
            cbxEstado.setSelection(estadoPosition);

            //btnLista.setVisibility(View.GONE);
        }
        else
        {
            btnGuardar.setText("Guardar");
            //btnLista.setVisibility(View.VISIBLE);
        }

        btnGuardar.setOnClickListener(v->
        {
            DerrumbeHueco dh = new DerrumbeHueco(edit_canton.getText().toString(), edit_distrito.getText().toString(), severidad[0], estado[0],fecha);
            if(dh_edit==null)
            {
                dao.add(dh).addOnSuccessListener(suc ->
                {
                    Intent intent = new Intent(FormActivity.this, RVActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            else
            {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("canton", edit_canton.getText().toString());
                hashMap.put("distrito", edit_distrito.getText().toString());
                hashMap.put("severidad", severidad[0]);
                hashMap.put("estado", estado[0]);
                hashMap.put("fecha", date);
                dao.update(dh_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                {
                    Intent intent = new Intent(FormActivity.this, RVActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Registro editado", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


    }
}
