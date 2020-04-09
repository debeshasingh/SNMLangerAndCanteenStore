

package com.example.snmlangerandcanteenstore.fragment.vendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.VendorActivity;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class AddVendorFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtMobile, edtName, edtDetail;
    private TextInputLayout inputName, inputMobile, inputDeatil;
    private Button btnAddVendor;
    private DatabaseReference reference;
    private Long maxId = 0L;
    private ImageView imgBack;


    public static Fragment newInstance() {
        return new AddVendorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vendor, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddVendor = view.findViewById(R.id.btn_add_vendor);
        btnAddVendor.setOnClickListener(this);

        edtName = view.findViewById(R.id.edt_name);
        edtMobile = view.findViewById(R.id.edt_mobile);
        edtDetail = view.findViewById(R.id.edt_detail);

        inputName = view.findViewById(R.id.input_name);
        inputMobile = view.findViewById(R.id.input_mobile);
        inputDeatil = view.findViewById(R.id.input_detail);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_VENDOR);

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
            case R.id.btn_add_vendor:
                validateVendor();
                break;
            case R.id.img_back:
                ((VendorActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateVendor() {

        inputName.setErrorEnabled(false);
        inputDeatil.setErrorEnabled(false);
        inputMobile.setErrorEnabled(false);

        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String detail = edtDetail.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if(!isVendor(name)){
                if (!TextUtils.isEmpty(mobile) && mobile.length() == 10) {
                    if (!TextUtils.isEmpty(detail) && detail.length() > 0) {
                        Vendor vendor = new Vendor();
                        vendor.setvName(name);
                        vendor.setvMob(mobile);
                        vendor.setDetail(detail);
                        vendor.setvId(String.valueOf(maxId + 1));

                        reference.child(String.valueOf(maxId)).setValue(vendor);
                        Toast.makeText(getActivity(), "Vendor Added", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), "Please Enter Detail", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }else {
                inputName.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Vendor Name already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputName.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Vendor Name", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isVendor(String key) {
        if (getHelper().getVendors(getActivity()) !=null && getHelper().getVendors(getActivity()).size() > 0) {
            for (Vendor vendor : getHelper().getVendors(getActivity())) {
                if (vendor.getvName().toLowerCase().equals(key.toLowerCase()))
                    return true;
            }
        }
        return false;
    }

}

