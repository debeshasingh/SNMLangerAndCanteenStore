

package com.example.snmlangerandcanteenstore.fragment.product;

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

import com.example.snmlangerandcanteenstore.CanteenActivity;
import com.example.snmlangerandcanteenstore.ProductActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.CanteenAdapter;
import com.example.snmlangerandcanteenstore.adapter.ProductAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.canteen.AddCanteenFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.example.snmlangerandcanteenstore.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddProduct;
    private RecyclerView rcyProduct;
    private DatabaseReference reference;
    private ProductAdapter productAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(this);

        rcyProduct = view.findViewById(R.id.rcy_product);
        rcyProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        productAdapter = new ProductAdapter(getActivity());
        rcyProduct.setAdapter(productAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_PRODUCT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Product> products = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Product product = postSnapshot.getValue(Product.class);
                        products.add(product);
                    }

                    if(products!=null){
                        productAdapter.swap(products);
                    }else {
                        productAdapter.swap(new ArrayList<Product>());
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
            case R.id.btn_add_product:
                addProduct();
                break;
            case R.id.img_back:
                ((ProductActivity) getActivity()).onBack();
                break;
        }
    }

    public void addProduct() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddProductFragment.newInstance();
        fragmentTransaction.add(R.id.frame_product, fragment, AppConstants.FRAGMENT_ADD_PRODUCT);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_PRODUCT);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
