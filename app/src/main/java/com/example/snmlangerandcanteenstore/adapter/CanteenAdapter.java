
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.Canteen;

import java.util.ArrayList;
import java.util.List;


public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CustomViewHolder> {

    private List<Canteen> canteens;
    private Activity activity;

    public CanteenAdapter(Activity activity) {
        this.canteens = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Canteen> canteenList) {
        canteens.clear();
        canteens.addAll(canteenList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_canteen_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Canteen canteen = canteens.get(i);

        if (canteen.getcName() != null) {
            customViewHolder.txtName.setText("Canteen Name : " + canteen.getcName());
        }
        if (canteen.getcMob() != null) {
            customViewHolder.txtMobile.setText("Mobile : " + canteen.getcMob());
        }
        if (canteen.getZone() != null) {
            customViewHolder.txtZone.setText("Zone : " + canteen.getZone());
        }


    }

    @Override
    public int getItemCount() {
        return (null != canteens ? canteens.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtMobile, txtZone;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
            this.txtMobile = view.findViewById(R.id.txt_mobile);
            this.txtZone = view.findViewById(R.id.txt_zone);
        }
    }
}
