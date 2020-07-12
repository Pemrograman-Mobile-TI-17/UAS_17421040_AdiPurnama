package com.Adi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Adi.R;
import com.Adi.server.BaseURL;
import com.Adi.server.VolleyMultipart;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputDataSnack extends AppCompatActivity {

    EditText edtKodeSnack, edtNamaSnack, edtHargaSnack;
    ImageView imgGambarSnack;

    Button btnTakeImg, simpanData;
    private RequestQueue mRequestQueue;

    Bitmap bitmap;

    private final int CameraR_PP = 1;
    String mCurrentPhotoPath;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_snack);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        edtKodeSnack = (EditText) findViewById(R.id.edtKodeSnack);
        edtNamaSnack = (EditText) findViewById(R.id.edtNamaSnack);
        imgGambarSnack = (ImageView) findViewById(R.id.gambar);

        btnTakeImg = (Button) findViewById(R.id.btnTakeImage);
        simpanData = (Button) findViewById(R.id.simpanData);

        btnTakeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });
        simpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputsnack(bitmap);
            }
        });

    }
    public void takeImage(){
        addPermission();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(InputDataSnack.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("Tags", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CameraR_PP);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EditSnackDanHapusActivity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CameraR_PP) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(InputDataSnack.this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imgGambarSnack.setImageBitmap(bitmap);
//                uploadPotoProfile(bitmap);
                if (imgGambarSnack.getDrawable() != null) {
                    int newHeight = 300; // New height in pixels
                    int newWidth = 300;
                    imgGambarSnack.requestLayout();
                    imgGambarSnack.getLayoutParams().height = newHeight;
                    // Apply the new width for ImageView programmatically
                    imgGambarSnack.getLayoutParams().width = newWidth;
                    // Set the scale type for ImageView image scaling
                    imgGambarSnack.setScaleType(ImageView.ScaleType.FIT_XY);
                    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imgGambarSnack.getLayoutParams();
                    marginParams.setMargins(0, 10, 0, 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(InputDataSnack.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addPermission() {
        Dexter.withActivity(InputDataSnack.this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(InputDataSnack.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void inputsnack(final Bitmap bitmap) {
        pDialog.setMessage("Mohon Tunggu .........");
        showDialog();
        VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.POST, BaseURL.inputSnack,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        mRequestQueue.getCache().clear();
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            System.out.println("ress = " + jsonObject.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(InputDataSnack.this, ActivityDataSnack.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(InputDataSnack.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kodeSnack", edtKodeSnack.getText().toString());
                params.put("judulSnack", edtNamaSnack.getText().toString());
                params.put("Harga", edtHargaSnack.getText().toString());
                params.put("hargaBuku", edtHargaSnack.getText().toString());
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new VolleyMultipart.DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue = Volley.newRequestQueue(InputDataSnack.this);
        mRequestQueue.add(volleyMultipartRequest);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(InputDataSnack.this, HomeAdminActivity.class);
        startActivity(i);
        finish();
    }

}
