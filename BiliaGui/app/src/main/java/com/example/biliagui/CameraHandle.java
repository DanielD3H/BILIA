package com.example.biliagui;

import android.content.Intent;
import android.content.pm.PackageManager;

public class CameraHandle {

    /**
     * This function check if the phone has camera hardware.
     * @param
     * @return returns true if it does, else false
     */
    private static boolean checkCameraHardware() {
        if (MainActivity.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * This function will open the phone camera.
     * @param
     * @return
     */
    public static void openCamera()
    {
        if(checkCameraHardware())
        {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            MainActivity.getContext().startActivity(intent);
        }
    }
    /**
     * This function return if this command fits this class.
     * @param command
     * @return return if this command fits this class.
     */
    public static boolean isCammand(String command){
        return command.contains("camera");
    }
}