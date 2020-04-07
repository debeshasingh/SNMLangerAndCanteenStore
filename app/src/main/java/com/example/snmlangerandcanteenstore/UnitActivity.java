
package com.example.snmlangerandcanteenstore;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.canteen.CanteenListFragment;
import com.example.snmlangerandcanteenstore.fragment.unit.UnitListFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;


public class UnitActivity extends AppCompatActivity implements HelperInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.SCREEN)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_unit);
        vendorList();
    }

    public void vendorList() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = UnitListFragment.newInstance();
        fragmentTransaction.add(R.id.frame_unit, fragment, AppConstants.FRAGMENT_UNIT_LIST);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_UNIT_LIST);
        fragmentTransaction.commit();
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
}

