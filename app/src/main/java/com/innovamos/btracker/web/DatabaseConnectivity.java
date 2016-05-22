package com.innovamos.btracker.web;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.utils.Cons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        VolleySingleton.getInstance(context).addToRequestQueue(
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
        VolleySingleton.getInstance(context).addToRequestQueue(
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
        VolleySingleton.getInstance(context).addToRequestQueue(
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
        VolleySingleton.getInstance(context).addToRequestQueue(
            new JsonObjectRequest(
                Request.Method.GET,
                requestURL,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Enviar respuesta JSON por Interfaz
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

    public void createProductLike(final Context context,String idCustomer, String idProduct){
        String requestURL = Cons.INSERT_PRODUCT_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct;
        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(
            new JsonObjectRequest(
                Request.Method.GET,
                requestURL,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    // Procesar la respuesta Json
                    el.insertProductLike(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                    }
                }
            )
        );
    }

    public void getProductsLike(final Context context,String idCustomer){
        String requestURL = Cons.GET_PRODUCTS_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(
            new JsonObjectRequest(
                Request.Method.GET,
                requestURL,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    // Procesar la respuesta Json
                    el.productsLikeList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                    }
                }
            )
        );
    }

    public void deleteProductLike(final Context context,String idCustomer, String idProduct){
        String requestURL = Cons.DELETE_PRODUCT_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct;
        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(
            new JsonObjectRequest(
                Request.Method.GET,
                requestURL,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    // Procesar la respuesta Json
                    el.deleteProductLike(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                    }
                }
            )
        );
    }

    public void createProductPurchase(final Context context, String idProduct, String idCustomer, String price){
        String requestURL = Cons.INSERT_PRODUCT_PURCHASE + Cons.QUESTION_MARK + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct + Cons.AND + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRICE + Cons.EQUAL_MARK + price;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String) null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.insertProductPurchase(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void getPurchasedProducts(final Context context,String idCustomer){
        String requestURL = Cons.GET_PURCHASED_PRODUCTS + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String) null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.purchasedProductsList(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void deleteProductPurchase(final Context context, String idProduct, String idCustomer){
        String requestURL = Cons.DELETE_PRODUCT_PURCHASE + Cons.QUESTION_MARK + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct + Cons.AND + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                requestURL,
                                (String) null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        el.deleteProductPurchase(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                                    }
                                }
                        )
                );
    }
}
