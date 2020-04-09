
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
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.mrn.UpdateMRNFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.Mrn;
import com.example.snmlangerandcanteenstore.model.Product;
import com.example.snmlangerandcanteenstore.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MrnUpdateProductAdapter extends RecyclerView.Adapter<MrnUpdateProductAdapter.CustomViewHolder> implements HelperInterface {

    public List<InStock> inStocks;
    private Activity activity;
    private Product product;
    private List<Product> products;
    private Mrn mrn_data;
    private float final_qty, final_amount;
    private EditText edtSearch;
    private Spinner spinnerProduct;
    private User user;

    public MrnUpdateProductAdapter(Activity activity) {
        this.inStocks = new ArrayList<>();
        this.activity = activity;
        this.product = new Product();
        this.products = new ArrayList<>();
        this.mrn_data = new Mrn();
        this.user = new User();
    }

    public void swap(List<InStock> inStockList, Mrn input_mrn) {
        inStocks.clear();
        inStocks.addAll(inStockList);
        String str_mrn = new Gson().toJson(input_mrn);
        mrn_data = new Gson().fromJson(str_mrn, Mrn.class);
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mrn_product_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final InStock inStock = inStocks.get(i);

        if (getHelper().isLogin(activity) && getHelper().getUser(activity) != null) {
            user = getHelper().getUser(activity);
        }

        if (user!=null && user.getType().equals("Admin") || user!=null && user.getType().equals("Account")) {
            customViewHolder.txtPerNumPrice.setVisibility(View.VISIBLE);
            customViewHolder.txtAmount.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.txtPerNumPrice.setVisibility(View.GONE);
            customViewHolder.txtAmount.setVisibility(View.GONE);
        }
        if (inStock != null) {
            if (inStock.getProd() != null && inStock.getProd().getpName() != null) {
                customViewHolder.txtProductName.setText("Product name : " + inStock.getProd().getpName() + " (" + inStock.getProd().getUnit() + ")");
            }
            if (inStock.getProUnit() != null) {
                customViewHolder.txtNumOfUnit.setText("No of Unit : " + inStock.getProUnit());
            }
            if (inStock.getPerUnit() != null) {
                customViewHolder.txtPerInUnit.setText("Per in Unit : " + inStock.getPerUnit());
            }
            if (inStock.getpInQty() != null) {
                customViewHolder.txtQuantity.setText("Quantity: " + inStock.getpInQty() + " (" + inStock.getProd().getUnit() + ")");
            }
            if (inStock.getPerUPrice() != null) {
                customViewHolder.txtPerNumPrice.setText("Per of Unit Price : ₹ " + inStock.getPerUPrice());
            }
            if (inStock.getPrice() != null) {
                customViewHolder.txtAmount.setText("Amount : ₹ " + inStock.getPrice());
            }

        }

        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null && user.getType().equals("Admin") || user!=null && user.getType().equals("Account")) {
                    addRowDialog(inStock, i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != inStocks ? inStocks.size() : 0);
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName, txtNumOfUnit, txtPerInUnit, txtQuantity, txtPerNumPrice, txtAmount;
        public LinearLayout cardView, layAccount;

        public CustomViewHolder(View view) {
            super(view);
            this.cardView = view.findViewById(R.id.card_view);
            this.txtProductName = view.findViewById(R.id.txt_product_name);
            this.txtNumOfUnit = view.findViewById(R.id.txt_num_of_unit);
            this.txtPerInUnit = view.findViewById(R.id.txt_per_in_unit);
            this.txtQuantity = view.findViewById(R.id.txt_quantity);
            this.layAccount = view.findViewById(R.id.layout_account);
            this.txtPerNumPrice = view.findViewById(R.id.txt_per_num_price);
            this.txtAmount = view.findViewById(R.id.txt_amount);
        }
    }

    private void addRowDialog(final InStock inStock, final int pos) {
        final AppCompatDialog dialog = new AppCompatDialog(activity, R.style.AppDialogThemeAccentNoTitle);
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

        if (user!=null && user.getType().equals("Admin") || user!=null && user.getType().equals("Account")) {
            layoutAccount.setVisibility(View.VISIBLE);
        } else {
            layoutAccount.setVisibility(View.GONE);
        }

        updateProduts(inStock);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateProduts(inStock);
            }
        });

        if (inStock != null) {
            if (inStock.getPerUnit() != null) {
                edtPerInUnit.setText(inStock.getPerUnit());
            }
            if (inStock.getProUnit() != null) {
                edtNumberOfUnit.setText(inStock.getProUnit());
            }
            if (inStock.getpInQty() != null) {
                txtQuantity.setText("Quantity : " + inStock.getpInQty() + " " + product.getUnit());
            }
            if (inStock.getPrice() != null) {
                txtAmount.setText("Amount : ₹ " + inStock.getPrice());
            }
            if (inStock.getPerUPrice() != null) {
                edtPerUnitPrice.setText(inStock.getPerUPrice());
            }
            if (inStock.getRemark() != null) {
                edtRemark.setText(inStock.getRemark());
            }
            if(inStock.getpInQty()!=null){
                final_qty = Float.valueOf(inStock.getpInQty());
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
                String price = edtPerUnitPrice.getText().toString().trim();
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());
                            if (!TextUtils.isEmpty(price)) {
                                txtAmount.setText("Amount : ₹ "+ (Float.valueOf(price) * quantity));
                                upadteAmount(Float.valueOf(price) * quantity);
                            } else {
                                txtAmount.setText("");
                            }
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                            txtAmount.setText("");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                        txtAmount.setText("");
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
                String price = edtPerUnitPrice.getText().toString().trim();
                if (product != null && product.getUnit() != null) {
                    if (number_of_unit.length() > 0 && per_in_unit.length() > 0) {
                        float quantity = (Float.valueOf(number_of_unit) * Float.valueOf(per_in_unit));
                        if (quantity > 0) {
                            upadteQty(quantity);
                            txtQuantity.setText("Quantity : " + quantity + product.getUnit());
                            if (!TextUtils.isEmpty(price)) {
                                txtAmount.setText("Amount : ₹ "+(Float.valueOf(price) * quantity));
                                upadteAmount(Float.valueOf(price) * quantity);
                            } else {
                                txtAmount.setText("");
                            }
                        } else {
                            txtQuantity.setText("Quantity : 0.0");
                            txtAmount.setText("");
                        }
                    } else {
                        txtQuantity.setText("Quantity : 0.0");
                        txtAmount.setText("");
                    }
                } else {
                    Toast.makeText(activity, "Please Select Product", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
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
                notifyDataSetChanged();
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

                String mrn = mrn_data.getMrnId();
                String createdBy = mrn_data.getcBy();
                String date = mrn_data.getcDate();

                if (product != null) {
                    if (!TextUtils.isEmpty(numberOfUnit)) {
                        if (!TextUtils.isEmpty(perInUnit)) {
                            if (!TextUtils.isEmpty(mrn)) {
                                InStock inStock = new InStock();
                                inStock.setProd(product);
                                inStock.setProUnit(numberOfUnit);
                                inStock.setPerUnit(perInUnit);
                                inStock.setpInQty(quantity);
                                inStock.setPrice(amount);
                                inStock.setPerUPrice(perUPrice);
                                inStock.setRemark(remark);
                                inStock.setVenName(mrn_data.getvName());
                                inStock.setMrnNo(mrn);
                                inStock.setcBy(createdBy);
                                inStock.setcDate(date);
                                inStocks.set(pos, inStock);
                                notifyDataSetChanged();
                                ((UpdateMRNFragment) ((MrnActivity) activity).getSupportFragmentManager().findFragmentByTag(AppConstants.FRAGMENT_DETAIL_MRN)).update();
                                dialog.cancel();
                            } else {
                                Toast.makeText(activity, "Please Enter MRN Number", Toast.LENGTH_SHORT).show();
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

    private void updateProduts(InStock inStock) {
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

                if (inStock.getProd().getpName() != null && products != null) {
                    int index = 0;
                    for (int i = 0; i < products.size(); i++) {
                        Product product = products.get(i);
                        if (product != null && product.getpName().equals(inStock.getProd().getpName())) {
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
