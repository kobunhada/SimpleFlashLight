package com.kobunhada.android.apps.flashlight;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.kobunhada.android.common.morsecode.MorseCodePlayer;

/**
 * SOS playing controller.
 *
 * @author kobunhada
 */
public class SOSAsyncTask implements Runnable {
    /** MainActivity.*/
    private MainActivity activity;
    /** LEDOnOff.*/
    private LEDOnOff led;
    /** MorseCodePlayer.*/
    private MorseCodePlayer morse = null;

    /**
     * Constructor.
     *
     * @param activity MainActivity instance
     * @param led LEDOnOff instance
     */
    public SOSAsyncTask(MainActivity activity, LEDOnOff led) {
        this.activity = activity;
        this.led = led;
    }

    /**
     * run.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        activity.setLEDButtonDesable();

        try {
            if(Thread.interrupted()){
                throw new InterruptedException();
            }
        }
        catch(InterruptedException e) {
            // 処理なし
            activity.setLEDButtonEnable();
        }

        morse = MorseCodePlayer.getInstance();
        morse.setPlayOnOff(led);
        morse.setCode("SOS");
        morse.play();

        activity.setLEDButtonEnable();
        activity = null;

    }

    /**
     * Stop morse code play.
     */
    public void stop() {
        morse.stop();
    }

}
