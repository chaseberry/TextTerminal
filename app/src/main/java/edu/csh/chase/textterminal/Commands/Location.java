package edu.csh.chase.textterminal.Commands;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import chase.csh.edu.textterminal.R;
import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Command.CommandFlag;

public class Location extends Command {

    GoogleApiClient locationClient;
    private final String ACCURACY_FLAG = parent.getString(R.string.command_location_flag_accuracy);


    GoogleApiClient.ConnectionCallbacks connectionListener = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle bundle) {
            android.location.Location l = LocationServices.FusedLocationApi.getLastLocation(locationClient);
            if (l != null) {
                String location = "(" + l.getLatitude() + ", " + l.getLongitude() + ")";
                if (canUseFlag(ACCURACY_FLAG)) {
                    location += parent.getString(R.string.command_location_accuracy_addition) + l.getAccuracy() + "m";
                }
                sendMessage(parent.getString(R.string.command_location_device_located) + location, fromNumber);
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
        super(c, c.getString(R.string.command_location_title), R.drawable.ic_my_location_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        buildGoogleApiClient();
        locationClient.connect();
        return true;
    }

    @Override
    public String getHelpMessage() {
        return parent.getString(R.string.command_location_help);
    }

    @Override
    protected ArrayList<CommandFlag> getFlags() {
        ArrayList<CommandFlag> flags = new ArrayList<>(1);
        if (commandFlags.get(ACCURACY_FLAG) != null) {
            flags.add(commandFlags.get(ACCURACY_FLAG));//Flag exists and was loaded in from memory
        } else {
            //Flag was not loaded in from memory (IE first run, so it must be created)
            flags.add(new CommandFlag(ACCURACY_FLAG, parent.getString(R.string.command_location_flag_accuracy_name),
                    parent.getString(R.string.command_location_flag_accuracy_description)));
        }
        return flags;
    }

}
