
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
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.fragment.mrn.AddMRNFragment;
import com.example.snmlangerandcanteenstore.model.InStock;

import java.util.ArrayList;
import java.util.List;


public class MrnProductAdapter extends RecyclerView.Adapter<MrnProductAdapter.CustomViewHolder> {

    public List<InStock> inStocks;
    private Activity activity;

    public MrnProductAdapter(Activity activity) {
        this.inStocks = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<InStock> inStockList) {
        inStocks.clear();
        inStocks.addAll(inStockList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mrn_product_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final InStock inStock = inStocks.get(i);
        if (BuildConfig.TYPE.equals("Admin") || BuildConfig.TYPE.equals("Account")) {
            customViewHolder.txtPerNumPrice.setVisibility(View.VISIBLE);
            customViewHolder.txtAmount.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.txtPerNumPrice.setVisibility(View.GONE);
            customViewHolder.txtAmount.setVisibility(View.GONE);
        }
        if (inStock != null) {
            if (inStock.getProd() != null && inStock.getProd().getpName()!=null) {
                customViewHolder.txtProductName.setText("Product name : " + inStock.getProd().getpName() + " (" + inStock.getProd().getUnit() + ")");
            }
            if (inStock.getProUnit() != null) {
                customViewHolder.txtNumOfUnit.setText("No of Unit : " + inStock.getProUnit());
            }
            if (inStock.getPerUnit() != null) {
                customViewHolder.txtPerInUnit.setText("Per in Unit : " + inStock.getPerUnit());
            }
            if (inStock.getpInQty() != null) {
                customViewHolder.txtQuantity.setText("Quantity: " + inStock.getpInQty() + " (" + inStock.getProd().getUnit() + ")");
            }
            if (inStock.getPerUPrice() != null) {
                customViewHolder.txtPerNumPrice.setText("Per of Unit Price : ₹ " + inStock.getPerUPrice());
            }
            if (inStock.getPrice() != null) {
                customViewHolder.txtAmount.setText("Amount : ₹ " + inStock.getPrice());
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
        return (null != inStocks ? inStocks.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName, txtNumOfUnit, txtPerInUnit, txtQuantity, txtPerNumPrice, txtAmount;
        public LinearLayout cardView, layAccount;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtNumOfUnit = view.findViewById(R.id.txt_num_of_unit);
            this.txtPerInUnit = view.findViewById(R.id.txt_per_in_unit);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
            this.layAccount = view.findViewById(R.id.layout_account);
            this.txtPerNumPrice = view.findViewById(R.id.txt_per_num_price);
            this.txtAmount = view.findViewById(R.id.txt_amount);
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
                inStocks.remove(pos);
                notifyDataSetChanged();
                ((AddMRNFragment) ((MrnActivity) activity).getSupportFragmentManager().findFragmentByTag(AppConstants.FRAGMENT_ADD_MRN)).update();
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
