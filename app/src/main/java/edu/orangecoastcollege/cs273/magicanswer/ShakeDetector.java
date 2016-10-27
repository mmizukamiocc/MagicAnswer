package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by mmizukami on 10/27/2016.
 */

public class ShakeDetector implements SensorEventListener {

    // Constant to represent a shake threshold
    private static final float SHAKE_THRESHOLD = 25f;
    // Constant to represent how long between shakes (milliseconds)
    private static final int SHAKE_TIME_LAPSE = 2000;

    // What was the last time the event occurred:
    private long  timeOfLastShake;
    // Define a listener to register onShake events
    private OnShakeListener shakeListener;

    // Constructor to create a new ShakeDetector passing in an OnShakeListener as argument
    public ShakeDetector(OnShakeListener listener)
    {
        shakeListener = listener;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Determine if the event is an accelerometer
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            //get the x, y, z values when this event occurs:
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Compare all 3 values against gravity

            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // Compute sum of squares
            double vector = Math.pow(gForceX,2.0) + Math.pow(gForceY,2.0) + Math.pow(gForceZ,2.0);

            // Compute gForce
            float gForce = (float) Math.sqrt(vector);

            // Compare gForce against the threshold
            if(gForce > SHAKE_THRESHOLD)
            {
                // Get the current time:
                long now = System.currentTimeMillis();
                // Compare to see if the is at least 2000 milliseconds
                // greater than the time of the last shake
                if(now - timeOfLastShake >= SHAKE_TIME_LAPSE)
                {
                    timeOfLastShake = now;
                    // Register a shake event!!
                    shakeListener.onShake();

                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Define our own interface (method for other classes to implement)
    // called onShake()
    // It's the responsibility of MagicAnswerActivity to implement this method
    public interface OnShakeListener{
        void onShake();
    }
}
