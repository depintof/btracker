package com.innovamos.btracker.async;

import org.json.JSONObject;

/**
 * Events Generation
 */
public interface EventsListener {
    void beaconsResult(JSONObject jsonResult);
    void customerResult(JSONObject jsonResult);
}
