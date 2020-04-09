

package com.example.snmlangerandcanteenstore.fragment.mrn;

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

import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.MrnUpdateProductAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.Mrn;
import com.example.snmlangerandcanteenstore.model.User;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class UpdateMRNFragment extends Fragment implements View.OnClickListener, HelperInterface, AdapterView.OnItemSelectedListener {

    private EditText edtBillNo, edtMrnNumber, edtDate, edtCreatedBy, edtDriver, edtDriverContact;
    private TextInputLayout inputMrnNumber, inputBillNo, inputDate, inputCratedBy, inputDriver, inputDriverContact;
    private Button btnAddMrn, btnAddMrnProduct;
    private DatabaseReference reference;
    private ImageView imgBack;
    private Spinner spinnerVendor;
    private Vendor vendor;
    private RecyclerView rcyMrnProd;
    private MrnUpdateProductAdapter mrnProductAdapter;
    private TextView txtFinalQuantity, txtFinalAmount;
    private List<Mrn> mrns = new ArrayList<>();
    private Mrn mrn;
    private List<Vendor> vendors = new ArrayList<>();
    private User user;

    public UpdateMRNFragment() {
    }

    public static Fragment newInstance(Mrn mrn) {
        UpdateMRNFragment updateMRNFragment = new UpdateMRNFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mrn", new Gson().toJson(mrn));
        updateMRNFragment.setArguments(bundle);
        return updateMRNFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String str_mrn = bundle.getString("mrn");
        mrn = new Gson().fromJson(str_mrn, Mrn.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_mrn, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddMrn = view.findViewById(R.id.btn_add_mrn);
        btnAddMrn.setOnClickListener(this);
        btnAddMrnProduct = view.findViewById(R.id.btn_add_row);
        btnAddMrnProduct.setOnClickListener(this);

        edtMrnNumber = view.findViewById(R.id.edt_mrn);
        edtBillNo = view.findViewById(R.id.edt_bill_no);
        edtDate = view.findViewById(R.id.edt_date);
        edtCreatedBy = view.findViewById(R.id.edt_created_by);
        edtDriver = view.findViewById(R.id.edt_driver);
        edtDriverContact = view.findViewById(R.id.edt_driver_contact);

        inputMrnNumber = view.findViewById(R.id.input_mrn);
        inputBillNo = view.findViewById(R.id.input_bill_no);
        inputDate = view.findViewById(R.id.input_date);
        inputCratedBy = view.findViewById(R.id.input_created_by);
        inputDriver = view.findViewById(R.id.input_driver);
        inputDriverContact = view.findViewById(R.id.input_driver_contact);

        txtFinalQuantity = view.findViewById(R.id.txt_final_quantity);
        txtFinalAmount = view.findViewById(R.id.txt_final_amount);

        spinnerVendor = view.findViewById(R.id.spinner_vendor);

        rcyMrnProd = view.findViewById(R.id.rcy_product);
        rcyMrnProd.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrnProductAdapter = new MrnUpdateProductAdapter(getActivity());
        rcyMrnProd.setAdapter(mrnProductAdapter);

        btnAddMrn.setVisibility(View.GONE);
        btnAddMrnProduct.setVisibility(View.GONE);
        txtFinalAmount.setVisibility(View.GONE);
        spinnerVendor.setEnabled(false);
        edtMrnNumber.setEnabled(false);
        edtBillNo.setEnabled(false);
        edtDriver.setEnabled(false);
        edtDriverContact.setEnabled(false);

        if (getHelper().isLogin(getActivity()) && getHelper().getUser(getActivity()) != null) {
            user = getHelper().getUser(getActivity());
        }

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_MRN);

        if (getHelper().getVendors(getActivity()) != null && getHelper().getVendors(getActivity()).size() > 0) {
            vendors.addAll(getHelper().getVendors(getActivity()));
            Vendor prodUnit = new Vendor();
            prodUnit.setvName("Select Vendor");
            vendors.add(0, prodUnit);
            ArrayAdapter<Vendor> adapter = new ArrayAdapter<Vendor>(getActivity(), R.layout.layout_spinner_row, vendors);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerVendor.setAdapter(adapter);
            spinnerVendor.setOnItemSelectedListener(this);
        } else {
            Toast.makeText(getActivity(), "Please Add Units List", Toast.LENGTH_SHORT).show();
        }

        if (getHelper().getMrns(getActivity()) != null && getHelper().getMrns(getActivity()).size() > 0) {
            mrns.addAll(getHelper().getMrns(getActivity()));
        }

        if (user != null && user.getType().equals("Admin") || user != null && user.getType().equals("Account")) {
            btnAddMrn.setVisibility(View.VISIBLE);
            spinnerVendor.setEnabled(true);
            txtFinalAmount.setVisibility(View.GONE);
            spinnerVendor.setEnabled(true);
            edtMrnNumber.setEnabled(true);
            edtBillNo.setEnabled(true);
            edtDriver.setEnabled(true);
            edtDriverContact.setEnabled(true);

            txtFinalAmount.setVisibility(View.VISIBLE);

        }
        updateView(mrn);


        return view;
    }


    private void updateView(Mrn mrn) {
        if (mrn.getcBy() != null) {
            edtCreatedBy.setText(mrn.getcBy());
        }
        if (mrn.getcDate() != null) {
            edtDate.setText(mrn.getcDate());
        }
        if (mrn.getBillNo() != null) {
            edtBillNo.setText(mrn.getBillNo());
        }
        if (mrn.getMrnId() != null) {
            edtMrnNumber.setText(mrn.getMrnId());
        }
        if (mrn.getDriver() != null) {
            edtDriver.setText(mrn.getDriver());
        }
        if (mrn.getDriCon() != null) {
            edtDriverContact.setText(mrn.getDriCon());
        }
        if (mrn.getInStocks() != null && mrn.getInStocks().size() > 0) {
            mrnProductAdapter.swap(mrn.getInStocks(), mrn);
        } else {
            mrnProductAdapter.swap(new ArrayList<InStock>(), null);
        }

        if (mrnProductAdapter.inStocks != null) {
            txtFinalAmount.setText("Total Amount : ₹ " + fAmount(mrnProductAdapter.inStocks));
        }
        if (mrnProductAdapter.inStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(mrnProductAdapter.inStocks));
        }

        if (mrn.getvName() != null && vendors != null) {
            int index = 0;
            for (int i = 0; i < vendors.size(); i++) {
                Vendor vendor = vendors.get(i);
                if (vendor != null && vendor.getvName().equals(mrn.getvName())) {
                    index = i;
                    break;
                }
            }
            spinnerVendor.setSelection(index);
        } else {
            spinnerVendor.setSelection(0);
        }

    }

    public void update() {

        if (mrnProductAdapter.inStocks != null) {
            txtFinalAmount.setText("Total Amount : ₹ " + fAmount(mrnProductAdapter.inStocks));
        }
        if (mrnProductAdapter.inStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(mrnProductAdapter.inStocks));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_mrn:
                validateMrn();
                break;
            case R.id.img_back:
                ((MrnActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateMrn() {

        inputMrnNumber.setErrorEnabled(false);
        inputBillNo.setErrorEnabled(false);
        inputDriver.setErrorEnabled(false);

        String str_mrn = edtMrnNumber.getText().toString().trim();
        String bill = edtBillNo.getText().toString().trim();
        String driver = edtDriver.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String createdBy = edtCreatedBy.getText().toString().trim();
        String driverContact = edtDriverContact.getText().toString().trim();

        if (!TextUtils.isEmpty(str_mrn)) {
            if (!TextUtils.isEmpty(bill)) {
                if (!TextUtils.isEmpty(driver)) {
                    if (!TextUtils.isEmpty(driverContact) && driverContact.length()==10) {
                        if (vendor != null) {
                            if (mrnProductAdapter.inStocks != null && mrnProductAdapter.inStocks.size() > 0) {
                                float fQuantity = fQuantity(mrnProductAdapter.inStocks);
                                float fAmount = fAmount(mrnProductAdapter.inStocks);

                                Mrn input_mrn = new Mrn();
                                input_mrn.setMrnId(str_mrn);
                                input_mrn.setBillNo(bill);
                                input_mrn.setDriver(driver);
                                input_mrn.setDriCon(driverContact);
                                input_mrn.setvName(vendor.getvName());
                                input_mrn.setfAmount(String.valueOf(fAmount));
                                input_mrn.setfQuantity(String.valueOf(fQuantity));
                                input_mrn.setcDate(date);
                                input_mrn.setcBy(createdBy);
                                input_mrn.setInStocks(mrnProductAdapter.inStocks);

                                reference.child(str_mrn).setValue(input_mrn);
                                Toast.makeText(getActivity(), "MRN Updated", Toast.LENGTH_SHORT).show();
                                getActivity().finish();

                            } else {
                                Toast.makeText(getActivity(), "Please Add Product", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        inputDriverContact.setErrorEnabled(true);
                        Toast.makeText(getActivity(), "Please Enter Driver Contact or valid number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    inputDriver.setErrorEnabled(true);
                    Toast.makeText(getActivity(), "Please Enter Driver detail", Toast.LENGTH_SHORT).show();
                }
            } else {
                inputBillNo.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Please Enter Bill No", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputMrnNumber.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter MRN", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idd = parent.getId();
        if (idd == R.id.spinner_vendor) {
            if (spinnerVendor.getSelectedItemPosition() > 0) {
                vendor = (Vendor) spinnerVendor.getSelectedItem();
            } else {
                vendor = null;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private float fQuantity(List<InStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }

    private float fAmount(List<InStock> items) {

        float totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += Float.valueOf(items.get(i).getPrice());
        }

        return totalPrice;
    }

}

