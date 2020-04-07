

package com.example.snmlangerandcanteenstore.fragment.canteen;

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
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.CanteenAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.vendor.AddVendorFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Canteen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CanteenListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddCanteen;
    private RecyclerView rcyCanteen;
    private DatabaseReference reference;
    private CanteenAdapter canteenAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new CanteenListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canteen_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddCanteen = view.findViewById(R.id.btn_add_canteen);
        btnAddCanteen.setOnClickListener(this);

        rcyCanteen = view.findViewById(R.id.rcy_canteen);
        rcyCanteen.setLayoutManager(new LinearLayoutManager(getActivity()));
        canteenAdapter = new CanteenAdapter(getActivity());
        rcyCanteen.setAdapter(canteenAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_CANTEEN);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Canteen> canteens = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Canteen canteen = postSnapshot.getValue(Canteen.class);
                        canteens.add(canteen);
                    }

                    if(canteens!=null){
                        canteenAdapter.swap(canteens);
                    }else {
                        canteenAdapter.swap(new ArrayList<Canteen>());
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
            case R.id.btn_add_canteen:
                addCanteen();
                break;
            case R.id.img_back:
                ((CanteenActivity) getActivity()).onBack();
                break;
        }
    }

    public void addCanteen() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddCanteenFragment.newInstance();
        fragmentTransaction.add(R.id.frame_canteen, fragment, AppConstants.FRAGMENT_ADD_CANTEEN);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_CANTEEN);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
