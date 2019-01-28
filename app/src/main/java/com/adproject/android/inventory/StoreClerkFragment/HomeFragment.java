package com.adproject.android.inventory.StoreClerkFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.adproject.android.inventory.R;

public class HomeFragment extends Fragment {

    FragmentManager fm;
    private String content;
    public static final String TAG = "Home";
    private View view;

    public static HomeFragment newInstance(String content) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(TAG, content);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        content = bundle != null ? bundle.getString(TAG) : "";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.storeclerk_fragment_home, container, false);
        getActivity().setTitle("Home");
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
        Button btnInventory = getActivity().findViewById(R.id.btnInventory);
        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageInventoryFragment inventoryList = ManageInventoryFragment.newInstance("Inventory");
                fm.beginTransaction().replace(R.id.storeclerk_content_frame, inventoryList,"Inventory").addToBackStack("Inventory").commit();
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(1).setChecked(true);
            }
        });

    }

    private void init() {

    }
}
