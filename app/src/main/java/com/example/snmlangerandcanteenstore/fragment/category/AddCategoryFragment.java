

package com.example.snmlangerandcanteenstore.fragment.category;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.snmlangerandcanteenstore.CategoryActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Category;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddCategoryFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private EditText edtName;
    private TextInputLayout inputName;
    private Button btnAddUnit;
    private DatabaseReference reference;
    private Long maxId = 0L;
    private ImageView imgBack;
    private List<Category> categories = new ArrayList<>();


    public static Fragment newInstance() {
        return new AddCategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddUnit = view.findViewById(R.id.btn_add_category);
        btnAddUnit.setOnClickListener(this);

        edtName = view.findViewById(R.id.edt_name);

        inputName = view.findViewById(R.id.input_name);

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_CATEGORY);

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
            case R.id.btn_add_category:
                addCategory();
                break;
            case R.id.img_back:
                ((CategoryActivity) getActivity()).onBack();
                break;
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    private void addCategory() {

        inputName.setErrorEnabled(false);

        String name = edtName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (!isCat(name)) {
                Category canteen = new Category();
                canteen.setCatName(name);
                canteen.setCatId(String.valueOf(maxId + 1));

                reference.child(String.valueOf(maxId)).setValue(canteen);
                Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                inputName.setErrorEnabled(true);
                Toast.makeText(getActivity(), "Category Name already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            inputName.setErrorEnabled(true);
            Toast.makeText(getActivity(), "Please Enter Category Name", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCat(String key) {
        if (getHelper().getCategory(getActivity()) != null && getHelper().getCategory(getActivity()).size() > 0) {
            for (Category category : getHelper().getCategory(getActivity())) {
                if (category.getCatName().toLowerCase().equals(key.toLowerCase()))
                    return true;
            }
        }
        return false;
    }

}

