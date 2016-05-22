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
import com.innovamos.btracker.adapter.LikedProductsArrayAdapter;
import com.innovamos.btracker.adapter.PurchasedProductsArrayAdapter;
import com.innovamos.btracker.dto.CustomerProductsDTO;
import com.innovamos.btracker.dto.PurchasesDTO;

import java.util.Arrays;

/**
 * Fragmento con la lista de productos comprados por el usuario
 */
public class PurchasedListFragment extends Fragment implements AbsListView.OnItemClickListener{

    // Lista de productos deseados, recibida desde la actividad principal
    private static PurchasesDTO[] purchasedProductsList;

    // Lista visual
    ListView purchasesListView;

    public static PurchasedListFragment newInstance(PurchasesDTO[] purchasedProductsList) {
        PurchasedListFragment fragment = new PurchasedListFragment();
        PurchasedListFragment.purchasedProductsList = purchasedProductsList;
        return fragment;
    }

    public PurchasedListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchaseslist_list, container, false);

        purchasesListView = (ListView)view.findViewById(R.id.purchaseslist);
        if(purchasedProductsList!=null){
            ArrayAdapter purchasedProductsAdapter = new PurchasedProductsArrayAdapter<>(getContext(), Arrays.asList(purchasedProductsList));
            purchasesListView.setAdapter(purchasedProductsAdapter);
        }
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
