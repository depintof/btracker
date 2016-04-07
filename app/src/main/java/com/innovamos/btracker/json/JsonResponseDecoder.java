package com.innovamos.btracker.json;

import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.dto.ProductDTO;
import com.innovamos.btracker.dto.ZoneDTO;
import com.innovamos.btracker.utils.Cons;
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
     * JSON -> CustomerDTO
     * @param response Respuesta JSON
     */
    public static CustomerDTO customerResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONObject mensaje = response.getJSONObject(Cons.CUSTOMERS);
                    return gson.fromJson(mensaje.toString(), CustomerDTO.class);
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON -> ZoneDTO
     * @param response Respuesta JSON
     */
    public static ZoneDTO zoneResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONArray mensaje = response.getJSONArray(Cons.ZONES);
                    return gson.fromJson(mensaje.getJSONObject(0).toString(), ZoneDTO.class);
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON -> ProductDTO
     * @param response Respuesta JSON
     */
    public static ProductDTO[] productListResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONObject mensaje = response.getJSONObject(Cons.PRODUCTS);
                    return gson.fromJson(mensaje.toString(), ProductDTO[].class);
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
