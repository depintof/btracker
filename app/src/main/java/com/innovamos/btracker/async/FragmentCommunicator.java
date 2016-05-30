package com.innovamos.btracker.async;

import com.estimote.sdk.Region;
import com.innovamos.btracker.dto.BeaconDTO;

public interface FragmentCommunicator {
    //public void passDataToFragment(Region region);

    public void setBeaconList(BeaconDTO[] beaconsList);
}
