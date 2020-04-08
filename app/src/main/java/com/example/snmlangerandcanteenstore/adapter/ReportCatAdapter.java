
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.CatList;

import java.util.ArrayList;
import java.util.List;


public class ReportCatAdapter extends RecyclerView.Adapter<ReportCatAdapter.CustomViewHolder> {

    private List<CatList> catLists;
    private Activity activity;
    private ReportSubCatAdapter reportCatAdapter;

    public ReportCatAdapter(Activity activity) {
        this.catLists = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<CatList> lists) {
        catLists.clear();
        catLists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cat_report_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final CatList list = catLists.get(i);

        if (list.getCatName() != null) {
            customViewHolder.txtName.setText(list.getCatName());
        }

        customViewHolder.rcyProd.setLayoutManager(new LinearLayoutManager(activity));
        reportCatAdapter = new ReportSubCatAdapter(activity);
        customViewHolder.rcyProd.setAdapter(reportCatAdapter);
        reportCatAdapter.swap(list.getProducts());

        if (i == catLists.size() - 1) {
            customViewHolder.view.setVisibility(View.GONE);
        } else {
            customViewHolder.view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != catLists ? catLists.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public LinearLayout cardView;
        public RecyclerView rcyProd;
        public View view;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
            this.rcyProd = view.findViewById(R.id.rcy_prod);
            this.view = view.findViewById(R.id.view);
        }
    }
}
