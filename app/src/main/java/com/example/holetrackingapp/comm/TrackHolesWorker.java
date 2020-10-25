package com.example.holetrackingapp.comm;

import com.example.holetrackingapp.activity.MapsActivity;
import com.example.holetrackingapp.model.Hole;
import com.example.holetrackingapp.model.HoleContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Thread that constantly tracks holes in the database. While holes won't change positions, new
 * holes may appear and old ones may be confirmed by other users.
 */
public class TrackHolesWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public TrackHolesWorker(MapsActivity ref){
        this.ref = ref;
        isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while(isAlive){
            delay(5000);
            String json = utilDomi.GETrequest(
                    "https://appmoviles202-408d5.firebaseio.com/holes.json");

            Type type = new TypeToken<HashMap<String, HoleContainer>>(){}.getType();
            HashMap<String, HoleContainer> holes = gson.fromJson(json, type);

            ArrayList<Hole> positions = new ArrayList<>();

            if(holes != null){
                holes.forEach(
                        (key, value) -> {
                            HoleContainer holeContainer = value;
                            positions.add(value.getHole());
                        }
                );
                ref.updateHoles(positions);
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

    public void finish(){
        isAlive = false;
    }
}
