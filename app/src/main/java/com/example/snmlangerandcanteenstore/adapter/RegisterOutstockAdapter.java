
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.OutStock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RegisterOutstockAdapter extends RecyclerView.Adapter<RegisterOutstockAdapter.CustomViewHolder> {

    private List<OutStock> outStocks;
    private Activity activity;

    public RegisterOutstockAdapter(Activity activity) {
        this.outStocks = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<OutStock> lists) {
        outStocks.clear();
        outStocks.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_outstock_register_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final OutStock list = outStocks.get(i);

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
        if (list.getIndNo() != null) {
            customViewHolder.txtIndentNumber.setText("Indent NO : " + list.getIndNo());
        }
        if (list.getcNo() != null) {
            customViewHolder.txtChallanNumber.setText("Challan NO : " + list.getIndNo());
        } else {
            customViewHolder.txtChallanNumber.setText("Challan NO : ");
        }
        if (list.getcName() != null) {
            customViewHolder.txtCanteenName.setText("Canteen Name : " + list.getcName());
        }
        if (list.getProd() != null && list.getProd().getpName() != null) {
            customViewHolder.txtProductName.setText("Product Name : " + list.getProd().getpName());
        }
        if (list.getpInQty() != null) {
            customViewHolder.txtQuantity.setText("Quantity : " + list.getpInQty()+" "+list.getProd().getUnit());
        }

    }

    @Override
    public int getItemCount() {
        return (null != outStocks ? outStocks.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDate, txtCreatedBy, txtIndentNumber, txtChallanNumber, txtCanteenName, txtProductName, txtQuantity;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtDate = view.findViewById(R.id.txt_date);
            this.txtCreatedBy = view.findViewById(R.id.txt_created_by);
            this.txtIndentNumber = view.findViewById(R.id.txt_indent_number);
            this.txtChallanNumber = view.findViewById(R.id.txt_challan_number);
            this.txtCanteenName = view.findViewById(R.id.txt_canteen_name);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
        }
    }
}
