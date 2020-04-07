

package com.example.snmlangerandcanteenstore.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


public class SignUpFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtEmail, edtMobile, edtPass, edtRePass, edtFirstName, edtLastName;
    private TextInputLayout inputEmail, inputFirstName, inputLastName, inputMobile, inputPass, inputRePass;
    private Button btnSignUp;
    private DatabaseReference reference;
    private Spinner spinnerType;
    private Long maxId = 0L;
    private ImageView imgBack;


    public static Fragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

        edtFirstName = view.findViewById(R.id.edt_first_name);
        edtLastName = view.findViewById(R.id.edt_last_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtMobile = view.findViewById(R.id.edt_mobile);
        edtPass = view.findViewById(R.id.edt_pass);
        edtRePass = view.findViewById(R.id.edt_re_pass);

        inputFirstName = view.findViewById(R.id.input_first_name);
        inputLastName = view.findViewById(R.id.input_last_name);
        inputEmail = view.findViewById(R.id.input_email);
        inputMobile = view.findViewById(R.id.input_mobile);
        inputPass = view.findViewById(R.id.input_pass);
        inputRePass = view.findViewById(R.id.input_re_pass);

        spinnerType = view.findViewById(R.id.spinner_type);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_USER);

        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        maxId = (dataSnapshot.getChildrenCount());
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
            case R.id.btn_sign_up:
                validateSignUp();
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

    private void validateSignUp() {

        inputFirstName.setErrorEnabled(false);
        inputLastName.setErrorEnabled(false);
        inputEmail.setErrorEnabled(false);
        inputMobile.setErrorEnabled(false);
        inputPass.setErrorEnabled(false);
        inputRePass.setErrorEnabled(false);

        String first_name = edtFirstName.getText().toString().trim();
        String last_name = edtLastName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String type = "";
        if (spinnerType.getSelectedItemId() > 0) {
            type = spinnerType.getSelectedItem().toString().trim();
        }
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String repass = edtRePass.getText().toString().trim();

        if (!TextUtils.isEmpty(first_name)) {
            if (!TextUtils.isEmpty(last_name)) {
                if (!TextUtils.isEmpty(mobile) && mobile.length() == 10) {
                    if (!TextUtils.isEmpty(email)) {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            if (!TextUtils.isEmpty(type) && type.length() > 0) {
                                if (!TextUtils.isEmpty(pass)) {
                                    if (!TextUtils.isEmpty(repass)) {
                                        if (pass.equals(repass)) {
                                            User user = new User();
                                            user.setFname(first_name);
                                            user.setLname(last_name);
                                            user.setMobile(mobile);
                                            user.setEmail(email);
                                            user.setType(type);
                                            user.setPassword(repass);
                                            user.setUserId(String.valueOf(maxId + 1));

                                            reference.child(String.valueOf(maxId)).setValue(user);
                                            Toast.makeText(getActivity(), "User Added", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();

                                        } else {
                                            Toast.makeText(getActivity(), "Password and Confirm Password dose not match", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        inputRePass.setErrorEnabled(true);
                                        Toast.makeText(getActivity(), "Please Enter Re-Password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    inputPass.setErrorEnabled(true);
                                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Select User Type", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            inputEmail.setErrorEnabled(true);
                            Toast.makeText(getActivity(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        inputEmail.setErrorEnabled(true);
                        Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            } else {
                inputLastName.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputFirstName.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }


    }
}
