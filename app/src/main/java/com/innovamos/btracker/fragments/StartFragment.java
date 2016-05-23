package com.innovamos.btracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
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
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.utils.Cons;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements FragmentCommunicator {

    // Objeto Cliente - Recibido desde la actividad Principal
    private static CustomerDTO customerDTO;

    // Imagen de carga
    private ImageView loadingView;
    // Animation del Buscador
    private AnimationDrawable loadingAnimation;

    public Context context;

    /*
     * Gestor de Beacons
     */
    // Gestor de Beacons
    private BeaconManager beaconManager;
    // Regiones de escaneo
    private static Region region;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartFragment.
     */
    public static StartFragment newInstance(CustomerDTO customerDTO) {
        StartFragment.customerDTO = customerDTO;
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
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    Log.e("Beacon encontrado: ", nearestBeacon.getMacAddress().toString());
                    productDetail(nearestBeacon, customerDTO);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        // Detener animación
        loadingAnimation.stop();
        loadingView.setVisibility(View.INVISIBLE);
        // Detener búsqueda de beacons
        if(region!=null){
            beaconManager.stopRanging(region);
        }
    }

    @Override
    public void passDataToFragment(Region region) {
        StartFragment.region = region;
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(StartFragment.region);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Animación de carga
        loadingAnimation.start();
        loadingView.setVisibility(View.VISIBLE);
        // Retomar búsqueda de beacons
        if(region!=null){
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        ((MainActivity)context).fc = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Método lanzado para abrir la actividad de producto
     * @param beacon Beacon
     * @param customerDTO Cliente
     */
    public void productDetail(Beacon beacon, CustomerDTO customerDTO) {
        Intent detailIntent = new Intent(getContext(), ProductActivity.class);
        detailIntent.putExtra("ProductBeacon", beacon);
        detailIntent.putExtra("Customer", customerDTO.getId());
        startActivity(detailIntent);
    }
}
