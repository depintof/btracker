package com.innovamos.btracker.async;

import com.estimote.sdk.Region;
import com.innovamos.btracker.dto.BeaconDTO;

import org.json.JSONObject;

/**
 * Beacon List Event
 */
public interface EventListener {
    void beaconsListResult(JSONObject jsonResult);
    void customerResult(JSONObject jsonResult);
    void zoneResult(JSONObject jsonResult);
    void zoneVisitResult(JSONObject jsonResult, long currentDate, boolean isEnteringToRegion, BeaconDTO beaconRegion);
    void productsZoneList(JSONObject jsonResult);
    void productsLikeList(JSONObject jsonResult);
    void insertProductLike(JSONObject jsonResult);
    void deleteProductLike(JSONObject jsonResult);
    void purchasedProductsList(JSONObject jsonResult);
    void insertProductPurchase(JSONObject jsonResult);
    void deleteProductPurchase(JSONObject jsonResult);
    void customerVisitsList(JSONObject jsonResult);
    void customerNotificationsList(JSONObject jsonResult);
    void insertVisit(JSONObject jsonResult);
}
