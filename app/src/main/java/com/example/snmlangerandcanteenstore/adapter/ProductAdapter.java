
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewHolder> {

    private List<Product> products;
    private Activity activity;

    public ProductAdapter(Activity activity) {
        this.products = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Product> productList) {
        products.clear();
        products.addAll(productList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_product_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Product product = products.get(i);

        if (product.getpName() != null) {
            customViewHolder.txtName.setText("Product Name : " + product.getpName());
        }
        if (product.getUnit() != null) {
            customViewHolder.txtUnit.setText("Unit : " + product.getUnit());
        }
        if (product.getCat() != null && product.getCat().getCatName()!=null) {
            customViewHolder.txtCategory.setText("Category: " + product.getCat().getCatName());
        }


    }

    @Override
    public int getItemCount() {
        return (null != products ? products.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtUnit,txtCategory;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
            this.txtUnit = view.findViewById(R.id.txt_unit);
            this.txtCategory = view.findViewById(R.id.txt_category);
        }
    }
}
