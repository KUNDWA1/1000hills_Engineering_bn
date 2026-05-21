package com.thousandhills.backend.dto;

import com.thousandhills.backend.model.VendorAvailability;

public class VendorAvailabilityRequest {

    private VendorAvailability availabilityStatus;

    public VendorAvailability getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(VendorAvailability availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
