package com.Adi.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.Adi.R;
import com.Adi.server.BaseURL;
import com.squareup.picasso.Picasso;

public class DetailPembeli extends AppCompatActivity {

    EditText edtKodeSnack, edtNamaSnack, edtHargaSnack;
    ImageView imgGambarSnack;

    String strKodeSnack, strNamaSnack, strHargaSnack, strGamabr, _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembeli);

        edtKodeSnack = (EditText) findViewById(R.id.edtKodeSnack);
        edtNamaSnack = (EditText) findViewById(R.id.edtNamaSnack);
        edtHargaSnack = (EditText) findViewById(R.id.edtHargaSnack);

        imgGambarSnack = (ImageView) findViewById(R.id.gambar);

        Intent i = getIntent();
        strKodeSnack = i.getStringExtra("kodeSnack");
        strNamaSnack = i.getStringExtra("namaSnack");
        strHargaSnack = i.getStringExtra("hargaSnack");
        strGamabr = i.getStringExtra("gambar");
        _id = i.getStringExtra("_id");

        edtKodeSnack.setText(strKodeSnack);
        edtNamaSnack.setText(strNamaSnack);
        edtHargaSnack.setText(strHargaSnack);
        Picasso.get().load(BaseURL.baseUrl + "gambar/" + strGamabr)
                .into(imgGambarSnack);
    }
}
