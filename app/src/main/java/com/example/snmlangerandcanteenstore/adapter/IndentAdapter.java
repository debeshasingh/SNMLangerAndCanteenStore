
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

import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.fragment.indent.UpdateIndentFragment;
import com.example.snmlangerandcanteenstore.fragment.mrn.UpdateMRNFragment;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.example.snmlangerandcanteenstore.model.Mrn;

import java.util.ArrayList;
import java.util.List;


public class IndentAdapter extends RecyclerView.Adapter<IndentAdapter.CustomViewHolder> {

    private List<Indent> indents;
    private Activity activity;

    public IndentAdapter(Activity activity) {
        this.indents = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<Indent> indentList) {
        indents.clear();
        indents.addAll(indentList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_indent_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Indent indent = indents.get(i);
        if(indent.getIndId()!=null){
            customViewHolder.txtIndentId.setText("Indent Id : "+indent.getIndId());
        }
        if(indent.getcName() !=null){
            customViewHolder.txtCanteenName.setText("Canteen Name : "+indent.getcName());
        }

        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailMrn(indent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != indents ? indents.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtIndentId, txtCanteenName;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtIndentId = view.findViewById(R.id.txt_indent_number);
            this.txtCanteenName = view.findViewById(R.id.txt_canteen_name);
        }
    }

    public void detailMrn(Indent indent) {
        FragmentTransaction fragmentTransaction = ((IndentActivity) activity).getSupportFragmentManager().beginTransaction();
        Fragment fragment = UpdateIndentFragment.newInstance(indent);
        fragmentTransaction.add(R.id.frame_indent, fragment, AppConstants.FRAGMENT_DETAIL_INDENT);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_DETAIL_INDENT);
        fragmentTransaction.commit();
    }
}
