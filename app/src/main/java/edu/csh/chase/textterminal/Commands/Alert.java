package edu.csh.chase.textterminal.Commands;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

import java.io.IOException;
import java.util.ArrayList;

import chase.csh.edu.textterminal.R;
import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Command.CommandFlag;

public class Alert extends Command {

    private final String FORCE_FLAG = parent.getString(R.string.command_alert_flag_force);

    public Alert(Context c, String[] values, String num) {
        super(c, c.getString(R.string.command_alert_title), R.drawable.ic_volume_up_white_48dp, values, num);
    }

    @Override
    protected boolean executeCommand() {

        final AudioManager audioManager = (AudioManager) parent.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        final int firstVol = volume;
        if (canUseFlag(FORCE_FLAG)) {
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(parent, RingtoneManager.getActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_NOTIFICATION));
        } catch (IOException e) {
            return false;
        }
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
            }
        });
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, firstVol, 0);
                sendMessage(parent.getString(R.string.command_alert_device_alerted), fromNumber);
            }
        });
        try {
            mediaPlayer.prepare();
        } catch (Exception e1) {
            mediaPlayer.release();
            return false;
        }
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.start();
            }
        });
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();
        return true;
    }

    @Override
    public String getHelpMessage() {
        return parent.getString(R.string.command_alert_help_message);
    }

    @Override
    public ArrayList<CommandFlag> getFlags() {
        ArrayList<CommandFlag> flags = new ArrayList<>(1);//Number of flags
        if (commandFlags.get(FORCE_FLAG) != null) {
            flags.add(commandFlags.get(FORCE_FLAG));//Was this flag loaded before?
        } else {
            flags.add(new CommandFlag(FORCE_FLAG, parent.getString(R.string.command_alert_flag_force_name), parent.getString(R.string.command_alert_flag_force_description)));
        }
        return flags;
    }

}
