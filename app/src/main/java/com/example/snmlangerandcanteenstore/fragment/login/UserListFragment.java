

package com.example.snmlangerandcanteenstore.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.BuildConfig;
import com.example.snmlangerandcanteenstore.HomeActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.SignInActivity;
import com.example.snmlangerandcanteenstore.adapter.UserAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddUser;
    private RecyclerView rcyUsers;
    private DatabaseReference reference;
    private UserAdapter userAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddUser = view.findViewById(R.id.btn_add_user);
        btnAddUser.setOnClickListener(this);

        rcyUsers = view.findViewById(R.id.rcy_user);
        rcyUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        userAdapter = new UserAdapter(getActivity());
        rcyUsers.setAdapter(userAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_USER);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        users.add(user);
                    }

                    if(users!=null){
                        userAdapter.swap(users);
                    }else {
                        userAdapter.swap(new ArrayList<User>());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_user:
                addUser();
                break;
            case R.id.img_back:
                ((SignInActivity) getActivity()).onBack();
                break;
        }
    }



    public void addUser() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = SignUpFragment.newInstance();
        fragmentTransaction.add(R.id.frame_sign_in, fragment, AppConstants.FRAGMENT_SIGN_UP);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_SIGN_UP);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
