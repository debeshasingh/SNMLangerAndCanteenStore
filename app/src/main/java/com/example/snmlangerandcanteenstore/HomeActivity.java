package com.example.snmlangerandcanteenstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.example.snmlangerandcanteenstore.model.Category;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.example.snmlangerandcanteenstore.model.Mrn;
import com.example.snmlangerandcanteenstore.model.OutStock;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.example.snmlangerandcanteenstore.model.Product;
import com.example.snmlangerandcanteenstore.model.User;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, HelperInterface, SwipeRefreshLayout.OnRefreshListener {
    private NavigationView navigationView;
    private TextView txtUserName, txtUserEmail;
    private DatabaseReference reference;
    private SwipeRefreshLayout refreshLayout;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SNM");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        refreshLayout = findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.color_accent);

        reference = getHelper().databaseReference(this);

        navigationView = findViewById(R.id.nav_view_end);
        navigationView.setNavigationItemSelectedListener(this);
        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txt_user_name);
        txtUserEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_user_email);

        if (getHelper().isLogin(this) && getHelper().getUser(this) != null) {
            user = getHelper().getUser(this);
            txtUserName.setText(user.getFname() + " " + user.getLname());
            txtUserEmail.setText(user.getEmail());
        }
        upadtView();

        updateProdUnits();
        updateCategory();
        updateProducts();
        updateVendors();
        updateCanteens();
        updateMrns();
        updateIndents();

    }

    private void upadtView() {
        navigationView.getMenu().findItem(R.id.nav_user).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_vendor).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_canteen).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_product).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_unit).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_mrn).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_indent).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_category).setVisible(false);

        if (user!=null && user.getType().equals("Admin")) {
            navigationView.getMenu().findItem(R.id.nav_user).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_vendor).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_canteen).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_product).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_unit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_mrn).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_indent).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_category).setVisible(true);
        } else if (user!=null && user.getType().equals("Store")) {
            navigationView.getMenu().findItem(R.id.nav_vendor).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_canteen).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_product).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_unit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_mrn).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_indent).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_category).setVisible(true);
        } else if (user!=null && user.getType().equals("Account")) {
            navigationView.getMenu().findItem(R.id.nav_vendor).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_canteen).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_product).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_unit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_mrn).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_indent).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_category).setVisible(true);
        }

    }

    private void updateProducts() {
        reference = getHelper().databaseReference(this).child(AppConstants.REF_PRODUCT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Product> products = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Product vendor = postSnapshot.getValue(Product.class);
                            products.add(vendor);
                        }
                        if (products.size() > 0) {
                            Log.d("debesh", "products Result: " + new Gson().toJson(products));
                            getHelper().setProducts(HomeActivity.this, products);
                        } else {
                            getHelper().setProducts(HomeActivity.this, null);
                            Log.d("debesh", "products Result: " + new Gson().toJson(products));
                        }
                    }else {
                        getHelper().setProducts(HomeActivity.this, null);
                        Log.d("debesh", "products Result: " + new Gson().toJson(products));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void updateCategory() {
        reference = getHelper().databaseReference(this).child(AppConstants.REF_CATEGORY);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Category> categories = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Category category = postSnapshot.getValue(Category.class);
                            categories.add(category);
                        }

                        if (categories.size() > 0) {
                            Log.d("debesh", "category Result: " + new Gson().toJson(categories));
                            getHelper().setCategory(HomeActivity.this, categories);
                        } else {
                            getHelper().setCategory(HomeActivity.this, null);
                            Log.d("debesh", "category Result: " + new Gson().toJson(categories));
                        }
                    }else {
                        getHelper().setCategory(HomeActivity.this, null);
                        Log.d("debesh", "category Result: " + new Gson().toJson(categories));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void updateVendors() {
        reference = getHelper().databaseReference(this).child(AppConstants.REF_VENDOR);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Vendor> vendors = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Vendor vendor = postSnapshot.getValue(Vendor.class);
                            vendors.add(vendor);
                        }

                        if (vendors != null) {
                            Log.d("debesh", "vendors Result: " + new Gson().toJson(vendors));
                            getHelper().setVendors(HomeActivity.this, vendors);
                        } else {
                            getHelper().setVendors(HomeActivity.this, null);
                            Log.d("debesh", "vendors Result: " + new Gson().toJson(vendors));
                        }
                    }else {
                        getHelper().setVendors(HomeActivity.this, null);
                        Log.d("debesh", "vendors Result: " + new Gson().toJson(vendors));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateCanteens() {
        reference = getHelper().databaseReference(this).child(AppConstants.REF_CANTEEN);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Canteen> canteens = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Canteen canteen = postSnapshot.getValue(Canteen.class);
                            canteens.add(canteen);
                        }

                        if (canteens != null) {
                            Log.d("debesh", "canteens Result: " + new Gson().toJson(canteens));
                            getHelper().setCanteens(HomeActivity.this, canteens);
                        } else {
                            getHelper().setCanteens(HomeActivity.this, null);
                            Log.d("debesh", "canteens Result: " + new Gson().toJson(canteens));
                        }
                    }else {
                        getHelper().setCanteens(HomeActivity.this, null);
                        Log.d("debesh", "canteens Result: " + new Gson().toJson(canteens));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateProdUnits() {
        reference = getHelper().databaseReference(this).child(AppConstants.REF_UNIT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ProdUnit> units = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ProdUnit unit = postSnapshot.getValue(ProdUnit.class);
                            units.add(unit);
                        }

                        if (units != null) {
                            Log.d("debesh", "units Result: " + new Gson().toJson(units));
                            getHelper().setProdUnit(HomeActivity.this, units);
                        } else {
                            getHelper().setProdUnit(HomeActivity.this, null);
                            Log.d("debesh", "units Result: " + new Gson().toJson(units));
                        }
                    }else {
                        getHelper().setProdUnit(HomeActivity.this, null);
                        Log.d("debesh", "units Result: " + new Gson().toJson(units));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void updateMrns() {
        reference = getHelper().databaseReference(HomeActivity.this).child(AppConstants.REF_MRN);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Mrn> mrns = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Mrn mrn = postSnapshot.getValue(Mrn.class);
                            mrns.add(mrn);
                        }

                        if (mrns != null) {
                            Log.d("debesh", "Mrn Result: " + new Gson().toJson(mrns));
                            getHelper().setMrns(HomeActivity.this, mrns);
                        } else {
                            getHelper().setMrns(HomeActivity.this, null);
                            Log.d("debesh", "Mrn Result: " + new Gson().toJson(mrns));
                        }
                    }else {
                        getHelper().setMrns(HomeActivity.this, null);
                        Log.d("debesh", "Mrn Result: " + new Gson().toJson(mrns));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        updateStock();
    }

    private void updateIndents() {
        reference = getHelper().databaseReference(HomeActivity.this).child(AppConstants.REF_INDENT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Indent> indents = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Indent indent = postSnapshot.getValue(Indent.class);
                            indents.add(indent);
                        }

                        if (indents != null) {
                            Log.d("debesh", "Indent Result: " + new Gson().toJson(indents));
                            getHelper().setIndents(HomeActivity.this, indents);
                        } else {
                            getHelper().setIndents(HomeActivity.this, null);
                            Log.d("debesh", "Indent Result: " + new Gson().toJson(indents));
                        }
                    }else {
                        getHelper().setIndents(HomeActivity.this, null);
                        Log.d("debesh", "Indent Result: " + new Gson().toJson(indents));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        updateStock();
    }

    private void updateStock() {
        if (getHelper().getMrns(HomeActivity.this) != null) {

            List<InStock> finalInstock = new ArrayList<>();
            List<InStock> inStocks = new ArrayList<>();
            for (Mrn mrn : getHelper().getMrns(HomeActivity.this)) {
                if (mrn != null && mrn.getInStocks() != null && mrn.getInStocks().size() > 0) {
                    inStocks.clear();
                    inStocks.addAll(mrn.getInStocks());
                }
                finalInstock.addAll(inStocks);
            }

            if(finalInstock.size()>0){
                Log.d("Debesh", "inStocks : "+new Gson().toJson(finalInstock));
                getHelper().setInStock(HomeActivity.this,finalInstock);
            }else {
                getHelper().setInStock(HomeActivity.this,null);
                Toast.makeText(this, "No MRN", Toast.LENGTH_SHORT).show();
            }
        }else {
            getHelper().setInStock(HomeActivity.this,null);
            Toast.makeText(this, "No MRN", Toast.LENGTH_SHORT).show();
        }
        if (getHelper().getIndents(HomeActivity.this) != null) {
            List<OutStock> finalOutStocks = new ArrayList<>();
            for (Indent indent : getHelper().getIndents(HomeActivity.this)) {
                List<OutStock> outStocks = new ArrayList<>();
                if (indent != null && indent.getoStocks() != null && indent.getoStocks().size() > 0) {
                    outStocks.clear();
                    outStocks.addAll(indent.getoStocks());
                }
                finalOutStocks.addAll(outStocks);
            }
            if(finalOutStocks.size()>0){
                Log.d("Debesh", "outStocks : "+new Gson().toJson(finalOutStocks));
                getHelper().setOutStock(HomeActivity.this,finalOutStocks);
            }else {
                Toast.makeText(this, "No Indent", Toast.LENGTH_SHORT).show();
                getHelper().setOutStock(HomeActivity.this,null);
            }
        }else {
            Toast.makeText(this, "No Indent", Toast.LENGTH_SHORT).show();
            getHelper().setOutStock(HomeActivity.this,null);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_user:
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.nav_vendor:
                startActivity(new Intent(this, VendorActivity.class));
                break;
            case R.id.nav_canteen:
                startActivity(new Intent(this, CanteenActivity.class));
                break;
            case R.id.nav_product:
                startActivity(new Intent(this, ProductActivity.class));
                break;
            case R.id.nav_unit:
                startActivity(new Intent(this, UnitActivity.class));
                break;
            case R.id.nav_category:
                startActivity(new Intent(this, CategoryActivity.class));
                break;
            case R.id.nav_mrn:
                startActivity(new Intent(this, MrnActivity.class));
                break;
            case R.id.nav_indent:
                startActivity(new Intent(this, IndentActivity.class));
                break;
            case R.id.nav_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.nav_report:
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.nav_logout:
                getHelper().getUser(null);
                getHelper().setLogin(this, false);
                Intent intent = new Intent(this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    public void onRefresh() {
        updateProdUnits();
        updateCategory();
        updateProducts();
        updateVendors();
        updateCanteens();
        updateMrns();
        updateIndents();
        refreshLayout.setRefreshing(false);
    }
}
