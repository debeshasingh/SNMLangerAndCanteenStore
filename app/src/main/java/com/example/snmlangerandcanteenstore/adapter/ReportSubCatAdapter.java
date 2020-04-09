
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.OutStock;
import com.example.snmlangerandcanteenstore.model.Product;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ReportSubCatAdapter extends RecyclerView.Adapter<ReportSubCatAdapter.CustomViewHolder> implements HelperInterface {

    private List<Product> products;
    private Activity activity;

    public ReportSubCatAdapter(Activity activity) {
        this.products = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Product> lists) {
        products.clear();
        products.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_cat_report_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Product product = products.get(i);

        if (product.getpName() != null) {
            customViewHolder.txtName.setText("Product Name : " + product.getpName());
        }

        if (i == products.size() - 1) {
            customViewHolder.view.setVisibility(View.GONE);
        } else {
            customViewHolder.view.setVisibility(View.VISIBLE);
        }
        if (product.getpId() != null) {
            updateView(product, customViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return (null != products ? products.size() : 0);
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtTotalInstock, txtTotalOutstock, txtQuantity;
        public LinearLayout cardView;
        public View view;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
            this.txtTotalInstock = view.findViewById(R.id.txt_total_instock);
            this.txtTotalOutstock = view.findViewById(R.id.txt_total_outstock);
            this.txtQuantity = view.findViewById(R.id.txt_balance);
            this.view = view.findViewById(R.id.view);
        }
    }

    private void updateView(Product product, CustomViewHolder holder) {
        List<InStock> inStocks = new ArrayList<>();
        List<OutStock> outStocks = new ArrayList<>();
        if (product != null && product.getpId() != null) {
            if (getHelper().getInStock(activity) != null) {
                for (InStock inStock : getHelper().getInStock(activity)) {
                    if (inStock != null && inStock.getProd() != null && inStock.getProd().getpId().equals(product.getpId())) {
                        inStocks.add(inStock);
                    }
                }
            }

            if (getHelper().getOutStock(activity) != null) {
                for (OutStock inStock : getHelper().getOutStock(activity)) {
                    if (inStock != null && inStock.getProd() != null && inStock.getProd().getpId().equals(product.getpId())) {
                        outStocks.add(inStock);
                    }
                }
            }
            Log.d("Debesh", "inStocks: " + new Gson().toJson(inStocks));
            Log.d("Debesh", "outStocks: " + new Gson().toJson(outStocks));

            if (inStocks.size() > 0) {
                holder.txtTotalInstock.setText("Purchesed Qty : " + fInStockQuantity(inStocks)+" "+product.getUnit());
            } else {
                holder.txtTotalInstock.setText("Purchesed Qty : " + fInStockQuantity(inStocks)+" "+product.getUnit());
            }

            if (outStocks.size() > 0) {
                holder.txtTotalOutstock.setText("Issued Qty : " + fOutStockQuantity(outStocks)+" "+product.getUnit());
            } else {
                holder.txtTotalOutstock.setText("Issued Qty : " + fOutStockQuantity(outStocks)+" "+product.getUnit());
            }

            holder.txtQuantity.setText("Balance : " + (fInStockQuantity(inStocks) - fOutStockQuantity(outStocks))+" "+product.getUnit());

        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            holder.txtTotalInstock.setText("Purchesed Qty : " + fInStockQuantity(inStocks)+" "+product.getUnit());
            holder.txtTotalOutstock.setText("Issued Qty : " + fOutStockQuantity(outStocks)+" "+product.getUnit());
            holder.txtQuantity.setText("Balance : " + (fInStockQuantity(inStocks) - fOutStockQuantity(outStocks)+" "+product.getUnit()));
        }
    }

    private float fInStockQuantity(List<InStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }

    private float fOutStockQuantity(List<OutStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }
}
