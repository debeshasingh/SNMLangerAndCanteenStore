
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.Vendor;

import java.util.ArrayList;
import java.util.List;


public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.CustomViewHolder> {

    private List<Vendor> vendors;
    private Activity activity;

    public VendorAdapter(Activity activity) {
        this.vendors = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Vendor> vendorList) {
        vendors.clear();
        vendors.addAll(vendorList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_vendor_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Vendor vendor = vendors.get(i);
        if(vendor.getvName()!=null){
            customViewHolder.txtName.setText("Vendor Name : "+vendor.getvName());
        }
        if(vendor.getvMob() !=null){
            customViewHolder.txtMobile.setText("Mobile : "+vendor.getvMob());
        }
        if(vendor.getDetail() !=null){
            customViewHolder.txtDetail.setText("Detail : "+vendor.getDetail());
        }

    }

    @Override
    public int getItemCount() {
        return (null != vendors ? vendors.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName,txtMobile, txtDetail;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
            this.txtMobile = view.findViewById(R.id.txt_mobile);
            this.txtDetail = view.findViewById(R.id.txt_detail);
        }
    }
}
