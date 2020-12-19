package com.kobunhada.android.apps.flashlight;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Future;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.RequiresApi;

/**
 * MainActivity.
 *
 * @author kobunhada
 */
public class MainActivity extends Activity {
    /**SOS button.*/
    private Button sos = null;
    /**On/Off button.*/
    private Button onoff = null;
    /** Other Apps link.*/
    private Button otherdApps = null;
    /** GUI thread handler.*/
    private Handler guiThreadHandler = null;
    /** Executor service.*/
    private ExecutorService asyncExecutorService;
    /** Starter task.*/
    private Runnable starterTask;
    /** SOS AsyncTask.*/
    private SOSAsyncTask asyncTask = null;
    /** Flashlight state.*/
    private boolean isOn = false;
    /** LED controller.*/
    private LEDOnOff led = null;
    /** Button disabled color.*/
    private static final int BUTTON_DISABLED_COLOR = Color.rgb(98, 96, 104);
    /** Button enabled color.*/
    private static final int BUTTON_ENABLED_COLOR = Color.rgb(248, 251, 248);
    @SuppressWarnings("unchecked")
    private Future taskPending;

    /**
     * onCreate event.
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        guiThreadHandler = new Handler();
        asyncExecutorService = Executors.newSingleThreadExecutor();
        starterTask = new Runnable() {
            public void run() {
                try{
                    asyncTask =
                            new SOSAsyncTask(MainActivity.this, led);
                    taskPending =
                            asyncExecutorService.submit(asyncTask);
                }
                catch (RejectedExecutionException e) {

                }
            }
        };

        otherdApps = (Button)findViewById(R.id.button_otherapps);

        otherdApps.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=%40kobunhada&so=1&c=apps"));
                startActivity(intent);

            }
        });



        sos = (Button)findViewById(R.id.button_sos);
        sos.setText("SOS");
        sos.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startAsyncTask();

            }
        });

        onoff = (Button)findViewById(R.id.button_onoff);
        onoff.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                if (isOn) {
                    // 消灯
                    offLED();
                }
                else {
                    // 点灯
                    onLED();
                }
            }
        });
        isOn = true;
    }

    protected void startAsyncTask() {
        guiThreadHandler.removeCallbacks(starterTask);
        guiThreadHandler.post(starterTask);
    }

    /**
     * onResume event.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (led == null) {
            led = new LEDOnOff(this);
        }

        if (isOn) {
            onLED();
        }
        else {
            offLED();
        }
    }

    /**
     * onStop event.
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopLED();
    }

    /**
     * onPause event.
     */
    @Override
    public void onPause() {
        super.onPause();
        stopLED();
    }

    /**
     * onKeyDown event.
     * <pre>
     * Tap the Home button or Back button to exit the app.
     * </pre>
     * @param keyCode key code
     * @param event key event
     * @return If "Home button" or "Back button" tapped: false, Other case is unknown :D
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            finishLED();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * The flashlight does "On" state.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onLED() {
        onoff.setText("OFF");
        led.On();
        isOn = true;
    }
    /**
     * The flashlight does "Off" state.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void offLED() {
        onoff.setText("ON");
        led.Off();
        isOn = false;
    }

    /**
     * The flashlight stop.
     */
    private void stopLED() {
        if (asyncTask != null) {
            asyncTask.stop();
        }

        guiThreadHandler.removeCallbacks(starterTask);
        sos.setEnabled(true);
        sos.setTextColor(BUTTON_ENABLED_COLOR);
        onoff.setEnabled(true);
        onoff.setTextColor(BUTTON_ENABLED_COLOR);
    }

    /**
     * This app do finish.
     */
    private void finishLED() {
        stopLED();
        finish();
    }

    /**
     * LED Button do "Desable" state.
     */
    public void setLEDButtonDesable() {
        guiThreadHandler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                offLED();
                sos.setEnabled(false);
                sos.setTextColor(BUTTON_DISABLED_COLOR);
                onoff.setEnabled(false);
                onoff.setTextColor(BUTTON_DISABLED_COLOR);
            }
        });
    }

    /**
     * LED Button do "Enable" state.
     */
    public void setLEDButtonEnable() {
        guiThreadHandler.post(new Runnable() {
            public void run() {
                sos.setEnabled(true);
                sos.setTextColor(BUTTON_ENABLED_COLOR);
                onoff.setEnabled(true);
                onoff.setTextColor(BUTTON_ENABLED_COLOR);
            }
        });
    }
}
