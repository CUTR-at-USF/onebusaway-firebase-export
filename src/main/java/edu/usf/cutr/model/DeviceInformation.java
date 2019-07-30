package edu.usf.cutr.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DeviceInformation {
    public String appVersion;

    public String deviceModel;

    public String sdkVersion;

    public Integer sdkVersionInt;

    public String googlePlayServicesApp;

    public Integer googlePlayServicesLib;

    public Long regionId;

    public Boolean isTalkBackEnabled;

    public String timestamp;

    public DeviceInformation() {
    }

    public DeviceInformation(String appVersion, String deviceModel, String sdkVersion,
                             Integer sdkVersionInt, String googlePlayServicesApp,
                             Integer googlePlayServicesLib, Long regionId, Boolean isTalkBackEnabled) {
        this.appVersion = appVersion;
        this.deviceModel = deviceModel;
        this.sdkVersion = sdkVersion;
        this.sdkVersionInt = sdkVersionInt;
        this.googlePlayServicesApp = googlePlayServicesApp;
        this.googlePlayServicesLib = googlePlayServicesLib;
        this.regionId = regionId;
        this.isTalkBackEnabled = isTalkBackEnabled;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(appVersion).append(deviceModel).append(sdkVersion)
                .append(sdkVersionInt).append(googlePlayServicesApp).append(googlePlayServicesLib)
                .append(regionId).append(isTalkBackEnabled).toHashCode();
    }
}
