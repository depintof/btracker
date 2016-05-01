package com.innovamos.btracker.json;

import com.google.gson.Gson;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.utils.Cons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Json Message Encoder
 */
public class JsonMessageEncoder {

    /**
     * @param idCustomer ID del usuario que da like
     * @param idProduct ID del producto que recibe el like
     */
    public static JSONObject encodeProductLike(String idCustomer, String idProduct) {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("idCustomer", idCustomer);
        map.put("idProduct", idProduct);
        return new JSONObject(map);
    }

}
