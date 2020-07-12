package com.Adi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.Adi.R;
import com.Adi.adapter.AdapterSnack;
import com.Adi.model.ModelSnack;
import com.Adi.server.BaseURL;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityDataSnack extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterSnack adapter;
    ListView list;

    ArrayList<ModelSnack> newsList = new ArrayList<ModelSnack>();
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_snack);

        getSupportActionBar().setTitle("Data Buku");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();
        adapter = new AdapterSnack(ActivityDataSnack.this, newsList);
        list.setAdapter(adapter);
        getAllSnack();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityDataSnack.this, HomeAdminActivity.class);
        startActivity(i);
        finish();
    }

    private void getAllSnack() {
        // Pass second argument as "null" for GET requests
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, BaseURL.dataSnack, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                Log.d("data buku = ", response.toString());
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelSnack snack = new ModelSnack();
                                    final String _id = jsonObject.getString("_id");
                                    final String namaSnack = jsonObject.getString("namaSnack");
                                    final String kodeSnack = jsonObject.getString("kodeSnack");
                                    final String hargaSnack = jsonObject.getString("hargaSnack");
                                    final String gamabr = jsonObject.getString("gambar");
                                    snack.setKodeSnack(kodeSnack);
                                    snack.setNamaSnack(namaSnack);
                                    snack.setHargaSnack(hargaSnack);
                                    snack.setGambar(gamabr);
                                    snack.set_id(_id);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(ActivityDataSnack.this, EditSnackDanHapusActivity.class);
                                            a.putExtra("kodeSnack", newsList.get(position).getKodeSnack());
                                            a.putExtra("_id", newsList.get(position).get_id());
                                            a.putExtra("namaSnack", newsList.get(position).getNamaSnack());
                                            a.putExtra("hargaSnack", newsList.get(position).getHargaSnack());
                                            a.putExtra("gambar", newsList.get(position).getGambar());
                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(snack);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
