package com.example.findfuel.directions;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ademar
 * @since Classe criada em 14/10/2017
 */

class GetDirectionsData extends AsyncTask<Object, Integer, List<List<HashMap<String, String>>>> {

    private GoogleMap mMap;

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(Object... objects) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            mMap = (GoogleMap) objects[0];
            jObject = new JSONObject(objects[1].toString());
            Log.d("GetDirectionsData",objects[1].toString());
            DataParser parser = new DataParser();
            Log.d("GetDirectionsData", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("GetDirectionsData","Executing routes");
            Log.d("GetDirectionsData",routes.toString());

        } catch (Exception e) {
            Log.d("GetDirectionsData",e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.RED);

            Log.d("onPostExecute","onPostExecute lineoptions decoded");

        }

        // Drawing polyline in the Google Map for the i-th route
        if(lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }
}
