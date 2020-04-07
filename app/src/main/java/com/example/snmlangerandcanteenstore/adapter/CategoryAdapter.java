
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.Category;
import com.example.snmlangerandcanteenstore.model.ProdUnit;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {

    private List<Category> categories;
    private Activity activity;

    public CategoryAdapter(Activity activity) {
        this.categories = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Category> categoryList) {
        categories.clear();
        categories.addAll(categoryList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_unit_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Category canteen = categories.get(i);

        if (canteen.getCatName() != null) {
            customViewHolder.txtName.setText("Category Type : " + canteen.getCatName());
        }
    }

    @Override
    public int getItemCount() {
        return (null != categories ? categories.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtName = view.findViewById(R.id.txt_name);
        }
    }
}
