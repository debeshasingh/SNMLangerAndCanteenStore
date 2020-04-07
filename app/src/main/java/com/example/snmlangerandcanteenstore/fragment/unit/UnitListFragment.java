

package com.example.snmlangerandcanteenstore.fragment.unit;

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
import com.example.snmlangerandcanteenstore.UnitActivity;
import com.example.snmlangerandcanteenstore.adapter.ProdUnitAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.ProdUnit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UnitListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddCanteen;
    private RecyclerView rcyUnit;
    private DatabaseReference reference;
    private ProdUnitAdapter prodUnitAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new UnitListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddCanteen = view.findViewById(R.id.btn_add_unit);
        btnAddCanteen.setOnClickListener(this);

        rcyUnit = view.findViewById(R.id.rcy_unit);
        rcyUnit.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodUnitAdapter = new ProdUnitAdapter(getActivity());
        rcyUnit.setAdapter(prodUnitAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_UNIT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ProdUnit> units = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        ProdUnit unit = postSnapshot.getValue(ProdUnit.class);
                        units.add(unit);
                    }

                    if(units!=null){
                        prodUnitAdapter.swap(units);
                    }else {
                        prodUnitAdapter.swap(new ArrayList<ProdUnit>());
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
            case R.id.btn_add_unit:
                addUnit();
                break;
            case R.id.img_back:
                ((UnitActivity) getActivity()).onBack();
                break;
        }
    }

    public void addUnit() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddProdUnitFragment.newInstance();
        fragmentTransaction.add(R.id.frame_unit, fragment, AppConstants.FRAGMENT_ADD_UNIT);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_UNIT);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
