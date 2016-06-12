package com.innovamos.btracker.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.estimote.sdk.Region;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.VisitsDTO;
import com.innovamos.btracker.utils.Common;
import com.innovamos.btracker.utils.Cons;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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
                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get beacons: Req: " + Cons.GET_ALL_BEACONS + " resp: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(context.getClass().getSimpleName(),"Error Volley while trying to get beacons list. " + error.getMessage());
                    }
                }
            )
        );
    }

    public void getCustomer(final Context context,String macAddress){
        final String requestURL = Cons.GET_CUSTOMER + Cons.QUESTION_MARK + Cons.MAC + Cons.EQUAL_MARK + macAddress;
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
                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get customer: Req: " + requestURL + " resp: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(),"Error Volley while trying to get customers. " + error.getMessage());
                    }
                }
            )
        );
    }

    public void getCustomer(final Context context){
        this.getCustomer(context, getMacAddr());
    }

    public void getZone(final Context context,String beaconId){
        final String requestURL = Cons.GET_ZONE + Cons.QUESTION_MARK + Cons.BEACON_ID + Cons.EQUAL_MARK + beaconId;
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
                                el.zoneResult(response);
                                Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get Zone: Req: " + requestURL + " resp: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get zone. " + error.getMessage());
                            }
                        }
                )
        );
    }

    public void getZoneVisit(final Context context,final BeaconDTO beaconRegion, final long currentDate, final boolean isEnteringToRegion){
        final String requestURL = Cons.GET_ZONE + Cons.QUESTION_MARK + Cons.BEACON_ID + Cons.EQUAL_MARK + beaconRegion.getId();
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
                                el.zoneVisitResult(response, currentDate,isEnteringToRegion,beaconRegion);
                                Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get Zone: Req: " + requestURL + " resp: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get zone. " + error.getMessage());
                            }
                        }
                )
        );
    }

    public void getProductZoneList(final Context context,String zoneId){
        final String requestURL = Cons.GET_PRODUCTS_ZONE_LIST + Cons.QUESTION_MARK + Cons.ZONE_ID + Cons.EQUAL_MARK + zoneId;
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
                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get prod zone list: Req: " + requestURL + " resp: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(),"Error Volley while trying to get products in zone. " + error.getMessage());
                    }
                }
            )
        );
    }

    public void createProductLike(final Context context,String idCustomer, String idProduct){
        final String requestURL = Cons.INSERT_PRODUCT_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct;
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
                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Create product like: Req: " + requestURL + " resp: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(), "Error Volley while trying to create like. " + error.getMessage());
                    }
                }
            )
        );
    }

    public void getProductsLike(final Context context,String idCustomer){
        final String requestURL = Cons.GET_PRODUCTS_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
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
                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get products like: Req: " + requestURL + " resp: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get likes list. " + error.getMessage());
                    }
                }
            )
        );
    }

    public void deleteProductLike(final Context context,String idCustomer, String idProduct){
        final String requestURL = Cons.DELETE_PRODUCT_LIKE + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct;
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
                                Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Delete prod like: Req: " + requestURL + " resp: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get products list. " + error.getMessage());
                            }
                        }
                )
        );
    }

    public void createProductPurchase(final Context context, String idProduct, String idCustomer, String price){
        final String requestURL = Cons.INSERT_PRODUCT_PURCHASE + Cons.QUESTION_MARK + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct + Cons.AND + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer + Cons.AND + Cons.PRICE + Cons.EQUAL_MARK + price;
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
                                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Create products purchase: Req: " + requestURL + " resp: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to create purchase. " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void getPurchasedProducts(final Context context,String idCustomer){
        final String requestURL = Cons.GET_PURCHASED_PRODUCTS + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
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
                                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get purchased prod: Req: " + requestURL + " resp: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get purchased products. " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void deleteProductPurchase(final Context context, String idProduct, String idCustomer){
        final String requestURL = Cons.DELETE_PRODUCT_PURCHASE + Cons.QUESTION_MARK + Cons.PRODUCT_ID + Cons.EQUAL_MARK + idProduct + Cons.AND + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
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
                                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Delete prod. purchase: Req: " + requestURL + " resp: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to delete product. " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void getCustomerVisits(final Context context,String idCustomer){
        final String requestURL = Cons.GET_CUSTOMER_VISITS + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;

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
                                        el.customerVisitsList(response);
                                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Get customer visits: Req: " + requestURL + " resp: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get customers visits. " + error.getMessage());
                                    }
                                }
                        )
                );
    }

    public void createVisit(final Context context, VisitsDTO visitsDTO) {
        final String requestURL = Cons.INSERT_VISIT + Cons.QUESTION_MARK +
                Cons.CUSTOMER_ID + Cons.EQUAL_MARK + visitsDTO.getCustomer_id() + Cons.AND +
                Cons.ZONE_ID + Cons.EQUAL_MARK + visitsDTO.getZone_id() + Cons.AND +
                Cons.TRIGGER_TIME + Cons.EQUAL_MARK + visitsDTO.getTrigger_time() + Cons.AND +
                Cons.LEAVE_TIME + Cons.EQUAL_MARK + visitsDTO.getLeave_time() + Cons.AND +
                Cons.VIEWED + Cons.EQUAL_MARK + visitsDTO.getViewed();

        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Create visit: Req: " + requestURL);

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
                                        el.insertVisit(response);
                                        Log.v(DatabaseConnectivity.class.getSimpleName(), ">>Create visit: Req: " + requestURL + " resp: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to create purchase. " + error.getMessage());
                                    }
                                }
                        )
                );
    }
    public void getCustomerNotifications(final Context context,String idCustomer){
        String requestURL = Cons.GET_CUSTOMER_NOTIFICATIONS + Cons.QUESTION_MARK + Cons.CUSTOMER_ID + Cons.EQUAL_MARK + idCustomer;
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
                                        el.customerNotificationsList(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(context.getClass().getSimpleName(), "Error Volley while trying to get customer notifications." + error.getMessage());
                                    }
                                }
                        )
                );
    }

    /**
     * Método que obtiene la MAC del dispositivo movil
     * @return MAC
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            return "02:00:00:00:00:00";
        }

        return null;
    }
}
