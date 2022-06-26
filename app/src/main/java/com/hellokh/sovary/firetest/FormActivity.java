package com.hellokh.sovary.firetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class FormActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        final EditText edit_canton = findViewById(R.id.txtCanton);
        final EditText edit_distrito = findViewById(R.id.txtDistrito);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnLista = findViewById(R.id.btnLista);


        btnLista.setOnClickListener(v->
        {
            Intent intent = new Intent(FormActivity.this, RVActivity.class);
            startActivity(intent);
        });

        DAODerrumbeHueco dao = new DAODerrumbeHueco();
        DerrumbeHueco dh_edit = (DerrumbeHueco)getIntent().getSerializableExtra("EDITAR");
        if(dh_edit != null)
        {
            btnGuardar.setText("Editar");
            edit_canton.setText(dh_edit.getCanton());
            edit_distrito.setText(dh_edit.getDistrito());
            btnLista.setVisibility(View.GONE);
        }
        else
        {
            btnGuardar.setText("Guardar");
            btnLista.setVisibility(View.VISIBLE);
        }

        btnGuardar.setOnClickListener(v->
        {
            DerrumbeHueco dh = new DerrumbeHueco(edit_canton.getText().toString(), edit_distrito.getText().toString());
            if(dh_edit==null)
            {
                dao.add(dh).addOnSuccessListener(suc ->
                {
                    edit_canton.setText("");
                    edit_distrito.setText("");
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            else
            {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("canton", edit_canton.getText().toString());
                hashMap.put("distrito", edit_distrito.getText().toString());
                dao.update(dh_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Registro editado", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


    }
}
