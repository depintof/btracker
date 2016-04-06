package com.btracker.test9.json;

import com.btracker.test9.dto.BeaconDTO;
import com.btracker.test9.dto.Customer;
import com.btracker.test9.utils.Cons;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Json Responses Decoder
 */
public class JsonResponseDecoder {

    /**
     * JSON -> BeaconsList
     * @param response Respuesta JSON
     */
    public static BeaconDTO[] beaconListResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONArray mensaje = response.getJSONArray(Cons.BEACONS);
                    BeaconDTO[] beaconDTOs = gson.fromJson(mensaje.toString(), BeaconDTO[].class);
                    return beaconDTOs;
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON -> Customer
     * @param response Respuesta JSON
     */
    public static Customer customerResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONObject mensaje = response.getJSONObject(Cons.CUSTOMERS);
                    return gson.fromJson(mensaje.toString(), Customer.class);
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
