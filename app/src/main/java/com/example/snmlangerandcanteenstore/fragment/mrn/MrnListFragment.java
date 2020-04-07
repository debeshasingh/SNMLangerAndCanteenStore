

package com.example.snmlangerandcanteenstore.fragment.mrn;

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

import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.MrnAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.InStock;
import com.example.snmlangerandcanteenstore.model.Mrn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MrnListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddMrn;
    private RecyclerView rcyMrn;
    private DatabaseReference reference;
    private MrnAdapter mrnAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new MrnListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mrn_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddMrn = view.findViewById(R.id.btn_add_mrn);
        btnAddMrn.setOnClickListener(this);

        rcyMrn = view.findViewById(R.id.rcy_mrn);
        rcyMrn.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrnAdapter = new MrnAdapter(getActivity());
        rcyMrn.setAdapter(mrnAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_MRN);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Mrn> mrns = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Mrn mrn = postSnapshot.getValue(Mrn.class);
                        mrns.add(mrn);
                    }

                    if(mrns!=null){
                        mrnAdapter.swap(mrns);
                    }else {
                        mrnAdapter.swap(new ArrayList<Mrn>());
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
            case R.id.btn_add_mrn:
                addMrn();
                break;
            case R.id.img_back:
                ((MrnActivity) getActivity()).onBack();
                break;
        }
    }

    public void addMrn() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddMRNFragment.newInstance();
        fragmentTransaction.add(R.id.frame_mrn, fragment, AppConstants.FRAGMENT_ADD_MRN);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_MRN);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
