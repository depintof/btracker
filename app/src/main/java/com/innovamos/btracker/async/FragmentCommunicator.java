package com.innovamos.btracker.async;

import com.estimote.sdk.Region;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;

public interface FragmentCommunicator {
    void setBeaconList(BeaconDTO[] beaconsList);

    void setCustomer(CustomerDTO customerDTO);
}
