
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.fragment.mrn.AddMRNFragment;
import com.example.snmlangerandcanteenstore.fragment.mrn.UpdateMRNFragment;
import com.example.snmlangerandcanteenstore.model.Mrn;

import java.util.ArrayList;
import java.util.List;


public class MrnAdapter extends RecyclerView.Adapter<MrnAdapter.CustomViewHolder> {

    private List<Mrn> mrns;
    private Activity activity;

    public MrnAdapter(Activity activity) {
        this.mrns = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Mrn> mrnList) {
        mrns.clear();
        mrns.addAll(mrnList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mrn_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Mrn mrn = mrns.get(i);
        if(mrn.getMrnId()!=null){
            customViewHolder.txtMRNId.setText("MRN Id : "+mrn.getMrnId());
        }
        if(mrn.getvName() !=null){
            customViewHolder.txtVendorName.setText("Vendor Name : "+mrn.getvName());
        }

        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailMrn(mrn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mrns ? mrns.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMRNId, txtVendorName;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtMRNId = view.findViewById(R.id.txt_mrn_number);
            this.txtVendorName = view.findViewById(R.id.txt_vendor_name);
        }
    }

    public void detailMrn(Mrn mrn) {
        FragmentTransaction fragmentTransaction = ((MrnActivity) activity).getSupportFragmentManager().beginTransaction();
        Fragment fragment = UpdateMRNFragment.newInstance(mrn);
        fragmentTransaction.add(R.id.frame_mrn, fragment, AppConstants.FRAGMENT_DETAIL_MRN);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_DETAIL_MRN);
        fragmentTransaction.commit();
    }
}
