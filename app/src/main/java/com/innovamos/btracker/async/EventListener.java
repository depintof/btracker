package com.innovamos.btracker.async;

import org.json.JSONObject;

/**
 * Beacon List Event
 */
public interface EventListener {
    void beaconsListResult(JSONObject jsonResult);
    void customerResult(JSONObject jsonResult);
    void zoneResult(JSONObject jsonResult);
    void productsZoneList(JSONObject jsonResult);
    void productsLikeList(JSONObject jsonResult);
    void insertProductLike(JSONObject jsonResult);
    void deleteProductLike(JSONObject jsonResult);
}
