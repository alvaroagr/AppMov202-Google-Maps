package com.example.holetrackingapp.comm;

import com.example.holetrackingapp.model.Hole;
import com.google.gson.Gson;

/**
 * Thread that is used to create or update a hole in the database. If it receives a hole in
 * which isConfirmed is false and its ID isn't yet in the database, the PUT request will create
 * a new hole. If it receives a hole that has isConfirmed as true and the id is already in the
 * database, it'll update that hole's information.
 */
public class HoleWorker extends Thread {

    private Hole hole;

    public HoleWorker(Hole hole){
        this.hole = hole;
    }

    @Override
    public void run() {
        super.run();
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        utilDomi.PUTrequest("https://appmoviles202-408d5.firebaseio.com/holes/" + hole.getId()
                + "/hole.json", gson.toJson(hole));
    }
}
