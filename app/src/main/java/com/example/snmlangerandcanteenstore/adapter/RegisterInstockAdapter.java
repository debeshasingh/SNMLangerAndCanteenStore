
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.InStock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RegisterInstockAdapter extends RecyclerView.Adapter<RegisterInstockAdapter.CustomViewHolder> {

    private List<InStock> inStocks;
    private Activity activity;

    public RegisterInstockAdapter(Activity activity) {
        this.inStocks = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<InStock> lists) {
        inStocks.clear();
        inStocks.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_instock_register_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final InStock list = inStocks.get(i);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (list.getcDate() != null) {
            try {
                Date date = formatter.parse(list.getcDate());
                customViewHolder.txtDate.setText("Date : " + formatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (list.getcBy() != null) {
            customViewHolder.txtCreatedBy.setText("Created By : " + list.getcBy());
        }
        if (list.getMrnNo() != null) {
            customViewHolder.txtMrnNumber.setText("MRN NO : " + list.getMrnNo());
        }
        if (list.getVenName() != null) {
            customViewHolder.txtVendorName.setText("Vendor Name : " + list.getVenName());
        }
        if (list.getProd() != null && list.getProd().getpName() != null) {
            customViewHolder.txtProductName.setText("Product Name : " + list.getProd().getpName());
        }
        if (list.getpInQty() != null) {
            customViewHolder.txtQuantity.setText("Quantity : " + list.getpInQty() + " " + list.getProd().getUnit());
        }

    }

    @Override
    public int getItemCount() {
        return (null != inStocks ? inStocks.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDate, txtCreatedBy, txtMrnNumber, txtVendorName, txtProductName, txtQuantity;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtDate = view.findViewById(R.id.txt_date);
            this.txtCreatedBy = view.findViewById(R.id.txt_created_by);
            this.txtMrnNumber = view.findViewById(R.id.txt_mrn_number);
            this.txtVendorName = view.findViewById(R.id.txt_vendor_name);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
        }
    }
}
