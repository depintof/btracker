package com.btracker.test9.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.btracker.test9.async.EventsListener;
import com.btracker.test9.utils.Cons;

import org.json.JSONObject;

/**
 * Methods for CRUD on Database
 */
public class DatabaseConnectivity {

    EventsListener el;

    // Constructor
    public DatabaseConnectivity(EventsListener el){
        this.el = el;
    }

    public void getBeaconsList(final Context context){
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                Cons.GET_ALL_BEACONS,
                                (String)null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.beaconsResult(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(),"Error Volley: " + error.getMessage());
                                    }
                                }
                        )
                );
    }
}
