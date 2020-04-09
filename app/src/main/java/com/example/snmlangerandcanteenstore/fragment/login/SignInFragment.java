

package com.example.snmlangerandcanteenstore.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.snmlangerandcanteenstore.HomeActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.SignInActivity;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SignInFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtEmail, edtPass;
    private TextInputLayout inputEmail, inputPass;
    private Button btnSignIn;
    private DatabaseReference reference;
    private ImageView imgBack;
    private boolean isLoding = false;
    private boolean isFound = false;


    public static Fragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);

        edtEmail = view.findViewById(R.id.edt_email);
        edtPass = view.findViewById(R.id.edt_pass);
        inputEmail = view.findViewById(R.id.input_email);
        inputPass = view.findViewById(R.id.input_pass);

        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in:
                if (!isLoding) {
                    isLoding = true;
                    validateSignIn();
                }
                break;
            case R.id.img_back:
                ((SignInActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateSignIn() {

        inputEmail.setErrorEnabled(false);
        inputPass.setErrorEnabled(false);

        final String email = edtEmail.getText().toString().trim();
        final String pass = edtPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email)) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!TextUtils.isEmpty(pass)) {
                    reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_USER);
                    if (reference != null) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                List<User> users = new ArrayList<>();
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        User user = postSnapshot.getValue(User.class);
                                        users.add(user);
                                    }

                                    if(users.size()>0){
                                        for (User user : users) {
                                            if (user.getEmail().equals(email) && user.getPassword().equals(pass)) {
                                                isFound = true;
                                                login(user);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    inputPass.setErrorEnabled(true);
                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                inputEmail.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputEmail.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        }


    }

    private void login(User user) {
        if(isFound){
            getHelper().setLogin(getActivity(), true);
            getHelper().setUser(getActivity(), user);
            startActivity(new Intent(getActivity(), HomeActivity.class));
            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            isLoding = false;
            getActivity().finish();
        }else {
            Toast.makeText(getActivity(), "Invalid User name or password", Toast.LENGTH_SHORT).show();
        }
    }
}
