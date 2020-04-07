

package com.example.snmlangerandcanteenstore.fragment.vendor;

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

import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.VendorActivity;
import com.example.snmlangerandcanteenstore.adapter.VendorAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Vendor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class VendorListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddVendor;
    private RecyclerView rcyVendor;
    private DatabaseReference reference;
    private VendorAdapter vendorAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new VendorListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddVendor = view.findViewById(R.id.btn_add_vendor);
        btnAddVendor.setOnClickListener(this);

        rcyVendor = view.findViewById(R.id.rcy_vendor);
        rcyVendor.setLayoutManager(new LinearLayoutManager(getActivity()));
        vendorAdapter = new VendorAdapter(getActivity());
        rcyVendor.setAdapter(vendorAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_VENDOR);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Vendor> vendors = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Vendor vendor = postSnapshot.getValue(Vendor.class);
                        vendors.add(vendor);
                    }

                    if(vendors!=null){
                        vendorAdapter.swap(vendors);
                    }else {
                        vendorAdapter.swap(new ArrayList<Vendor>());
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
            case R.id.btn_add_vendor:
                addVendor();
                break;
            case R.id.img_back:
                ((VendorActivity) getActivity()).onBack();
                break;
        }
    }

    public void addVendor() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddVendorFragment.newInstance();
        fragmentTransaction.add(R.id.frame_vendor, fragment, AppConstants.FRAGMENT_ADD_VENDOR);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_VENDOR);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
