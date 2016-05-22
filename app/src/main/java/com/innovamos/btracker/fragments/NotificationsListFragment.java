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
import com.innovamos.btracker.adapter.CustomerNotificationsArrayAdapter;
import com.innovamos.btracker.adapter.CustomerVisitsArrayAdapter;
import com.innovamos.btracker.dto.VisitsDTO;

import java.util.Arrays;

/**
 * Fragmento con la lista de notificaciones realizadas por el usuario
 */
public class NotificationsListFragment extends Fragment implements AbsListView.OnItemClickListener{

    // Lista de visitas realizadas por el usuario, recibida desde la actividad principal
    private static VisitsDTO[] customerNotificationsList;

    // Lista visual
    ListView notificationsListView;

    public static NotificationsListFragment newInstance(VisitsDTO[] customerNotificationsList) {
        NotificationsListFragment fragment = new NotificationsListFragment();
        NotificationsListFragment.customerNotificationsList = customerNotificationsList;
        return fragment;
    }

    public NotificationsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificationslist_list, container, false);

        notificationsListView = (ListView)view.findViewById(R.id.notificationslist);
        if(customerNotificationsList!=null){
            ArrayAdapter customerNotificationsAdapter = new CustomerNotificationsArrayAdapter(getContext(), Arrays.asList(customerNotificationsList));
            notificationsListView.setAdapter(customerNotificationsAdapter);
        }
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
