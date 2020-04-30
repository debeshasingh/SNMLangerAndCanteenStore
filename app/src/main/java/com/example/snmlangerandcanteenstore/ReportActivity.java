
package com.example.snmlangerandcanteenstore;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.adapter.ReportCatAdapter;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.CatList;
import com.example.snmlangerandcanteenstore.model.Category;
import com.example.snmlangerandcanteenstore.model.Product;

import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends AppCompatActivity implements HelperInterface, View.OnClickListener {

    private ImageView imgBack;
    private RecyclerView rcyCategory;
    private ReportCatAdapter catAdapter;
    private List<CatList> catLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.SCREEN)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_report);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        rcyCategory = findViewById(R.id.rcy_category);
        rcyCategory.setLayoutManager(new LinearLayoutManager(ReportActivity.this));
        catAdapter = new ReportCatAdapter(this);
        rcyCategory.setAdapter(catAdapter);
        UpdateList();
    }

    public void UpdateList() {
        if (getHelper().getCategory(ReportActivity.this) != null && getHelper().getCategory(ReportActivity.this).size() > 0) {
            for (Category category : getHelper().getCategory(ReportActivity.this)) {
                CatList catList = new CatList();
                catList.setCatId(category.getCatId());
                catList.setCatName(category.getCatName());
                if (getHelper().getProducts(ReportActivity.this) != null && getHelper().getProducts(ReportActivity.this).size() > 0) {
                    List<Product> products = new ArrayList<>();
                    for (Product product : getHelper().getProducts(ReportActivity.this)) {
                        if (product != null && product.getCat() != null && product.getCat().getCatId().equals(category.getCatId())) {
                            products.add(product);
                        }
                    }
                    catList.setProducts(products);
                }
                catLists.add(catList);
            }


            if(catLists.size()>0){
                catAdapter.swap(catLists);
            }else {
                catAdapter.swap(new ArrayList<CatList>());
            }

        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            onBack();
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}

