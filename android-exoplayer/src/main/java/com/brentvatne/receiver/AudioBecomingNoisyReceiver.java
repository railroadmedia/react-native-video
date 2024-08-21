package com.brentvatne.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

public class AudioBecomingNoisyReceiver extends BroadcastReceiver {

    private final Context context;
    private BecomingNoisyListener listener = BecomingNoisyListener.NO_OP;

    public AudioBecomingNoisyReceiver(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            listener.onAudioBecomingNoisy();
        }
    }

    public void setListener(BecomingNoisyListener listener) {
        this.listener = listener;
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        compatRegisterReceiver(this.context, this, intentFilter, false);
    }

    public void removeListener() {
        this.listener = BecomingNoisyListener.NO_OP;
        try {
            context.unregisterReceiver(this);
        } catch (Exception ignore) {
            // ignore if already unregistered
        }
    }

    private void compatRegisterReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter, boolean exported) {
        if (Build.VERSION.SDK_INT >= 34 && context.getApplicationInfo().targetSdkVersion >= 34) {
            context.registerReceiver(receiver, filter, exported ? Context.RECEIVER_EXPORTED : Context.RECEIVER_NOT_EXPORTED);
        } else {
            context.registerReceiver(receiver, filter);
        }
    }
}
