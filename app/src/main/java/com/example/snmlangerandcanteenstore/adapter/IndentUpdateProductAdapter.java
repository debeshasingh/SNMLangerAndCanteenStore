
package com.example.snmlangerandcanteenstore.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.BuildConfig;
import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.indent.UpdateIndentFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.example.snmlangerandcanteenstore.model.OutStock;
import com.example.snmlangerandcanteenstore.model.Product;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class IndentUpdateProductAdapter extends RecyclerView.Adapter<IndentUpdateProductAdapter.CustomViewHolder> implements HelperInterface {

    public List<OutStock> outStocks;
    private Activity activity;
    private Product product;
    private List<Product> products;
    private Indent indent;
    private float final_qty;
    private Spinner spinnerProduct;
    private EditText edtSearch;

    public IndentUpdateProductAdapter(Activity activity) {
        this.outStocks = new ArrayList<>();
        this.activity = activity;
        this.product = new Product();
        this.products = new ArrayList<>();
        this.indent = new Indent();
    }

    public void swap(List<OutStock> outStockList, Indent input_mrn) {
        this.outStocks.clear();
        this.outStocks.addAll(outStockList);
        String str_indent = new Gson().toJson(input_mrn);
        indent = new Gson().fromJson(str_indent, Indent.class);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_indent_product_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final OutStock outStock = outStocks.get(i);

        if (outStock != null) {
            if (outStock.getProd() != null && outStock.getProd().getpName()!=null) {
                customViewHolder.txtProductName.setText("Product name : " + outStock.getProd().getpName() + " (" + outStock.getProd().getUnit() + ")");
            }
            if (outStock.getProUnit() != null) {
                customViewHolder.txtNumOfUnit.setText("No of Unit : " + outStock.getProUnit());
            }
            if (outStock.getPerUnit() != null) {
                customViewHolder.txtPerInUnit.setText("Per in Unit : " + outStock.getPerUnit());
            }
            if (outStock.getpInQty() != null) {
                customViewHolder.txtQuantity.setText("Quantity: " + outStock.getpInQty() + " (" + outStock.getProd().getUnit() + ")");
            }
        }

        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getHelper().getUser(activity)!=null && getHelper().getUser(activity).getType().equals("Admin") ||  getHelper().getUser(activity).getType().equals("Account")) {
                    addRowDialog(outStock, i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != outStocks ? outStocks.size() : 0);
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName, txtNumOfUnit, txtPerInUnit, txtQuantity;
        public LinearLayout cardView;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtNumOfUnit = view.findViewById(R.id.txt_num_of_unit);
            this.txtPerInUnit = view.findViewById(R.id.txt_per_in_unit);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
        }
    }

    private void addRowDialog(final OutStock outStock, final int pos) {
        final AppCompatDialog dialog = new AppCompatDialog(activity, R.style.AppDialogThemeAccentNoTitle);
        dialog.setContentView(R.layout.dialog_add_indent_product_row);
        dialog.setCancelable(false);
        final TextView txtQuantity = dialog.findViewById(R.id.txt_quantity);

        final Button btnAddProduct = dialog.findViewById(R.id.btn_add);
        final TextView btnCancel = dialog.findViewById(R.id.btn_cancel);

        final EditText edtPerInUnit = dialog.findViewById(R.id.edt_per_in_unit);
        final EditText edtNumberOfUnit = dialog.findViewById(R.id.edt_no_of_unit);
        final EditText edtRemark = dialog.findViewById(R.id.edt_remark);
        edtSearch = dialog.findViewById(R.id.edt_search);

        final TextInputLayout inputNumberOfUnit = dialog.findViewById(R.id.input_no_of_unit);
        final TextInputLayout inputPerInUnit = dialog.findViewById(R.id.input_per_in_unit);
        final TextInputLayout inputRemark = dialog.findViewById(R.id.input_remark);
        final TextInputLayout inputSearch = dialog.findViewById(R.id.input_search);

        spinnerProduct = dialog.findViewById(R.id.spinner_product);

        updateProducts(outStock);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateProducts(outStock);
            }
        });

        if (outStock != null) {
            if (outStock.getPerUnit() != null) {
                edtPerInUnit.setText(outStock.getPerUnit());
            }
            if (outStock.getProUnit() != null) {
                edtNumberOfUnit.setText(outStock.getProUnit());
            }
            if (outStock.getpInQty() != null) {
                txtQuantity.setText("Quantity : " + outStock.getpInQty() + " " + product.getUnit());
            }
            if (outStock.getRemark() != null) {
                edtRemark.setText(outStock.getRemark());
            }

        }

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
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                    }
                } else {
                    Toast.makeText(activity, "Please Select Product", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
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
                    Toast.makeText(activity, "Please Select Product", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
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

                String indId = indent.getIndId();
                String createdBy = indent.getcBy();
                String date = indent.getcDate();
                String challan = indent.getcNo();

                if (product != null) {
                    if (!TextUtils.isEmpty(numberOfUnit)) {
                        if (!TextUtils.isEmpty(perInUnit)) {
                            if (!TextUtils.isEmpty(indId)) {
                                OutStock outStock = new OutStock();
                                outStock.setProd(product);
                                outStock.setProUnit(numberOfUnit);
                                outStock.setPerUnit(perInUnit);
                                outStock.setpInQty(quantity);
                                outStock.setRemark(remark);
                                outStock.setcName(indent.getcName());
                                outStock.setIndNo(indId);
                                outStock.setcBy(createdBy);
                                outStock.setcDate(date);
                                outStock.setcNo(challan);
                                outStocks.set(pos, outStock);
                                notifyDataSetChanged();
                                ((UpdateIndentFragment) ((IndentActivity) activity).getSupportFragmentManager().findFragmentByTag(AppConstants.FRAGMENT_DETAIL_INDENT)).update();
                                dialog.cancel();
                            } else {
                                Toast.makeText(activity, "Please Enter Indent Number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Enter per in unit ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Enter number Of unit ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Select Product", Toast.LENGTH_SHORT).show();
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

    private void updateProducts(OutStock outStock) {
        String str_search = edtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            products.clear();
            products.addAll(getSearchProducts(str_search));
            Product prodUnit = new Product();
            prodUnit.setpName("Select Product");
            products.add(0, prodUnit);
            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(activity, R.layout.layout_spinner_row, products);
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
            if (getHelper().getProducts(activity) != null && getHelper().getProducts(activity).size() > 0) {
                products.clear();
                products.addAll(getHelper().getProducts(activity));
                Product prodUnit = new Product();
                prodUnit.setpName("Select Product");
                products.add(0, prodUnit);
                ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(activity, R.layout.layout_spinner_row, products);
                adapter.setDropDownViewResource(R.layout.layout_spinner_row);
                spinnerProduct.setAdapter(adapter);

                if (outStock.getProd().getpName() != null && products != null) {
                    int index = 0;
                    for (int i = 0; i < products.size(); i++) {
                        Product product = products.get(i);
                        if (product != null && product.getpName().equals(outStock.getProd().getpName())) {
                            index = i;
                            break;
                        }
                    }
                    spinnerProduct.setSelection(index);
                    product = (Product) spinnerProduct.getSelectedItem();
                } else {
                    spinnerProduct.setSelection(0);
                    product = (Product) spinnerProduct.getSelectedItem();
                }

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
                Toast.makeText(activity, "Please Add Products", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upadteQty(float quantity) {
        if (quantity > 0) {
            final_qty = quantity;
        }
    }

    private List<Product> getSearchProducts(String key) {
        List<Product> products = new ArrayList<>();
        if (getHelper().getProducts(activity) != null && getHelper().getProducts(activity).size() > 0) {
            for (Product product : getHelper().getProducts(activity)) {
                if (product.getpName().toLowerCase().startsWith(key.toLowerCase()))
                    products.add(product);
            }
        }
        return products;
    }
}
