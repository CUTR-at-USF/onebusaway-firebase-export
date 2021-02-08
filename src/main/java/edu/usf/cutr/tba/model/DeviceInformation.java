package edu.usf.cutr.tba.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DeviceInformation {
    public String appVersion;

    public String deviceModel;

    public String googlePlayServicesApp;

    public Integer googlePlayServicesLib;

    public Boolean isIgnoringBatteryOptimizations;

    public Boolean isPowerSaveModeEnabled;

    public Boolean isTalkBackEnabled;

    public Long regionId;

    public String sdkVersion;

    public Integer sdkVersionInt;

    public String timestamp;

    public DeviceInformation() {
    }

    public DeviceInformation(String appVersion, String deviceModel, String sdkVersion,
                             Integer sdkVersionInt, String googlePlayServicesApp,
                             Integer googlePlayServicesLib, Long regionId, Boolean isTalkBackEnabled,
                            Boolean isPowerSaveModeEnabled, Boolean isIgnoringBatteryOptimizations,
                             String timestamp) {
        this.appVersion = appVersion;
        this.deviceModel = deviceModel;
        this.sdkVersion = sdkVersion;
        this.sdkVersionInt = sdkVersionInt;
        this.googlePlayServicesApp = googlePlayServicesApp;
        this.googlePlayServicesLib = googlePlayServicesLib;
        this.regionId = regionId;
        this.isTalkBackEnabled = isTalkBackEnabled;
        this.isPowerSaveModeEnabled = isPowerSaveModeEnabled;
        this.isIgnoringBatteryOptimizations = isIgnoringBatteryOptimizations;
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp(){ return this.timestamp; }

    public Boolean getIgnoringBatteryOptimizations() { return isIgnoringBatteryOptimizations; }

    public Boolean getPowerSaveModeEnabled() { return isPowerSaveModeEnabled; }

    public Boolean getTalkBackEnabled() { return isTalkBackEnabled; }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(appVersion).append(deviceModel).append(sdkVersion)
                .append(sdkVersionInt).append(googlePlayServicesApp).append(googlePlayServicesLib)
                .append(regionId).append(isTalkBackEnabled).toHashCode();
    }
}
