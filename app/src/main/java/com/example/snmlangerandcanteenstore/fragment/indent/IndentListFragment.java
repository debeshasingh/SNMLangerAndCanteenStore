

package com.example.snmlangerandcanteenstore.fragment.indent;

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

import com.example.snmlangerandcanteenstore.IndentActivity;
import com.example.snmlangerandcanteenstore.MrnActivity;
import com.example.snmlangerandcanteenstore.R;
import com.example.snmlangerandcanteenstore.adapter.IndentAdapter;
import com.example.snmlangerandcanteenstore.constant.AppConstants;
import com.example.snmlangerandcanteenstore.constant.HelperInterface;
import com.example.snmlangerandcanteenstore.fragment.mrn.AddMRNFragment;
import com.example.snmlangerandcanteenstore.helper.ApplicationHelper;
import com.example.snmlangerandcanteenstore.model.Indent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class IndentListFragment extends Fragment implements View.OnClickListener, HelperInterface {

    private Button btnAddIndent;
    private RecyclerView rcyIndent;
    private DatabaseReference reference;
    private IndentAdapter indentAdapter;
    private ImageView imgBack;

    public static Fragment newInstance() {
        return new IndentListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indent_list, container, false);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnAddIndent = view.findViewById(R.id.btn_add_indent);
        btnAddIndent.setOnClickListener(this);

        rcyIndent = view.findViewById(R.id.rcy_indent);
        rcyIndent.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentAdapter = new IndentAdapter(getActivity());
        rcyIndent.setAdapter(indentAdapter);

        reference = getHelper().databaseReference(getActivity());

        reference = getHelper().databaseReference(getActivity()).child(AppConstants.REF_INDENT);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Indent> indents = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Indent indent = postSnapshot.getValue(Indent.class);
                        indents.add(indent);
                    }

                    if(indents!=null){
                        indentAdapter.swap(indents);
                    }else {
                        indentAdapter.swap(new ArrayList<Indent>());
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
            case R.id.btn_add_indent:
                addIndent();
                break;
            case R.id.img_back:
                ((IndentActivity) getActivity()).onBack();
                break;
        }
    }

    public void addIndent() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = AddIndentFragment.newInstance();
        fragmentTransaction.add(R.id.frame_indent, fragment, AppConstants.FRAGMENT_ADD_INDENT);
        fragmentTransaction.addToBackStack(AppConstants.FRAGMENT_ADD_INDENT);
        fragmentTransaction.commit();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

}
