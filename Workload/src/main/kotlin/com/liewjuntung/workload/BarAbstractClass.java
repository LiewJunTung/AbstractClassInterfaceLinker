package com.liewjuntung.workload;

import com.soon.work.Something;

public abstract class BarAbstractClass {
    /** Permissions */
    public abstract void checkCameraPermission();

    public abstract void mobileDeviceGetInfo(String uuid);

    public abstract void mobileDeviceInit(String uuid,  boolean videoMode);
    /**
     * true if phone supports arkit face, false otherwise (should return false on android)
     * hasARFace(uuid : string) : bool;
     * perform a hit test with xy of the image (normalized 0-1 topleft for ios, image px from topleft for android)
     * use ARFrame raycastQuery for IOS, then pass to session's raycast. uses Frame.hitTest(float, float) for android instead. returns the HitResult structure
     */
    public abstract void mobileDevicePreviewStart(String uuid);

    public abstract void mobileDevicePreviewStop(String uuid);

    public abstract void mobileDeviceHighResCapture(String uuid);

    public abstract void mobileDeviceUninit(String uuid);

    public abstract boolean isGood(boolean flag);
    public abstract boolean isGood2(boolean flag);

    public abstract void doSomethingElse(Something something);
}
