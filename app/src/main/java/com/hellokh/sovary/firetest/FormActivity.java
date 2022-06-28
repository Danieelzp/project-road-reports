package com.hellokh.sovary.firetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FormActivity extends AppCompatActivity
{
    private ImageView imgDerrumbe;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        imgDerrumbe = findViewById(R.id.imgDerrumbe);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        TextView txtCanton = findViewById(R.id.txtCanton);
        TextView txtDistrito = findViewById(R.id.txtDistrito);
        TextView txtSeveridad = findViewById(R.id.txtSeveridad);
        TextView txtEstado = findViewById(R.id.txtEstado);
        TextView txtFecha = findViewById(R.id.txtFecha);

        Spinner cbxCanton = (Spinner) findViewById(R.id.cbxCanton);
        Spinner cbxDistrito= (Spinner) findViewById(R.id.cbxDistrito);
        Spinner cbxSeveridad = (Spinner) findViewById(R.id.cbxSeveridad);
        Spinner cbxEstado = (Spinner) findViewById(R.id.cbxEstado);

        ArrayAdapter<CharSequence> adapterCantones = ArrayAdapter.createFromResource(this, R.array.cantones_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistritosNicoya = ArrayAdapter.createFromResource(this, R.array.distritos_nicoya_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistritosLiberia = ArrayAdapter.createFromResource(this, R.array.distritos_liberia_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistritosBagaces = ArrayAdapter.createFromResource(this, R.array.distritos_bagaces_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSeveridad = ArrayAdapter.createFromResource(this, R.array.severidad_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(this, R.array.estado_array, android.R.layout.simple_spinner_item);

        cbxCanton.setAdapter(adapterCantones);
        cbxSeveridad.setAdapter(adapterSeveridad);
        cbxEstado.setAdapter(adapterEstado);

        final String[] canton = new String[1];
        final String[] distrito = new String[1];
        final String[] severidad = new String[1];
        final String[] estado = new String[1];
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnLista = findViewById(R.id.btnLista);

        imgDerrumbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        cbxCanton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                canton[0] = adapterView.getItemAtPosition(i).toString();

                if(canton[0].equals("Nicoya")){
                    cbxDistrito.setAdapter(adapterDistritosNicoya);

                }   else if(canton[0].equals("Liberia")){
                    cbxDistrito.setAdapter(adapterDistritosLiberia);

                }   else if(canton[0].equals("Bagaces")){
                    cbxDistrito.setAdapter(adapterDistritosBagaces);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cbxDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distrito[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        DerrumbeHueco dh_details = (DerrumbeHueco)getIntent().getSerializableExtra("DETALLES");
        if(dh_edit != null && dh_details == null)
        {
            btnGuardar.setText("Editar");
            int cantonPosition = adapterCantones.getPosition(dh_edit.getCanton());
            cbxCanton.setSelection(cantonPosition);

            //Este código es para seleccionar el distrito al entrar en editar, pero no sé por qué no funciona
            /*if(dh_edit.getCanton()=="Nicoya"){
                cbxDistrito.setAdapter(adapterDistritosNicoya);
                int distritoPosition = adapterDistritosNicoya.getPosition(dh_edit.getDistrito());
                cbxDistrito.setSelection(distritoPosition);

            }else if(dh_edit.getCanton()=="Liberia"){
                cbxDistrito.setAdapter(adapterDistritosLiberia);
                int distritoPosition = adapterDistritosLiberia.getPosition(dh_edit.getDistrito());
                cbxDistrito.setSelection(distritoPosition);

            }else if(dh_edit.getCanton()=="Bagaces"){
                cbxDistrito.setAdapter(adapterDistritosBagaces);
                int distritoPosition = adapterDistritosBagaces.getPosition(dh_edit.getDistrito());
                cbxDistrito.setSelection(distritoPosition);
            }*/

            int severidadPosition = adapterSeveridad.getPosition(dh_edit.getSeveridad());
            cbxSeveridad.setSelection(severidadPosition);
            int estadoPosition = adapterEstado.getPosition(dh_edit.getEstado());
            cbxEstado.setSelection(estadoPosition);

            txtCanton.setVisibility(View.GONE);
            txtDistrito.setVisibility(View.GONE);
            txtSeveridad.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
            txtFecha.setVisibility(View.GONE);
        }
        else if(dh_edit == null && dh_details != null){

            txtCanton.setText(dh_details.getCanton());
            txtDistrito.setText(dh_details.getDistrito());
            txtSeveridad.setText(dh_details.getSeveridad());
            txtEstado.setText(dh_details.getEstado());
            txtFecha.setText(dh_details.getFecha());

            cbxCanton.setVisibility(View.GONE);
            cbxDistrito.setVisibility(View.GONE);
            cbxSeveridad.setVisibility(View.GONE);
            cbxEstado.setVisibility(View.GONE);
            btnGuardar.setVisibility(View.GONE);
            btnLista.setText("Volver");
        }
        else
        {
            txtCanton.setVisibility(View.GONE);
            txtDistrito.setVisibility(View.GONE);
            txtSeveridad.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
            txtFecha.setVisibility(View.GONE);
            btnGuardar.setText("Guardar");
        }

        btnGuardar.setOnClickListener(v->
        {
            DerrumbeHueco dh = new DerrumbeHueco(canton[0], distrito[0], severidad[0], estado[0],fecha);
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
                hashMap.put("canton", canton[0]);
                hashMap.put("distrito", distrito[0]);
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

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgDerrumbe.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference stRef = storageReference.child("images/" + randomKey);

        stRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }
}
