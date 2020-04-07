

package com.example.snmlangerandcanteenstore.fragment.indent;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.IndentProductAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.example.snmlangerandcanteenstore.model.OutStock;
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


public class AddIndentFragment extends Fragment implements View.OnClickListener, HelperInterface, AdapterView.OnItemSelectedListener {

    private EditText edtChallanNo, edtIndentNumber, edtDate, edtCreatedBy,edtSearch;
    private TextInputLayout inputIndentNumber, inputChallanNo, inputDate, inputCratedBy;
    private Button btnAddIndent, btnAddMrnProduct;
    private DatabaseReference reference;
    private ImageView imgBack;
    private Spinner spinnerCanteen;
    private Canteen canteen;
    private RecyclerView rcyMrnProd;
    private IndentProductAdapter indentProductAdapter;
    private Product product;
    private List<OutStock> perInstock = new ArrayList<>();
    private float final_qty;
    private TextView txtFinalQuantity;
    private List<Indent> indents = new ArrayList<>();
    private EditText edtSearchProd;
    private Spinner spinnerProduct;


    public static Fragment newInstance() {
        return new AddIndentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_indent, container, false);

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
        edtSearch = view.findViewById(R.id.edt_search);

        inputIndentNumber = view.findViewById(R.id.input_indent);
        inputChallanNo = view.findViewById(R.id.input_challan_no);
        inputDate = view.findViewById(R.id.input_date);
        inputCratedBy = view.findViewById(R.id.input_created_by);

        txtFinalQuantity = view.findViewById(R.id.txt_final_quantity);

        spinnerCanteen = view.findViewById(R.id.spinner_canteen);

