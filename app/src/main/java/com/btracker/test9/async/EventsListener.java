package com.btracker.test9.async;

import com.btracker.test9.dto.Beacon;

import org.json.JSONObject;

/**
 * Events Generation
 */
public interface EventsListener {
    void beaconsResult(JSONObject jsonResult);
    void customerResult(JSONObject jsonResult);
}
