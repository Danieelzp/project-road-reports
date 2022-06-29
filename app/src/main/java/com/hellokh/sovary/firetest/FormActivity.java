package com.hellokh.sovary.firetest;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FormActivity extends AppCompatActivity
{
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private ImageView mImageView;
    public Uri mImageUri;
    String rutaImagen;

    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    final String[] canton = new String[1];
    final String[] distrito = new String[1];
    final String[] severidad = new String[1];
    final String[] estado = new String[1];
    String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        DAODerrumbeHueco dao = new DAODerrumbeHueco();
        DerrumbeHueco dh_edit = (DerrumbeHueco)getIntent().getSerializableExtra("EDITAR");
        DerrumbeHueco dh_details = (DerrumbeHueco)getIntent().getSerializableExtra("DETALLES");

        mImageView = findViewById(R.id.imgDerrumbe);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DerrumbeHueco");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Todos los componentes del formulario
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

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnSubirImagen = findViewById(R.id.btnSubirImagen);
        Button btnUbicacion = findViewById(R.id.btnUbicacion);
        Button btnCapturarImagen = findViewById(R.id.btnCapturarImagen);


        //Método para llenar el Spinner de Distritos conforme a lo que seleccionemos en Canton
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

        btnCancelar.setOnClickListener(v->
        {
            Intent intent = new Intent(FormActivity.this, RVActivity.class);
            startActivity(intent);
            finish();
        });

        btnSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnCapturarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

       //EDITAR
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
            btnUbicacion.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
            btnSubirImagen.setVisibility(View.GONE);
            btnCapturarImagen.setVisibility(View.GONE);
        }
        //DETALLES
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
            btnSubirImagen.setVisibility(View.GONE);
            btnCapturarImagen.setVisibility(View.GONE);
            btnCancelar.setText("Volver");
        }
        //CREAR
        else
        {
            txtCanton.setVisibility(View.GONE);
            txtDistrito.setVisibility(View.GONE);
            txtSeveridad.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
            txtFecha.setVisibility(View.GONE);
            btnUbicacion.setVisibility(View.GONE);
            btnGuardar.setText("Guardar");
        }

        btnGuardar.setOnClickListener(v->
        {
            if(dh_edit==null)
            {
                uploadFile();
            }
            else
            {
                //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("canton", canton[0]);
                hashMap.put("distrito", distrito[0]);
                hashMap.put("severidad", severidad[0]);
                hashMap.put("estado", estado[0]);
                //hashMap.put("fecha", date);
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

        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void takePicture() {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager()) !=null) {

            File imagenArchivo = null;

            try {
                imagenArchivo = crearImagen();
            } catch (IOException ex){
                Log.e("Error", ex.toString());
            }

            if(imagenArchivo != null){
                Uri fotoUri = FileProvider.getUriForFile(this, "com.hellokh.sovary.firetest", imagenArchivo);
                imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);

        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(rutaImagen);
            //mImageView.setImageBitmap(imageBitmap);
            Toast.makeText(this,"Imagen guardada en el dispositivo",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Cargando imagen...");
            pd.show();

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                            DerrumbeHueco dh = new DerrumbeHueco(canton[0], distrito[0], severidad[0], estado[0],fecha, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String dhId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(dhId).setValue(dh);
                            Intent intent = new Intent(FormActivity.this, RVActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Porcentaje: " + (int) progressPercent + "%");
                        }
                    });
        }else{
            Toast.makeText(this,"Debe subir una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;

    }

}