        rcyMrnProd = view.findViewById(R.id.rcy_product);
        rcyMrnProd.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentProductAdapter = new IndentProductAdapter(getActivity());
        rcyMrnProd.setAdapter(indentProductAdapter);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        edtDate.setText(formatter.format(date));

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_INDENT);

        if (getHelper().getUser(getActivity()) != null) {
            edtCreatedBy.setText(getHelper().getUser(getActivity()).getFname() + " " + getHelper().getUser(getActivity()).getLname());
        }

        setCanteen();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setCanteen();
            }
        });
        updateIndents();

        return view;
    }

    private void setCanteen() {
        String str_search = edtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            List<Canteen> canteens = new ArrayList<>();
            canteens.addAll(getSearchList(str_search));
            Canteen canteen = new Canteen();
            canteen.setcName("Select Canteen");
            canteens.add(0, canteen);
            ArrayAdapter<Canteen> adapter = new ArrayAdapter<Canteen>(getActivity(), R.layout.layout_spinner_row, canteens);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerCanteen.setAdapter(adapter);
            spinnerCanteen.setOnItemSelectedListener(this);

        } else {
            if (getHelper().getCanteens(getActivity()).size() > 0) {
                List<Canteen> canteens = new ArrayList<>();
                canteens.addAll(getHelper().getCanteens(getActivity()));
                Canteen canteen = new Canteen();
                canteen.setcName("Select Canteen");
                canteens.add(0, canteen);
                ArrayAdapter<Canteen> adapter = new ArrayAdapter<Canteen>(getActivity(), R.layout.layout_spinner_row, canteens);
                adapter.setDropDownViewResource(R.layout.layout_spinner_row);
                spinnerCanteen.setAdapter(adapter);
                spinnerCanteen.setOnItemSelectedListener(this);
            } else {
                Toast.makeText(getActivity(), "Please Add Units List", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private List<Canteen> getSearchList(String key) {
        List<Canteen> canteens = new ArrayList<>();
        if (getHelper().getCanteens(getActivity()).size() > 0) {
            for (Canteen canteen : getHelper().getCanteens(getActivity())) {
                if (canteen.getcName().toLowerCase().startsWith(key.toLowerCase()))
                    canteens.add(canteen);
            }
        }
        return canteens;
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


    private void updateIndents() {

        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Indent> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Indent indent = postSnapshot.getValue(Indent.class);
                        list.add(indent);
                    }

                    if (list != null) {
                        Log.d("debesh", "units Result: " + new Gson().toJson(list));
                        getHelper().setIndents(getActivity(), list);
                        indents.addAll(list);
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
            case R.id.btn_add_indent:
                updateIndents();
                validateMrn();
                break;
            case R.id.img_back:
                ((IndentActivity) getActivity()).onBack();
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

        inputIndentNumber.setErrorEnabled(false);
        inputChallanNo.setErrorEnabled(false);

        String str_indent = edtIndentNumber.getText().toString().trim();
        String challan = edtChallanNo.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String createdBy = edtCreatedBy.getText().toString().trim();

        if (!TextUtils.isEmpty(str_indent)) {
            if (!isIndent(str_indent)) {
                if (canteen != null) {
                    if (indentProductAdapter.outStocks != null && indentProductAdapter.outStocks.size() > 0) {
                        Indent input_indent = new Indent();
                        input_indent.setIndId(str_indent);
                        input_indent.setcNo(challan);
                        input_indent.setcName(canteen.getcName());
                        input_indent.setfQuantity(String.valueOf(fQuantity(indentProductAdapter.outStocks)));
                        input_indent.setcDate(date);
                        input_indent.setcBy(createdBy);
                        input_indent.setoStocks(indentProductAdapter.outStocks);

                        reference.child(str_indent).setValue(input_indent);
                        Toast.makeText(getActivity(), "Indent Added", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), "Please Add Product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Indent already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputIndentNumber.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Indent Number", Toast.LENGTH_SHORT).show();
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

    private void addRowDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(getActivity(), R.style.AppDialogThemeAccentNoTitle);
        dialog.setContentView(R.layout.dialog_add_indent_product_row);
        dialog.setCancelable(false);
        final TextView txtQuantity = dialog.findViewById(R.id.txt_quantity);

        final Button btnAddProduct = dialog.findViewById(R.id.btn_add);
        final TextView btnCancel = dialog.findViewById(R.id.btn_cancel);

        final EditText edtPerInUnit = dialog.findViewById(R.id.edt_per_in_unit);
        final EditText edtNumberOfUnit = dialog.findViewById(R.id.edt_no_of_unit);
        final EditText edtRemark = dialog.findViewById(R.id.edt_remark);
        edtSearchProd = dialog.findViewById(R.id.edt_search);

        final TextInputLayout inputNumberOfUnit = dialog.findViewById(R.id.input_no_of_unit);
        final TextInputLayout inputPerInUnit = dialog.findViewById(R.id.input_per_in_unit);
        final TextInputLayout inputRemark = dialog.findViewById(R.id.input_remark);

        spinnerProduct = dialog.findViewById(R.id.spinner_product);
        txtQuantity.setText("Quantity : 0.0");

        updateProd();

        edtSearchProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateProd();
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
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity+ product.getUnit());
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
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
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputNumberOfUnit.setErrorEnabled(false);
                inputPerInUnit.setErrorEnabled(false);

                String numberOfUnit = edtNumberOfUnit.getText().toString().trim();
                String perInUnit = edtPerInUnit.getText().toString().trim();
                String quantity = String.valueOf(final_qty);
                String remark = edtRemark.getText().toString().trim();

                String indent = edtIndentNumber.getText().toString().trim();
                String challan = edtChallanNo.getText().toString().trim();
                String createdBy = edtCreatedBy.getText().toString().trim();
                String date = edtDate.getText().toString().trim();

                perInstock.clear();
                Log.d("Debesh", "Adapter List : " + new Gson().toJson(indentProductAdapter.outStocks));
                perInstock.addAll(indentProductAdapter.outStocks);
                Log.d("Debesh", "Adapter List add to perInStock : " + new Gson().toJson(perInstock));

                if (product != null) {
                    if (!TextUtils.isEmpty(numberOfUnit)) {
                        if (!TextUtils.isEmpty(perInUnit)) {
                            if(canteen !=null){
                                if(!TextUtils.isEmpty(indent) && !isIndent(indent)){
                                    OutStock outStock = new OutStock();
                                    outStock.setProd(product);
                                    outStock.setProUnit(numberOfUnit);
                                    outStock.setPerUnit(perInUnit);
                                    outStock.setpInQty(quantity);
                                    outStock.setRemark(remark);
                                    outStock.setcName(canteen.getcName());
                                    outStock.setIndNo(indent);
                                    outStock.setcBy(createdBy);
                                    outStock.setcDate(date);
                                    outStock.setcNo(challan);
                                    perInstock.add(outStock);
                                    indentProductAdapter.swap(perInstock);
                                    txtFinalQuantity.setText("Total Quantity : "+fQuantity(perInstock));
                                    Log.d("Debesh", "perInstock : " + new Gson().toJson(perInstock));
                                    dialog.cancel();
                                }else {
                                    Toast.makeText(getActivity(), "Please Enter Indent Number or Indent already exist", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "Please Select Canteen", Toast.LENGTH_SHORT).show();
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

    private void updateProd() {
        String str_search = edtSearchProd.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            List<Product> products = new ArrayList<>();
            products.addAll(getSearchProducts(str_search));
            Product prodUnit = new Product();
            prodUnit.setpName("Select Product");
            products.add(0, prodUnit);
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
                Product prodUnit = new Product();
                prodUnit.setpName("Select Product");
                products.add(0, prodUnit);
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

    private void upadteQty(float quantity) {
        if (quantity > 0) {
            final_qty = quantity;
        }
    }

    private float fQuantity(List<OutStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }

    private boolean isIndent(String indentId) {

        if (indents != null && indents.size() > 0) {
            for (Indent indent : indents) {
                if (indent.getIndId().equals(indentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update(){

        if (indentProductAdapter.outStocks != null) {
            txtFinalQuantity.setText("Total Quantity : " + fQuantity(indentProductAdapter.outStocks));
        }
    }

}

