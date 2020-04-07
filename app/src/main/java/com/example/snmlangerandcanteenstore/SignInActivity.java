
package com.example.snmlangerandcanteenstore;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.login.SignInFragment;
import com.example.snmlangerandcanteenstore.fragment.login.UserListFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;


public class SignInActivity extends AppCompatActivity implements HelperInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.SCREEN)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_sign_in);

        if(getHelper().isLogin(this)){
            userList();
        }else {
            signIn();
        }
    }

    public void userList() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = UserListFragment.newInstance();
        fragmentTransaction.add(R.id.frame_sign_in, fragment, AppConstants.FRAGMENT_USER_LIST);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_USER_LIST);
        fragmentTransaction.commit();
    }

    public void signIn() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = SignInFragment.newInstance();
        fragmentTransaction.add(R.id.frame_sign_in, fragment, AppConstants.FRAGMENT_SIGN_IN);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_SIGN_IN);
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

