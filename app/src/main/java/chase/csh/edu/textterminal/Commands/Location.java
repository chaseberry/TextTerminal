package chase.csh.edu.textterminal.Commands;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chase.csh.edu.textterminal.R;

public class Location extends Command {

    GoogleApiClient locationClient;

    GoogleApiClient.ConnectionCallbacks connectionListener = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle bundle) {
            android.location.Location l = LocationServices.FusedLocationApi.getLastLocation(locationClient);
            if (l != null) {
                String location = "(" + l.getLatitude() + ", " + l.getLongitude() + ")";
                if (flags.contains("-a")) {
                    location += " within " + l.getAccuracy() + "m";
                }
                sendMessage("Location is " + location, fromNumber);
                locationClient.disconnect();
                locationClient = null;
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

    };

    protected synchronized void buildGoogleApiClient() {
        locationClient = new GoogleApiClient.Builder(parent)
                .addConnectionCallbacks(connectionListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    };

    public Location(Context c, String[] values, String phone) {
        super(c, "Location", R.drawable.ic_my_location_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        buildGoogleApiClient();
        locationClient.connect();
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "Gets the location of this device.";
    }

    @Override
    public String[] getParams() {
        return new String[0];
    }

    @Override
    public CommandFlag[] getFlags() {
        return new CommandFlag[]{new CommandFlag("-a", "Accuracy", "Sends the accuracy back with the location.")};
    }

    @Override
    protected JSONObject addExtras(JSONObject obj) {
        try {
            obj.put(KEY_FLAGS, new JSONArray().put("-a"));
        } catch (JSONException e) {

        }
        return obj;
    }
}
