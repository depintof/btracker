package com.innovamos.btracker.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.utils.Cons;

import org.json.JSONObject;

/**
 * Methods for CRUD on Database
 */
public class DatabaseConnectivity {

    EventListener el;

    // Constructor
    public DatabaseConnectivity(EventListener el){
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
                                        el.beaconsListResult(response);
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

    public void getCustomer(final Context context,String macAddress){
        String requestURL = Cons.GET_CUSTOMER + Cons.QUESTION_MARK + Cons.MAC + Cons.EQUAL_MARK + macAddress;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String)null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.customerResult(response);
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

    public void getZone(final Context context,String beaconId){
        String requestURL = Cons.GET_ZONE + Cons.QUESTION_MARK + Cons.BEACON_ID + Cons.EQUAL_MARK + beaconId;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String)null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.zoneResult(response);
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

    public void getProductZoneList(final Context context,String zoneId){
        String requestURL = Cons.GET_PRODUCTS_ZONE_LIST + Cons.QUESTION_MARK + Cons.ZONE_ID + Cons.EQUAL_MARK + zoneId;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String)null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.productsZoneList(response);
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
