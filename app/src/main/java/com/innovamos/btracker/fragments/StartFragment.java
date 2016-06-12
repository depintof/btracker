package com.innovamos.btracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.innovamos.btracker.MainActivity;
import com.innovamos.btracker.ProductActivity;
import com.innovamos.btracker.R;
import com.innovamos.btracker.async.FragmentCommunicator;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.utils.Common;
import com.innovamos.btracker.utils.Cons;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StartFragment extends Fragment implements FragmentCommunicator {

    private static CustomerDTO customerDTO;

    private ImageView loadingView;
    private AnimationDrawable loadingAnimation;

    public Context context;

    private Date lastLaunch;
    private Boolean canView;

    private BeaconManager beaconManager;
    private Region region;

    static Map<String, List<String>> placesByBeacons = new HashMap<>();

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (placesByBeacons.containsKey(beaconKey)) {
            return placesByBeacons.get(beaconKey);
        }
        return Collections.emptyList();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartFragment.
     */
    public static StartFragment newInstance() {
        return new StartFragment();
    }

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Iniciar búsqueda de beacons
        beaconManager = new BeaconManager(getContext());
        //beaconManager.setForegroundScanPeriod(25000,5000);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                if (lastLaunch != null) {
                    Date currentDate = Calendar.getInstance().getTime();
                    Long seconds = currentDate.getTime() - lastLaunch.getTime();
                    seconds = seconds / 1000;
                    Log.d("Elapsed time: ", seconds.toString());

                    if (seconds >= Cons.TIMEOUT) {
                        lastLaunch = Calendar.getInstance().getTime();
                        canView = true;
                    }
                } else {
                    lastLaunch = Calendar.getInstance().getTime();
                    Log.d("Launched time:", lastLaunch.toString());
                    canView = false;
                }

                if (!list.isEmpty() && canView) {
                    canView = false;
                    Beacon nearestBeacon = list.get(0);

                    Log.d("Distance", Double.toString(Common.getDistance(nearestBeacon.getRssi(), nearestBeacon.getMeasuredPower())));

                    List<String> places = placesNearBeacon(nearestBeacon);

                    Log.d("Nearest beacon", places.toString());

                    if (!places.isEmpty()) {
                        if (customerDTO == null) {
                            customerDTO = ((MainActivity) getActivity()).getCustomerDTO();
                        }
                        productDetail(nearestBeacon, customerDTO);
                    } else {
                        Log.d("Beacon without zone", nearestBeacon.getMajor() + ":" + nearestBeacon.getMinor());
                        canView = true;
                    }
                }
            }
        });

        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        // Configuración de la imagen de carga
        loadingView = (ImageView) view.findViewById(R.id.loadingView);
        // Obtener animación de loading
        if (loadingView != null) {
            loadingAnimation = (AnimationDrawable) loadingView.getBackground();
            loadingAnimation.start();
        }

        return view;
    }

    @Override
    public void onStop(){
        super.onStop();
        loadingAnimation.stop();
        loadingView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();

        loadingAnimation.stop();
        loadingView.setVisibility(View.INVISIBLE);

        beaconManager.stopRanging(region);
    }

    @Override
    public void setBeaconList(BeaconDTO[] beaconsList) {

        for (final BeaconDTO beacon : beaconsList) {

            placesByBeacons.put(beacon.getMajor() + ":" + beacon.getMinor(), new ArrayList<String>() {{
                add("Beacon " + beacon.getId());
            }});
        }

        Log.d("Places", placesByBeacons.toString());
    }

    @Override
    public void setCustomer(CustomerDTO customer) {
        customerDTO = customer;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        loadingAnimation.start();
        loadingView.setVisibility(View.VISIBLE);

        lastLaunch = Calendar.getInstance().getTime();
        canView = false;

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        ((MainActivity)context).fc = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Método lanzado para abrir la actividad de producto
     * @param beacon Beacon
     * @param customerDTO Cliente
     */
    public void productDetail(Beacon beacon, CustomerDTO customerDTO) {
        Intent detailIntent = new Intent(getContext(), ProductActivity.class);
        detailIntent .setFlags(detailIntent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        detailIntent.putExtra("ProductBeacon", beacon);
        detailIntent.putExtra("Customer", customerDTO.getId());
        startActivity(detailIntent);
    }
}
