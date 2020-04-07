

package com.example.snmlangerandcanteenstore.fragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snmlangerandcanteenstore.CategoryActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.CategoryAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategoryListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddCategory;
    private RecyclerView rcyCategory;
    private DatabaseReference reference;
    private CategoryAdapter categoryAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddCategory = view.findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(this);

        rcyCategory = view.findViewById(R.id.rcy_category);
        rcyCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new CategoryAdapter(getActivity());
        rcyCategory.setAdapter(categoryAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_CATEGORY);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Category> categories = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Category unit = postSnapshot.getValue(Category.class);
                        categories.add(unit);
                    }

                    if(categories.size()>0){
                        categoryAdapter.swap(categories);
                    }else {
                        categoryAdapter.swap(new ArrayList<Category>());
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

    public void addCategory() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddCategoryFragment.newInstance();
        fragmentTransaction.add(R.id.frame_category, fragment, AppConstants.FRAGMENT_ADD_CATEGORY);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_CATEGORY);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
