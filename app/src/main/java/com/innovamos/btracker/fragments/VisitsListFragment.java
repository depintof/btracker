package com.innovamos.btracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.innovamos.btracker.R;
import com.innovamos.btracker.adapter.CustomerVisitsArrayAdapter;
import com.innovamos.btracker.adapter.PurchasedProductsArrayAdapter;
import com.innovamos.btracker.dto.PurchasesDTO;
import com.innovamos.btracker.dto.VisitsDTO;

import java.util.Arrays;

/**
 * Fragmento con la lista de visitas realizadas por el usuario
 */
public class VisitsListFragment extends Fragment implements AbsListView.OnItemClickListener{

    // Lista de visitas realizadas por el usuario, recibida desde la actividad principal
    private static VisitsDTO[] customerVisitsList;

    // Lista visual
    ListView visitsListView;

    public static VisitsListFragment newInstance(VisitsDTO[] customerVisitsList) {
        VisitsListFragment fragment = new VisitsListFragment();
        VisitsListFragment.customerVisitsList = customerVisitsList;
        return fragment;
    }

    public VisitsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitslist_list, container, false);

        visitsListView = (ListView)view.findViewById(R.id.visitslist);
        if(customerVisitsList!=null){
            ArrayAdapter customerVisitsAdapter = new CustomerVisitsArrayAdapter(getContext(), Arrays.asList(customerVisitsList));
            visitsListView.setAdapter(customerVisitsAdapter);
        }
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
