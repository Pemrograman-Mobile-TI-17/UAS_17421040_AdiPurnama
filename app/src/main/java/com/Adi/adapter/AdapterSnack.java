package com.Adi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Adi.R;
import com.Adi.model.ModelSnack;
import com.Adi.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSnack extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelSnack> item;

    public AdapterSnack(Activity activity, List<ModelSnack> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_snack, null);


        TextView  NamaSnack = (TextView) convertView.findViewById(R.id.txtNamaSnack);
        TextView  HargaSnack     = (TextView) convertView.findViewById(R.id.txtHargaSnack);
        ImageView gambarSnack         = (ImageView) convertView.findViewById(R.id.gambarSnack);


        NamaSnack.setText(item.get(position).getNamaSnack());
        HargaSnack.setText(item.get(position).getHargaSnack());
        Picasso.get().load(BaseURL.baseUrl + "gambar/" + item.get(position).getGambar())
                .resize(40, 40)
                .centerCrop()
                .into(gambarSnack);
        return convertView;
    }

}
