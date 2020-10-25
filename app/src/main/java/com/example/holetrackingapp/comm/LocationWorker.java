package com.example.holetrackingapp.comm;

import com.example.holetrackingapp.activity.MapsActivity;
import com.google.gson.Gson;

/**
 * Thread that constantly updates the current users position in the database.
 */
public class LocationWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public LocationWorker(MapsActivity ref){
        this.ref = ref;
        this.isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while(isAlive){
            delay(5000);
            if(ref.getCurrentPosition() != null){
                utilDomi.PUTrequest("https://appmoviles202-408d5.firebaseio.com/users/"
                        + ref.getUser() + "/location.json", gson.toJson(ref.getCurrentPosition()));
            }
        }
    }

    public void delay(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        isAlive = false;
    }
}
