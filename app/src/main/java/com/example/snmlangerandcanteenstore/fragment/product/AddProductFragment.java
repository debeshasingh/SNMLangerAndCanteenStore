

package com.example.snmlangerandcanteenstore.fragment.product;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.snmlangerandcanteenstore.ProductActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.example.snmlangerandcanteenstore.model.Category;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.example.snmlangerandcanteenstore.model.Product;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddProductFragment extends Fragment implements View.OnClickListener, HelperInterface, AdapterView.OnItemSelectedListener {

    private EditText edtName;
    private TextInputLayout inputName;
    private Button btnAddProduct;
    private DatabaseReference reference;
    private Long maxId = 0L;
    private ImageView imgBack;
    private Spinner spinnerUnit,spinnerCategory;
    private String prodUnit;
    private Category category;


    public static Fragment newInstance() {
        return new AddProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(this);

        edtName = view.findViewById(R.id.edt_name);
        spinnerUnit = view.findViewById(R.id.spinner_unit);
        spinnerCategory = view.findViewById(R.id.spinner_category);

        inputName = view.findViewById(R.id.input_name);

        if (getHelper().getProdUnit(getActivity()) != null && getHelper().getProdUnit(getActivity()).size() > 0) {
            List<ProdUnit> prodUnits = new ArrayList<>();
            prodUnits.addAll(getHelper().getProdUnit(getActivity()));
            ProdUnit prodUnit = new ProdUnit();
            prodUnit.setuName("Select Unit for product");
            prodUnits.add(0, prodUnit);
            ArrayAdapter<ProdUnit> adapter = new ArrayAdapter<ProdUnit>(getActivity(), R.layout.layout_spinner_row, prodUnits);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerUnit.setAdapter(adapter);
            spinnerUnit.setOnItemSelectedListener(this);
        }else {
            Toast.makeText(getActivity(), "Please Add Units List", Toast.LENGTH_SHORT).show();
        }

        if (getHelper().getCategory(getActivity()) != null && getHelper().getCategory(getActivity()).size() > 0) {
            List<Category> prodUnits = new ArrayList<>();
            prodUnits.addAll(getHelper().getCategory(getActivity()));
            Category prodUnit = new Category();
            prodUnit.setCatName("Select Category");
            prodUnits.add(0, prodUnit);
            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), R.layout.layout_spinner_row, prodUnits);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerCategory.setAdapter(adapter);
            spinnerCategory.setOnItemSelectedListener(this);
        }else {
            Toast.makeText(getActivity(), "Please Add category List", Toast.LENGTH_SHORT).show();
        }


        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_PRODUCT);

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
            case R.id.btn_add_product:
                validateCanteen();
                break;
            case R.id.img_back:
                ((ProductActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void validateCanteen() {

        inputName.setErrorEnabled(false);

        String name = edtName.getText().toString().trim();

        if(category !=null){
            if (!TextUtils.isEmpty(name)) {
                if(!isProd(name)){
                    if (!TextUtils.isEmpty(prodUnit) && prodUnit.length() > 0) {
                        Product product = new Product();
                        product.setCat(category);
                        product.setpName(name);
                        product.setUnit(prodUnit);
                        product.setpId(String.valueOf(maxId + 1));

                        reference.child(String.valueOf(maxId)).setValue(product);
                        Toast.makeText(getActivity(), "Product Added", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), "Please Select Unit", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    inputName.setErrorEnabled(true);
                    Toast.makeText(getActivity(), "Product Name already exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                inputName.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Please Enter Product Name", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idd = parent.getId();
        if (idd == R.id.spinner_unit) {
            if (spinnerUnit.getSelectedItemPosition() > 0) {
                prodUnit = spinnerUnit.getSelectedItem().toString();
            }else {
                prodUnit = "";
            }
        }
        if (idd == R.id.spinner_category) {
            if (spinnerCategory.getSelectedItemPosition() > 0) {
                category = (Category) spinnerCategory.getSelectedItem();
            }else {
                category = null;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isProd(String key) {
        if (getHelper().getProducts(getActivity()) != null && getHelper().getProducts(getActivity()).size() > 0) {
            for (Product category : getHelper().getProducts(getActivity())) {
                if (category.getpName().toLowerCase().equals(key.toLowerCase()))
                    return true;
            }
        }
        return false;
    }
}

