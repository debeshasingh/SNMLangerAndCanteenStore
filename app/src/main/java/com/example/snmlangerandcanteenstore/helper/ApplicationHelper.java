package com.example.snmlangerandcanteenstore.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.snmlangerandcanteenstore.constant.ParamConstants;
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
import com.example.snmlangerandcanteenstore.util.PreferenceUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationHelper {
    private static final Object _lock = new Object();
    private DatabaseReference reference;
    private static String TAG = "debesh";
    private static ApplicationHelper _instance;
    private User user;


    public synchronized static ApplicationHelper getInstance() {
        if (_instance == null) {
            _instance = new ApplicationHelper();
            Log.d(TAG, "instance of ActivityHelper created.");
        }
        return _instance;
    }

    public User getUser(Context context) {
        if (_instance.user == null) {
            _instance.user = PreferenceUtil.getUser(context);
        }
        return _instance.user;
    }


    public void setUser(Context context, User user) {
        PreferenceUtil.setUser(context, user);
        synchronized (_lock) {
            _instance.user = user;
        }
    }

    public DatabaseReference databaseReference(Context context) {
        if (_instance.reference == null) {
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return _instance.reference;
    }

    public Boolean isLogin(Context context) {
        return PreferenceUtil.getLogin(context);
    }

    public void setLogin(Context context, Boolean login) {
        PreferenceUtil.setLogin(context, login);
    }


    public static List<ProdUnit> getProdUnit(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_UNITS, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<ProdUnit>>() {
                }.getType();
                List<ProdUnit> prodUnits = new Gson().fromJson(json, type);
                if (prodUnits != null && prodUnits.size() > 0)
                    return prodUnits;
            }
        }
        return null;
    }

    public static void setProdUnit(Context context, List<ProdUnit> prodUnits) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(prodUnits);
        preferences.edit().putString(ParamConstants.PARAM_UNITS, encrypt(json)).commit();

    }

    public static List<Category> getCategory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_CATEGORY, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Category>>() {
                }.getType();
                List<Category> prodUnits = new Gson().fromJson(json, type);
                if (prodUnits != null && prodUnits.size() > 0)
                    return prodUnits;
            }
        }
        return null;
    }

    public static void setCategory(Context context, List<Category> prodUnits) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(prodUnits);
        preferences.edit().putString(ParamConstants.PARAM_CATEGORY, encrypt(json)).commit();

    }

    public static List<Product> getProducts(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_PROUCTS, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Product>>() {
                }.getType();
                List<Product> products = new Gson().fromJson(json, type);
                if (products != null && products.size() > 0)
                    return products;
            }
        }
        return null;
    }

    public static void setProducts(Context context, List<Product> products) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(products);
        preferences.edit().putString(ParamConstants.PARAM_PROUCTS, encrypt(json)).commit();

    }


    public static List<Vendor> getVendors(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_VENDORS, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Vendor>>() {
                }.getType();
                List<Vendor> vendors = new Gson().fromJson(json, type);
                if (vendors != null && vendors.size() > 0)
                    return vendors;
            }
        }
        return null;
    }

    public static void setVendors(Context context, List<Vendor> vendors) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(vendors);
        preferences.edit().putString(ParamConstants.PARAM_VENDORS, encrypt(json)).commit();

    }


    public static List<Canteen> getCanteens(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_CANTEENS, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Canteen>>() {
                }.getType();
                List<Canteen> canteens = new Gson().fromJson(json, type);
                if (canteens != null && canteens.size() > 0)
                    return canteens;
            }
        }
        return null;
    }

    public static void setCanteens(Context context, List<Canteen> canteens) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(canteens);
        preferences.edit().putString(ParamConstants.PARAM_CANTEENS, encrypt(json)).commit();

    }

    public static List<Mrn> getMrns(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_MRNS, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Mrn>>() {
                }.getType();
                List<Mrn> mrns = new Gson().fromJson(json, type);
                if (mrns != null && mrns.size() > 0)
                    return mrns;
            }
        }
        return null;
    }

    public static void setMrns(Context context, List<Mrn> mrns) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(mrns);
        preferences.edit().putString(ParamConstants.PARAM_MRNS, encrypt(json)).commit();

    }

    public static List<Indent> getIndents(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_INDENT, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<Indent>>() {
                }.getType();
                List<Indent> indents = new Gson().fromJson(json, type);
                if (indents != null && indents.size() > 0)
                    return indents;
            }
        }
        return null;
    }

    public static void setIndents(Context context, List<Indent> indents) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(indents);
        preferences.edit().putString(ParamConstants.PARAM_INDENT, encrypt(json)).commit();

    }

    public static List<InStock> getInStock(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_INSTOCK, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<InStock>>() {
                }.getType();
                List<InStock> inStocks = new Gson().fromJson(json, type);
                if (inStocks != null && inStocks.size() > 0)
                    return inStocks;
            }
        }
        return null;
    }

    public static void setInStock(Context context, List<InStock> inStocks) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(inStocks);
        preferences.edit().putString(ParamConstants.PARAM_INSTOCK, encrypt(json)).commit();

    }

    public static List<OutStock> getOutStock(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json_encrypted = preferences.getString(ParamConstants.PARAM_OUTSTOCK, null);
        if (json_encrypted != null && json_encrypted.trim().length() > 0) {
            String json = decrypt(json_encrypted);
            if (json != null && json.trim().length() > 0) {
                Type type = new TypeToken<List<OutStock>>() {
                }.getType();
                List<OutStock> outStocks = new Gson().fromJson(json, type);
                if (outStocks != null && outStocks.size() > 0)
                    return outStocks;
            }
        }
        return null;
    }

    public static void setOutStock(Context context, List<OutStock> outStocks) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = new Gson().toJson(outStocks);
        preferences.edit().putString(ParamConstants.PARAM_OUTSTOCK, encrypt(json)).commit();

    }


    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
