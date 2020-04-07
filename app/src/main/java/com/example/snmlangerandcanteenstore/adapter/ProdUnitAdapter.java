
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
import com.example.snmlangerandcanteenstore.model.ProdUnit;

import java.util.ArrayList;
import java.util.List;


public class ProdUnitAdapter extends RecyclerView.Adapter<ProdUnitAdapter.CustomViewHolder> {

    private List<ProdUnit> prodUnits;
    private Activity activity;

    public ProdUnitAdapter(Activity activity) {
        this.prodUnits = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<ProdUnit> units) {
        prodUnits.clear();
        prodUnits.addAll(units);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_unit_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final ProdUnit canteen = prodUnits.get(i);

        if (canteen.getuName() != null) {
            customViewHolder.txtName.setText("Unit Type : " + canteen.getuName());
        }
    }

    @Override
    public int getItemCount() {
        return (null != prodUnits ? prodUnits.size() : 0);
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
