
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CustomViewHolder> {

    private List<User> users;
    private Activity activity;

    public UserAdapter(Activity activity) {
        this.users = new ArrayList<>();
        this.activity = activity;
    }

    public void swap(List<User> userList) {
        users.clear();
        users.addAll(userList);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final User user = users.get(i);
        if(user.getFname()!=null){
            customViewHolder.txtFname.setText("First Name : "+user.getFname());
        }
        if(user.getLname()!=null){
            customViewHolder.txtLname.setText("Last Name : "+user.getLname());
        }
        if(user.getEmail()!=null){
            customViewHolder.txtEmail.setText("Email ID : "+user.getEmail());
        }
        if(user.getMobile()!=null){
            customViewHolder.txtMobile.setText("Mobile : "+user.getMobile());
        }
        if(user.getType()!=null){
            customViewHolder.txtType.setText("Account Type : "+user.getType());
        }
    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtFname, txtLname,txtEmail,txtMobile,txtType;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtFname = view.findViewById(R.id.txt_first_name);
            this.txtLname = view.findViewById(R.id.txt_last_name);
            this.txtEmail = view.findViewById(R.id.txt_email);
            this.txtMobile = view.findViewById(R.id.txt_mobile);
            this.txtType = view.findViewById(R.id.txt_type);
        }
    }
}
