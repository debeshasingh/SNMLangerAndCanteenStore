

package com.example.snmlangerandcanteenstore.fragment.indent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.BuildConfig;
import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.IndentUpdateProductAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.example.snmlangerandcanteenstore.model.OutStock;
import com.example.snmlangerandcanteenstore.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class UpdateIndentFragment extends Fragment implements View.OnClickListener, HelperInterface, AdapterView.OnItemSelectedListener {

    private EditText edtChallanNo, edtIndentNumber, edtDate, edtCreatedBy;
    private TextInputLayout inputIndentNumber, inputChallanNo, inputDate, inputCratedBy;
    private Button btnAddIndent, btnAddMrnProduct;
    private DatabaseReference reference;
    private ImageView imgBack;
    private Spinner spinnerCanteen;
    private Canteen canteen;
    private RecyclerView rcyMrnProd;
    private IndentUpdateProductAdapter indentProductAdapter;
    private TextView txtFinalQuantity;
    private Indent indent;
    private List<Canteen> canteens = new ArrayList<>();
    private User user;

    public UpdateIndentFragment() {
    }

    public static Fragment newInstance(Indent indent) {
        UpdateIndentFragment updateMRNFragment = new UpdateIndentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("indent", new Gson().toJson(indent));
        updateMRNFragment.setArguments(bundle);
        return updateMRNFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String str_indent = bundle.getString("indent");
        indent = new Gson().fromJson(str_indent, Indent.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_indent, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddIndent = view.findViewById(R.id.btn_add_indent);
        btnAddIndent.setOnClickListener(this);
        btnAddMrnProduct = view.findViewById(R.id.btn_add_row);
        btnAddMrnProduct.setOnClickListener(this);

        edtIndentNumber = view.findViewById(R.id.edt_indent);
        edtChallanNo = view.findViewById(R.id.edt_challan_no);
        edtDate = view.findViewById(R.id.edt_date);
        edtCreatedBy = view.findViewById(R.id.edt_created_by);

        inputIndentNumber = view.findViewById(R.id.input_indent);
        inputChallanNo = view.findViewById(R.id.input_challan_no);
        inputDate = view.findViewById(R.id.input_date);
        inputCratedBy = view.findViewById(R.id.input_created_by);

        txtFinalQuantity = view.findViewById(R.id.txt_final_quantity);

        spinnerCanteen = view.findViewById(R.id.spinner_canteen);

        rcyMrnProd = view.findViewById(R.id.rcy_product);
        rcyMrnProd.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentProductAdapter = new IndentUpdateProductAdapter(getActivity());
        rcyMrnProd.setAdapter(indentProductAdapter);

        btnAddIndent.setVisibility(View.GONE);
        btnAddMrnProduct.setVisibility(View.GONE);
        spinnerCanteen.setEnabled(false);
        edtIndentNumber.setEnabled(false);
        edtChallanNo.setEnabled(false);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_INDENT);

        if (getHelper().getCanteens(getActivity()) != null && getHelper().getCanteens(getActivity()).size() > 0) {
            canteens.addAll(getHelper().getCanteens(getActivity()));
            Canteen canteen = new Canteen();
            canteen.setcName("Select Canteen");
            canteens.add(0, canteen);
            ArrayAdapter<Canteen> adapter = new ArrayAdapter<Canteen>(getActivity(), R.layout.layout_spinner_row, canteens);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerCanteen.setAdapter(adapter);
            spinnerCanteen.setOnItemSelectedListener(this);
        } else {
            Toast.makeText(getActivity(), "Please Add Canteens", Toast.LENGTH_SHORT).show();
        }

        if (getHelper().isLogin(getActivity()) && getHelper().getUser(getActivity()) != null) {
            user = getHelper().getUser(getActivity());
        }

        if (user!=null && user.getType().equals("Admin") || user!=null && user.getType().equals("Account")) {
            btnAddIndent.setVisibility(View.VISIBLE);
            spinnerCanteen.setEnabled(true);
            spinnerCanteen.setEnabled(true);
            edtIndentNumber.setEnabled(true);
            edtChallanNo.setEnabled(true);
        }
        updateView(indent);


        return view;
    }

    private void updateView(Indent indent) {
        if (indent.getcBy() != null) {
            edtCreatedBy.setText(indent.getcBy());
        }
        if (indent.getcDate() != null) {
            edtDate.setText(indent.getcDate());
        }
        if (indent.getcNo() != null) {
            edtChallanNo.setText(indent.getcNo());
        }
        if (indent.getIndId() != null) {
            edtIndentNumber.setText(indent.getIndId());
        }
        if (indent.getoStocks() != null && indent.getoStocks().size() > 0) {
            indentProductAdapter.swap(indent.getoStocks(), indent);
        } else {
            indentProductAdapter.swap(new ArrayList<OutStock>(), null);
        }
        if (indentProductAdapter.outStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(indentProductAdapter.outStocks));
        }

        if (indent.getcName() != null && canteens != null) {
            int index = 0;
            for (int i = 0; i < canteens.size(); i++) {
                Canteen canteen = canteens.get(i);
                if (canteen != null && canteen.getcName().equals(indent.getcName())) {
                    index = i;
                    break;
                }
            }
            spinnerCanteen.setSelection(index);
        } else {
            spinnerCanteen.setSelection(0);
        }

    }

    public void update() {
        if (indentProductAdapter.outStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(indentProductAdapter.outStocks));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_indent:
                validateIndent();
                break;
            case R.id.img_back:
                ((IndentActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateIndent() {

        inputIndentNumber.setErrorEnabled(false);
        inputChallanNo.setErrorEnabled(false);

        String str_indent = edtIndentNumber.getText().toString().trim();
        String challan = edtChallanNo.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String createdBy = edtCreatedBy.getText().toString().trim();

        if (!TextUtils.isEmpty(str_indent)) {
            if (canteen != null) {
                if (indentProductAdapter.outStocks != null && indentProductAdapter.outStocks.size() > 0) {
                    Indent input_mrn = new Indent();
                    input_mrn.setIndId(str_indent);
                    input_mrn.setcNo(challan);
                    input_mrn.setcName(canteen.getcName());
                    input_mrn.setfQuantity(String.valueOf(fQuantity(indentProductAdapter.outStocks)));
                    input_mrn.setcDate(date);
                    input_mrn.setcBy(createdBy);
                    input_mrn.setoStocks(indentProductAdapter.outStocks);

                    reference.child(str_indent).setValue(input_mrn);
                    Toast.makeText(getActivity(), "Indent Updated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Please Add Product", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputIndentNumber.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Indent", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idd = parent.getId();
        if (idd == R.id.spinner_canteen) {
            if (spinnerCanteen.getSelectedItemPosition() > 0) {
                canteen = (Canteen) spinnerCanteen.getSelectedItem();
            } else {
                canteen = null;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private float fQuantity(List<OutStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }

}

