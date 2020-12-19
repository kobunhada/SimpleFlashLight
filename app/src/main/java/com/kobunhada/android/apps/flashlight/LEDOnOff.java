package com.kobunhada.android.apps.flashlight;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.content.Context;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.kobunhada.android.common.morsecode.PlayOnOff;

/**
 * Flashlight On/Off Control.
 *
 * @author kobunhada
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LEDOnOff implements PlayOnOff {
    private CameraManager cameraManager = null;
    private String cameraId = "";

    /**
     * Constructor.
     *
     * @param context Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LEDOnOff(Context context) {
        this.initialize(context);
    }

    /**
     * The LED light mode does on the state.
     */
    public void On() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The LED light mode does off the state.
     */
    public void Off() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize.
     * <pre>
     * This method checks as follows.
     *   - The device is an available flashlight, or not?
     *   - This app can use the camera device, or not?
     * </pre>
     *
     * @param context Activity
     */
    private void initialize(Context context) {
        Boolean isFlashAvailable = context.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isFlashAvailable) {
            doFinishApplication(context, "Your device doesn't support flash light!");
        }
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            doFinishApplication(context, "Sorry, this Application failed anything reason.");
            e.printStackTrace();
        }
    }

    /**
     * Event of when this app force finish.
     *
     * @param context Activity
     * @param message The message of the reason for the force finish
     */
    private void doFinishApplication(Context context, String message) {
        AlertDialog alert = new AlertDialog.Builder(context)
                .create();
        alert.setTitle("Error !!");
        alert.setMessage(message);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // closing the application
                        ((Activity)context).finish();
                        System.exit(0);
                    }
                });
        alert.show();
    }
}
