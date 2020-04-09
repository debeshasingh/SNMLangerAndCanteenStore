

package com.example.snmlangerandcanteenstore.fragment.unit;

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
import com.example.snmlangerandcanteenstore.UnitActivity;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class AddProdUnitFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtName;
    private TextInputLayout inputName;
    private Button btnAddUnit;
    private DatabaseReference reference;
    private Long maxId = 0L;
    private ImageView imgBack;


    public static Fragment newInstance() {
        return new AddProdUnitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_unit, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddUnit = view.findViewById(R.id.btn_add_unit);
        btnAddUnit.setOnClickListener(this);

        edtName = view.findViewById(R.id.edt_name);

        inputName = view.findViewById(R.id.input_name);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_UNIT);

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
            case R.id.btn_add_unit:
                addUnit();
                break;
            case R.id.img_back:
                ((UnitActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void addUnit() {

        inputName.setErrorEnabled(false);

        String name = edtName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (!isUnit(name)) {
                ProdUnit canteen = new ProdUnit();
                canteen.setuName(name);
                canteen.setId(String.valueOf(maxId + 1));

                reference.child(String.valueOf(maxId)).setValue(canteen);
                Toast.makeText(getActivity(), "Unit Added", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                inputName.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Unit Name already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputName.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Unit Name", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUnit(String key) {
        if (getHelper().getProdUnit(getActivity()) != null && getHelper().getProdUnit(getActivity()).size() > 0) {
            for (ProdUnit prodUnit : getHelper().getProdUnit(getActivity())) {
                if (prodUnit.getuName().toLowerCase().equals(key.toLowerCase()))
                    return true;
            }
        }
        return false;
    }
}

