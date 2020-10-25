package com.example.holetrackingapp.comm;

import android.util.Log;

import com.example.holetrackingapp.activity.MapsActivity;
import com.example.holetrackingapp.model.Position;
import com.example.holetrackingapp.model.PositionContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Thread that constantly tracks users in the database. As they move, users' position will be
 * updated in the database, so the app has to reflect that.
 */
public class TrackUsersWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public TrackUsersWorker(MapsActivity ref){
        this.ref = ref;
        isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(5000);
            String json = utilDomi.GETrequest(
                    "https://appmoviles202-408d5.firebaseio.com/users.json");
            Type type = new TypeToken<HashMap<String, PositionContainer>>(){}.getType();
            HashMap<String, PositionContainer> users = gson.fromJson(json, type);

            ArrayList<Position> positions = new ArrayList<>();
            if(users != null){
                users.forEach(
                        (key, value) -> {
                            Log.e(">>>", key);
                            PositionContainer positionContainer = value;
                            positions.add(positionContainer.getLocation());
                        }
                );
                ref.updateUsers(positions);
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
