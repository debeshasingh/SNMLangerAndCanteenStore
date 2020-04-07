

package com.example.snmlangerandcanteenstore.fragment.canteen;

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

import com.example.snmlangerandcanteenstore.CanteenActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.VendorActivity;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class AddCanteenFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtMobile, edtName, edtZone;
    private TextInputLayout inputName, inputMobile, inputZone;
    private Button btnAddCanteen;
    private DatabaseReference reference;
    private Long maxId = 0L;
    private ImageView imgBack;


    public static Fragment newInstance() {
        return new AddCanteenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_canteen, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddCanteen = view.findViewById(R.id.btn_add_canteen);
        btnAddCanteen.setOnClickListener(this);

        edtName = view.findViewById(R.id.edt_name);
        edtMobile = view.findViewById(R.id.edt_mobile);
        edtZone = view.findViewById(R.id.edt_detail);

        inputName = view.findViewById(R.id.input_name);
        inputMobile = view.findViewById(R.id.input_mobile);
        inputZone = view.findViewById(R.id.input_detail);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_CANTEEN);

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
            case R.id.btn_add_canteen:
                validateCanteen();
                break;
            case R.id.img_back:
                ((CanteenActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateCanteen() {

        inputName.setErrorEnabled(false);
        inputZone.setErrorEnabled(false);
        inputMobile.setErrorEnabled(false);

        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String detail = edtZone.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(mobile) && mobile.length() == 10) {
                if (!TextUtils.isEmpty(detail) && detail.length() > 0) {
                    Canteen canteen = new Canteen();
                    canteen.setcName(name);
                    canteen.setcMob(mobile);
                    canteen.setZone(detail);
                    canteen.setcId(String.valueOf(maxId + 1));

                    reference.child(String.valueOf(maxId)).setValue(canteen);
                    Toast.makeText(getActivity(), "Canteen Added", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Please Enter Detail", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputName.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }
    }


}

