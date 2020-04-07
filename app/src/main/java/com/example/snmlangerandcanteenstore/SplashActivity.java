package com.example.snmlangerandcanteenstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity implements HelperInterface {

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        reference = getHelper().databaseReference(this);
        if(getHelper().isLogin(this)){
            startActivity(new Intent(SplashActivity.this,HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this,SignInActivity.class));
            finish();
        }

    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}
