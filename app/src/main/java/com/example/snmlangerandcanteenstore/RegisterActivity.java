
package com.example.snmlangerandcanteenstore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.adapter.RegisterInstockAdapter;
import com.example.snmlangerandcanteenstore.adapter.RegisterOutstockAdapter;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.OutStock;
import com.example.snmlangerandcanteenstore.model.Product;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements HelperInterface, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView imgBack;
    private RecyclerView rcyInward, rcyOutward;
    private RegisterInstockAdapter inwardAdapter;
    private RegisterOutstockAdapter outwardAdapter;
    private EditText edtSearch;
    private Spinner spinnerProduct;
    private Product product;
    private TextView txtFinalInward,txtFinalOutward,txtBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.SCREEN)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_register);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        edtSearch = findViewById(R.id.edt_search);
        spinnerProduct = findViewById(R.id.spinner_product);

        txtFinalInward = findViewById(R.id.txt_total_instock);
        txtFinalOutward = findViewById(R.id.txt_total_outstock);
        txtBalance = findViewById(R.id.txt_balance);

        rcyInward = findViewById(R.id.rcy_inward);
        rcyInward.setLayoutManager(new LinearLayoutManager(RegisterActivity.this));
        inwardAdapter = new RegisterInstockAdapter(RegisterActivity.this);
        rcyInward.setAdapter(inwardAdapter);

        rcyOutward = findViewById(R.id.rcy_outward);
        rcyOutward.setLayoutManager(new LinearLayoutManager(RegisterActivity.this));
        outwardAdapter = new RegisterOutstockAdapter(RegisterActivity.this);
        rcyOutward.setAdapter(outwardAdapter);
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
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            onBack();
        }
    }

    private void updateProducts() {
        String str_search = edtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(str_search)) {
            List<Product> products = new ArrayList<>();
            products.clear();
            products.addAll(getSearchProducts(str_search));
            Product pro = new Product();
            pro.setpName("Select Product");
            products.add(0, pro);
            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(RegisterActivity.this, R.layout.layout_spinner_row, products);
            adapter.setDropDownViewResource(R.layout.layout_spinner_row);
            spinnerProduct.setAdapter(adapter);
            spinnerProduct.setOnItemSelectedListener(this);
        } else {
            if (getHelper().getProducts(RegisterActivity.this) != null && getHelper().getProducts(RegisterActivity.this).size() > 0) {
                List<Product> products = new ArrayList<>();
                products.clear();
                products.addAll(getHelper().getProducts(RegisterActivity.this));
                Product pro = new Product();
                pro.setpName("Select Product");
                products.add(0, pro);
                ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(RegisterActivity.this, R.layout.layout_spinner_row, products);
                adapter.setDropDownViewResource(R.layout.layout_spinner_row);
                spinnerProduct.setAdapter(adapter);
                spinnerProduct.setOnItemSelectedListener(this);
            } else {
                getHelper().setProducts(RegisterActivity.this,null);
                Toast.makeText(RegisterActivity.this, "Please Add Products", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Product> getSearchProducts(String key) {
        List<Product> products = new ArrayList<>();
        if (getHelper().getProducts(RegisterActivity.this) != null && getHelper().getProducts(RegisterActivity.this).size() > 0) {
            for (Product product : getHelper().getProducts(RegisterActivity.this)) {
                if (product.getpName().toLowerCase().startsWith(key.toLowerCase()))
                    products.add(product);
            }
        }else {
            getHelper().setProducts(RegisterActivity.this,null);
        }
        return products;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        int idd = parent.getId();
        if (idd == R.id.spinner_product) {
            Object obj = spinnerProduct.getSelectedItem();
            if (obj instanceof Product && pos > 0) {
                product = (Product) obj;
                updateView(product);
                Log.d("Debesh", "product: " + new Gson().toJson(product));
            } else {
                product = null;
                updateView(product);
                Log.d("Debesh", "product: " + new Gson().toJson(product));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateView(Product product) {
        List<InStock> inStocks = new ArrayList<>();
        List<OutStock> outStocks = new ArrayList<>();
        if (product != null && product.getpId() != null) {
            if (getHelper().getInStock(RegisterActivity.this) != null) {
                for (InStock inStock : getHelper().getInStock(RegisterActivity.this)) {
                    if (inStock != null && inStock.getProd() != null && inStock.getProd().getpId().equals(product.getpId())) {
                        inStocks.add(inStock);
                    }
                }
            }

            if (getHelper().getOutStock(RegisterActivity.this) != null) {
                for (OutStock outStock : getHelper().getOutStock(RegisterActivity.this)) {
                    if (outStock != null && outStock.getProd() != null && outStock.getProd().getpId().equals(product.getpId())) {
                        outStocks.add(outStock);
                    }
                }
            }
            Log.d("Debesh", "inStocks: " + new Gson().toJson(inStocks));
            Log.d("Debesh", "outStocks: " + new Gson().toJson(outStocks));

            if (inStocks.size() > 0) {
                inwardAdapter.swap(inStocks);
                txtFinalInward.setText("Purchesed Qty : "+fInStockQuantity(inStocks));
            } else {
                txtFinalInward.setText("Purchesed Qty : "+fInStockQuantity(inStocks));
                inwardAdapter.swap(new ArrayList<InStock>());
            }

            if (outStocks.size() > 0) {
                txtFinalOutward.setText("Issued Qty : "+fOutStockQuantity(outStocks));
                outwardAdapter.swap(outStocks);
            } else {
                txtFinalOutward.setText("Issued Qty : "+fOutStockQuantity(outStocks));
                outwardAdapter.swap(new ArrayList<OutStock>());
            }

            txtBalance.setText("Balance : "+ (fInStockQuantity(inStocks) - fOutStockQuantity(outStocks)));

        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            inwardAdapter.swap(new ArrayList<InStock>());
            outwardAdapter.swap(new ArrayList<OutStock>());
            txtFinalInward.setText("Purchesed Qty : ");
            txtFinalOutward.setText("Issued Qty : ");
            txtBalance.setText("Balance : ");
        }
    }

    private float fInStockQuantity(List<InStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }

    private float fOutStockQuantity(List<OutStock> items) {

        float totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) {
            totalQuantity += Float.valueOf(items.get(i).getpInQty());
        }

        return totalQuantity;
    }
}

