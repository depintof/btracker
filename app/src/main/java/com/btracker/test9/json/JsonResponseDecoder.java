package com.btracker.test9.json;

import com.btracker.test9.dto.Beacon;
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
     * JSON -> Beacons
     * @param response Respuesta JSON
     */
    public static Beacon[] beaconListResponse(JSONObject response) {
        try {
            Gson gson = new Gson();
            String status = response.getString(Cons.STATUS);

            switch (status) {
                case Cons.STATUS_SUCCESS: // Respuesta exitosa
                    JSONArray mensaje = response.getJSONArray(Cons.BEACONS);
                    Beacon[] beacons = gson.fromJson(mensaje.toString(), Beacon[].class);
                    return beacons;
                case Cons.STATUS_FAIL: // Respuesta fallida
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
