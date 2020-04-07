

package com.example.snmlangerandcanteenstore.fragment.mrn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.BuildConfig;
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.MrnProductAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.Mrn;
import com.example.snmlangerandcanteenstore.model.Product;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddMRNFragment extends Fragment implements View.OnClickListener, HelperInterface, AdapterView.OnItemSelectedListener {

    private EditText edtBillNo, edtMrnNumber, edtDate, edtCreatedBy, edtDriver, edtVendor;
    private TextInputLayout inputMrnNumber, inputBillNo, inputDate, inputCratedBy, inputDriver, inputVendor;
    private Button btnAddMrn, btnAddMrnProduct;
    private DatabaseReference reference;
    private ImageView imgBack;
    private Spinner spinnerVendor;
    private Vendor vendor;
    private RecyclerView rcyMrnProd;
    private MrnProductAdapter mrnProductAdapter;
    private Product product;
    private List<InStock> perInstock = new ArrayList<>();
    private float final_qty, final_amount;
    private TextView txtFinalQuantity, txtFinalAmount;
    private List<Mrn> mrns = new ArrayList<>();
    private EditText edtSearch;
    private Spinner spinnerProduct;


    public static Fragment newInstance() {
        return new AddMRNFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_mrn, container, false);

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
        edtVendor = view.findViewById(R.id.edt_vendor);

        inputMrnNumber = view.findViewById(R.id.input_mrn);
        inputBillNo = view.findViewById(R.id.input_bill_no);
        inputDate = view.findViewById(R.id.input_date);
        inputCratedBy = view.findViewById(R.id.input_created_by);
        inputDriver = view.findViewById(R.id.input_driver);
        inputVendor = view.findViewById(R.id.input_vendor);

        txtFinalQuantity = view.findViewById(R.id.txt_final_quantity);
        txtFinalAmount = view.findViewById(R.id.txt_final_amount);

        spinnerVendor = view.findViewById(R.id.spinner_vendor);

        rcyMrnProd = view.findViewById(R.id.rcy_product);
        rcyMrnProd.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrnProductAdapter = new MrnProductAdapter(getActivity());
        rcyMrnProd.setAdapter(mrnProductAdapter);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        edtDate.setText(formatter.format(date));

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_MRN);

        if (getHelper().getUser(getActivity()) != null) {
            edtCreatedBy.setText(getHelper().getUser(getActivity()).getFname() + " " + getHelper().getUser(getActivity()).getLname());
        }

        edtVendor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setVendor();
            }
        });
        setVendor();

        return view;
    }

    private void setVendor() {
        String str_search = edtVendor.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            List<Vendor> vendors = new ArrayList<>();
            vendors.addAll(getSearchList(str_search));
            Vendor prodUnit = new Vendor();
            prodUnit.setvName("Select Vendor");
            vendors.add(0, prodUnit);
            ArrayAdapter<Vendor> adapter = new ArrayAdapter<Vendor>(getActivity(), R.layout.layout_spinner_row, vendors);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerVendor.setAdapter(adapter);
            spinnerVendor.setOnItemSelectedListener(this);

        } else {
            if (getHelper().getVendors(getActivity()) != null && getHelper().getVendors(getActivity()).size() > 0) {
                List<Vendor> prodUnits = new ArrayList<>();
                prodUnits.addAll(getHelper().getVendors(getActivity()));
                Vendor vendor = new Vendor();
                vendor.setvName("Select Vendor");
                prodUnits.add(0, vendor);
                ArrayAdapter<Vendor> adapter = new ArrayAdapter<Vendor>(getActivity(), R.layout.layout_spinner_row, prodUnits);
                adapter.setDropDownViewResource(R.layout.layout_spinner_row);
                spinnerVendor.setAdapter(adapter);
                spinnerVendor.setOnItemSelectedListener(this);
            } else {
                Toast.makeText(getActivity(), "Please Add Units List", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private List<Vendor> getSearchList(String key) {
        List<Vendor> vendors = new ArrayList<>();
        if (getHelper().getVendors(getActivity()) != null && getHelper().getVendors(getActivity()).size() > 0) {
            for (Vendor vendor : getHelper().getVendors(getActivity())) {
                if (vendor.getvName().toLowerCase().startsWith(key.toLowerCase()))
                    vendors.add(vendor);
            }
        }
        return vendors;
    }

    private void updateMrns() {
        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_MRN);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Mrn> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Mrn mrn = postSnapshot.getValue(Mrn.class);
                        list.add(mrn);
                    }

                    if (list.size() > 0) {
                        Log.d("debesh", "units Result: " + new Gson().toJson(list));
                        //getHelper().setMrns(getActivity(), list);
                        mrns.addAll(list);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_mrn:
                updateMrns();
                validateMrn();
                break;
            case R.id.img_back:
                ((MrnActivity) getActivity()).onBack();
                break;
            case R.id.btn_add_row:
                addRowDialog();
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

        if (!TextUtils.isEmpty(str_mrn)) {
            if (!isMrn(str_mrn)) {
                if (!TextUtils.isEmpty(bill)) {
                    if (!isBill(bill)) {
                        if (!TextUtils.isEmpty(driver)) {
                            if (vendor != null) {
                                if (mrnProductAdapter.inStocks != null && mrnProductAdapter.inStocks.size() > 0) {
                                    float fQuantity = fQuantity(mrnProductAdapter.inStocks);
                                    float fAmount = fAmount(mrnProductAdapter.inStocks);

                                    Mrn input_mrn = new Mrn();
                                    input_mrn.setMrnId(str_mrn);
                                    input_mrn.setBillNo(bill);
                                    input_mrn.setDriver(driver);
                                    input_mrn.setvName(vendor.getvName());
                                    input_mrn.setfAmount(String.valueOf(fAmount));
                                    input_mrn.setfQuantity(String.valueOf(fQuantity));
                                    input_mrn.setcDate(date);
                                    input_mrn.setcBy(createdBy);
                                    input_mrn.setInStocks(mrnProductAdapter.inStocks);

                                    reference.child(str_mrn).setValue(input_mrn);
                                    Toast.makeText(getActivity(), "MRN Added", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();

                                } else {
                                    Toast.makeText(getActivity(), "Please Add Product", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            inputDriver.setErrorEnabled(true);
                            Toast.makeText(getActivity(), "Please Enter Driver detail", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Bill number already exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    inputBillNo.setErrorEnabled(true);
                    Toast.makeText(getActivity(), "Please Enter Bill No", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "MRN already exist", Toast.LENGTH_SHORT).show();
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

    private void addRowDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(getActivity(), R.style.AppDialogThemeAccentNoTitle);
        dialog.setContentView(R.layout.dialog_add_mrn_product_row);
        dialog.setCancelable(false);
        final TextView txtQuantity = dialog.findViewById(R.id.txt_quantity);
        final TextView txtAmount = dialog.findViewById(R.id.txt_amount);

        final Button btnAddProduct = dialog.findViewById(R.id.btn_add);
        final TextView btnCancel = dialog.findViewById(R.id.btn_cancel);

        final EditText edtPerUnitPrice = dialog.findViewById(R.id.edt_per_unit_price);
        final EditText edtPerInUnit = dialog.findViewById(R.id.edt_per_in_unit);
        final EditText edtNumberOfUnit = dialog.findViewById(R.id.edt_no_of_unit);
        final EditText edtRemark = dialog.findViewById(R.id.edt_remark);
        edtSearch = dialog.findViewById(R.id.edt_search);

        final TextInputLayout inputNumberOfUnit = dialog.findViewById(R.id.input_no_of_unit);
        final TextInputLayout inputPerUnitPrice = dialog.findViewById(R.id.input_per_unit_price);
        final TextInputLayout inputPerInUnit = dialog.findViewById(R.id.input_per_in_unit);
        final TextInputLayout inputRemark = dialog.findViewById(R.id.input_remark);
        final TextInputLayout inputSearch = dialog.findViewById(R.id.input_search);


        final LinearLayout layoutAccount = dialog.findViewById(R.id.layout_account);

        spinnerProduct = dialog.findViewById(R.id.spinner_product);

        txtQuantity.setText("Quantity : 0.0");
        txtAmount.setText("Amount : ₹ 0.0");

        if (BuildConfig.TYPE.equals("Admin") || BuildConfig.TYPE.equals("Account")) {
            layoutAccount.setVisibility(View.VISIBLE);
            txtFinalAmount.setVisibility(View.VISIBLE);
        } else {
            layoutAccount.setVisibility(View.GONE);
            txtFinalAmount.setVisibility(View.GONE);
        }

        updateProducts();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateProducts();

            }
        });

        edtNumberOfUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number_of_unit = edtNumberOfUnit.getText().toString().trim();
                String per_in_unit = edtPerInUnit.getText().toString().trim();
                String price = edtPerUnitPrice.getText().toString().trim();
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());

                            if (!TextUtils.isEmpty(price)) {
                                txtAmount.setText("Amount : ₹ " + (Float.valueOf(price) * quantity));
                            } else {
                                txtAmount.setText("Amount : ₹ 0.0");
                            }
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                            txtAmount.setText("Amount : ₹ 0.0");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                        txtAmount.setText("Amount : ₹ 0.0");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtPerInUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number_of_unit = edtNumberOfUnit.getText().toString().trim();
                String per_in_unit = edtPerInUnit.getText().toString().trim();
                String price = edtPerUnitPrice.getText().toString().trim();
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());
                            if (!TextUtils.isEmpty(price)) {
                                txtAmount.setText("Amount : ₹ " + (Float.valueOf(price) * quantity));
                            } else {
                                txtAmount.setText("Amount : ₹ 0.0");
                            }
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                            txtAmount.setText("Amount : ₹ 0.0");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                        txtAmount.setText("Amount : ₹ 0.0");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtPerUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String price = edtPerUnitPrice.getText().toString().trim();
                if (final_qty > 0 && price.length() > 0) {
                    float amount = (final_qty * Float.valueOf(price));
                    if (amount > 0) {
                        upadteAmount(amount);
                        txtAmount.setText("Amount : ₹ " + amount);
                    } else {
                        txtAmount.setText("Amount : ₹ 0.0");
                    }
                } else {
                    txtAmount.setText("Amount : ₹ 0.0");
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputNumberOfUnit.setErrorEnabled(false);
                inputPerUnitPrice.setErrorEnabled(false);
                inputPerInUnit.setErrorEnabled(false);

                String numberOfUnit = edtNumberOfUnit.getText().toString().trim();
                String perInUnit = edtPerInUnit.getText().toString().trim();
                String quantity = String.valueOf(final_qty);
                String amount = String.valueOf(final_amount);
                String perUPrice = edtPerUnitPrice.getText().toString().trim();
                String remark = edtRemark.getText().toString().trim();

                String mrn = edtMrnNumber.getText().toString().trim();
                String createdBy = edtCreatedBy.getText().toString().trim();
                String date = edtDate.getText().toString().trim();

                perInstock.clear();
                Log.d("Debesh", "Adapter List : " + new Gson().toJson(mrnProductAdapter.inStocks));
                perInstock.addAll(mrnProductAdapter.inStocks);
                Log.d("Debesh", "Adapter List add to perInStock : " + new Gson().toJson(perInstock));


                if (product != null) {
                    if (!TextUtils.isEmpty(numberOfUnit)) {
                        if (!TextUtils.isEmpty(perInUnit)) {
                            if (vendor != null) {
                                if (!TextUtils.isEmpty(mrn) && !isMrn(mrn)) {
                                    InStock inStock = new InStock();
                                    inStock.setProd(product);
                                    inStock.setProUnit(numberOfUnit);
                                    inStock.setPerUnit(perInUnit);
                                    inStock.setpInQty(quantity);
                                    inStock.setPrice(amount);
                                    inStock.setPerUPrice(perUPrice);
                                    inStock.setRemark(remark);
                                    inStock.setVenName(vendor.getvName());
                                    inStock.setMrnNo(mrn);
                                    inStock.setcBy(createdBy);
                                    inStock.setcDate(date);
                                    perInstock.add(inStock);
                                    mrnProductAdapter.swap(perInstock);
                                    txtFinalQuantity.setText("Total Quantity : " + fQuantity(perInstock));
                                    txtFinalAmount.setText("Total Amount : ₹ " + fAmount(perInstock));
                                    Log.d("Debesh", "perInstock : " + new Gson().toJson(perInstock));
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(getActivity(), "Please Enter MRN Number or MRN already exist", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Enter per in unit ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Enter number Of unit ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Select Product", Toast.LENGTH_SHORT).show();
                }

            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void upadteAmount(float amount) {
        if (amount > 0) {
            final_amount = amount;
        }
    }

    private void upadteQty(float quantity) {
        if (quantity > 0) {
            final_qty = quantity;
        }
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

    private boolean isMrn(String mrnId) {

        if (mrns != null && mrns.size() > 0) {
            for (Mrn mrn : mrns) {
                if (mrn.getMrnId().equals(mrnId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBill(String bill) {

        if (mrns != null && mrns.size() > 0) {
            for (Mrn mrn : mrns) {
                if (mrn.getBillNo().equals(bill)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update() {

        if (mrnProductAdapter.inStocks != null) {
            txtFinalAmount.setText("Total Amount : ₹ " + fAmount(mrnProductAdapter.inStocks));
        }
        if (mrnProductAdapter.inStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(mrnProductAdapter.inStocks));
        }
    }

    private List<Product> getSearchProducts(String key) {
        List<Product> products = new ArrayList<>();
        if (getHelper().getProducts(getActivity()).size() > 0) {
            for (Product product : getHelper().getProducts(getActivity())) {
                if (product.getpName().toLowerCase().startsWith(key.toLowerCase()))
                    products.add(product);
            }
        }
        return products;
    }

    private void updateProducts() {
        String str_search = edtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            List<Product> products = new ArrayList<>();
            products.addAll(getSearchProducts(str_search));
            Product prod = new Product();
            prod.setpName("Select Product");
            products.add(0, prod);
            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(getActivity(), R.layout.layout_spinner_row, products);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerProduct.setAdapter(adapter);
            spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int idd = parent.getId();
                    if (idd == R.id.spinner_product) {
                        if (spinnerProduct.getSelectedItemPosition() > 0) {
                            product = (Product) spinnerProduct.getSelectedItem();
                        } else {
                            product = null;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            if (getHelper().getProducts(getActivity()).size() > 0) {
                List<Product> products = new ArrayList<>();
                products.addAll(getHelper().getProducts(getActivity()));
                Product prod = new Product();
                prod.setpName("Select Product");
                products.add(0, prod);
                ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(getActivity(), R.layout.layout_spinner_row, products);
                adapter.setDropDownViewResource(R.layout.layout_spinner_row);
                spinnerProduct.setAdapter(adapter);
                spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int idd = parent.getId();
                        if (idd == R.id.spinner_product) {
                            if (spinnerProduct.getSelectedItemPosition() > 0) {
                                product = (Product) spinnerProduct.getSelectedItem();
                            } else {
                                product = null;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please Add Products", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

