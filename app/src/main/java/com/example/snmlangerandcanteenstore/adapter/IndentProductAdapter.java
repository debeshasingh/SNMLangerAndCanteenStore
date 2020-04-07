
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.BuildConfig;
import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.fragment.indent.AddIndentFragment;
import com.example.snmlangerandcanteenstore.fragment.mrn.AddMRNFragment;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.OutStock;

import java.util.ArrayList;
import java.util.List;


public class IndentProductAdapter extends RecyclerView.Adapter<IndentProductAdapter.CustomViewHolder> {

    public List<OutStock> outStocks;
    private Activity activity;

    public IndentProductAdapter(Activity activity) {
        this.outStocks = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<OutStock> outStockList) {
        outStocks.clear();
        outStocks.addAll(outStockList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_indent_product_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final OutStock outStock = outStocks.get(i);

        if (outStock != null) {
            if (outStock.getProd() != null && outStock.getProd().getpName()!= null) {
                customViewHolder.txtProductName.setText("Product name : " + outStock.getProd().getpName() + " (" + outStock.getProd().getUnit() + ")");
            }
            if (outStock.getProUnit() != null) {
                customViewHolder.txtNumOfUnit.setText("No of Unit : " + outStock.getProUnit());
            }
            if (outStock.getPerUnit() != null) {
                customViewHolder.txtPerInUnit.setText("Per in Unit : " + outStock.getPerUnit());
            }
            if (outStock.getpInQty() != null) {
                customViewHolder.txtQuantity.setText("Quantity: " + outStock.getpInQty() + " (" + outStock.getProd().getUnit() + ")");
            }

            customViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialog(i);
                    return true;
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return (null != outStocks ? outStocks.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName, txtNumOfUnit, txtPerInUnit, txtQuantity;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtNumOfUnit = view.findViewById(R.id.txt_num_of_unit);
            this.txtPerInUnit = view.findViewById(R.id.txt_per_in_unit);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
        }
    }

    private void dialog(final int pos) {
        final AppCompatDialog dialog = new AppCompatDialog(activity, R.style.AppDialogThemeAccentNoTitle);
        dialog.setContentView(R.layout.dialog_cancel);
        dialog.setCancelable(false);
        final Button btnYes = dialog.findViewById(R.id.btn_yes);
        final Button btnNo = dialog.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outStocks.remove(pos);
                notifyDataSetChanged();
                ((AddIndentFragment) ((IndentActivity) activity).getSupportFragmentManager().findFragmentByTag(AppConstants.FRAGMENT_ADD_INDENT)).update();
                dialog.cancel();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
