package vztrack.gls.com.vztrack_user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ale.infra.application.RainbowContext;
import com.ale.infra.manager.call.ITelephonyListener;
import com.ale.infra.manager.call.PeerSession;
import com.ale.infra.manager.call.PeerSession.PeerSessionType;
import com.ale.infra.manager.call.WebRTCCall;
import com.ale.rainbow.phone.session.MediaState;
import com.ale.rainbowsdk.RainbowSdk;

import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoSink;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;

/**
 * Created by Santosh on 24-Jan-18.
 */

public class Rainbow_CallActivity extends AppCompatActivity implements ITelephonyListener, OnAudioFocusChangeListener {

    private final static String LOG_TAG = "Rainbow_CallActivity-->";

    private LinearLayout m_outgoingCallLayout;
    private LinearLayout m_incomingCallLayout;
    private LinearLayout m_ongoingCallLayout;

    private ImageView m_answerVideoCallButton;
    private LinearLayout m_cardViewPhoto;

    private SurfaceViewRenderer m_bigVideoView;
    private SurfaceViewRenderer m_littleVideoView;
    private VideoSink m_remoteVideoRenderer;
    private VideoSink m_localVideoRenderer;
    private boolean m_localVideoOnLittleView = true;
    private MediaPlayer m_mediaPlayer;
    ImageView imageViewSwitchCamera;
    ImageView imageViewAddVideo;
    ImageView imageViewPhoto;
    ImageView answerAudioCallButton;
    TextView callType, callStatus, name;
    ArrayList<FamilyBean> rainbowUsers;
    Chronometer chronometer;
    PowerManager powerManager;
    WakeLock wakeLock;
    CallReceiver callReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG,"come");

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        setContentView(R.layout.activity_audio_call_manage);

        rainbowUsers = MainActivity.rainbowUsers;
        chronometer = (Chronometer) findViewById(R.id.chronometer); // initiate a chronometer

        WebRTCCall currentCall = RainbowSdk.instance().webRTC().getCurrentCall();

        if (currentCall == null) {
            Log.e(LOG_TAG, "No call to display");
            finish();
            return;
        } else if (currentCall.getDistant() == null) {
            Log.e(LOG_TAG, "Contact is null");
            finish();
            return;
        }

        m_cardViewPhoto = (LinearLayout) findViewById(R.id.card_view_photo);
        callType = (TextView) findViewById(R.id.callType);
        callStatus = (TextView) findViewById(R.id.callStatus);
        name = (TextView) findViewById(R.id.name);

        imageViewPhoto = (ImageView) findViewById(R.id.photo_image_view);
        if (currentCall.getDistant().getPhoto() == null) {
            imageViewPhoto.setImageResource(R.drawable.contact);
        } else {
            imageViewPhoto.setImageBitmap(currentCall.getDistant().getPhoto());
        }

        String callTo = currentCall.getDistant().getFirstName() + " " + currentCall.getDistant().getLastName();
        String emailId = currentCall.getDistant().getMainEmailAddress();
        String flatNo = UtilityMethods.getFlatNumberFromEmail(emailId);
        callTo = callTo + flatNo;

        name.setText(callTo);
        // ===== m_incomingCallLayout
        m_incomingCallLayout = (LinearLayout) findViewById(R.id.incoming_call_layout);

        answerAudioCallButton = (ImageView) findViewById(R.id.answer_audio_button);
        answerAudioCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RainbowSdk.instance().webRTC().takeCall(false);
            }
        });

        m_answerVideoCallButton = (ImageView) findViewById(R.id.answer_video_button);
        m_answerVideoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RainbowSdk.instance().webRTC().takeCall(true);
            }
        });

        ImageView rejectCallButton = (ImageView) findViewById(R.id.reject_call_button);
        rejectCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("reject","call");
                RainbowSdk.instance().webRTC().rejectCall();
            }
        });


        // ===== m_outgoingCallLayout
        m_outgoingCallLayout = (LinearLayout) findViewById(R.id.outgoing_call_layout);
        //m_outgoingCallLayout.setVisibility(View.GONE);
        ImageView imageViewhangupCall = (ImageView) findViewById(R.id.hangup_call_button);
        imageViewhangupCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TelephonyMgr", "Hangup call");
                RainbowSdk.instance().webRTC().hangupCall();
            }
        });

        // ===== m_ongoingCallLayout
        m_ongoingCallLayout = (LinearLayout) findViewById(R.id.ongoing_call_layout);

        // Switch camera
        imageViewSwitchCamera = (ImageView) findViewById(R.id.switch_camera_button);
        imageViewSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RainbowSdk.instance().webRTC().switchCamera();
            }
        });

        // Mute / unmute
        final ImageView imageViewStateMicOff = (ImageView) findViewById(R.id.state_mic_off);
        final ImageView imageViewMute = (ImageView) findViewById(R.id.mute_image_button);
        imageViewMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RainbowSdk.instance().webRTC().mute(!RainbowSdk.instance().webRTC().isMuted(), false);
                imageViewMute.setImageResource(RainbowSdk.instance().webRTC().isMuted() ? R.drawable.btn_mic_on : R.drawable.btn_mic_off);
                //imageViewStateMicOff.setVisibility(RainbowSdk.instance().webRTC().isMuted() ? View.VISIBLE : View.GONE);
            }
        });

        // Add / remove video
        imageViewAddVideo = (ImageView) findViewById(R.id.add_video_button);
        imageViewAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RainbowSdk.instance().webRTC().getLocalVideoTrack() != null) {
                    if (RainbowSdk.instance().webRTC().dropVideo()) {
                        imageViewAddVideo.setImageResource(R.drawable.btn_camera_on);
                    }
                } else {
                    if (RainbowSdk.instance().webRTC().addVideo()) {
                        imageViewAddVideo.setImageResource(R.drawable.btn_camera_off);
                    }
                }
            }
        });

        // Hang up call
        ImageView imageViewHangupCall = (ImageView) findViewById(R.id.hang_up_image_view);
        imageViewHangupCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RainbowSdk.instance().webRTC().hangupCall();
            }
        });

        // Remote video view
        m_bigVideoView = (SurfaceViewRenderer) findViewById(R.id.big_video_view);
        m_bigVideoView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        m_bigVideoView.requestLayout();

        // Local video view
        m_littleVideoView = (SurfaceViewRenderer) findViewById(R.id.little_video_view);
        m_littleVideoView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        m_littleVideoView.setZOrderMediaOverlay(true);
        m_littleVideoView.requestLayout();

        // Init these two surface views
        try {
            m_bigVideoView.init(RainbowSdk.instance().webRTC().getCurrentCall().getEglBaseContext(), null);
            m_littleVideoView.init(RainbowSdk.instance().webRTC().getCurrentCall().getEglBaseContext(), null);
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "EGL context seems to be dead, call must have been removed");
            finish();
            return;
        }

        // Animation to show or hide buttons menu when video is used
        final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (m_ongoingCallLayout.getVisibility() != View.VISIBLE) {
                    m_ongoingCallLayout.setVisibility(View.VISIBLE);
                } else {
                    m_ongoingCallLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        m_bigVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RainbowSdk.instance().webRTC().getCurrentCall().wasInitiatedWithVideo()){
                    if (m_ongoingCallLayout.getVisibility() != View.VISIBLE) {
                        AlphaAnimation fade_in = new AlphaAnimation(0.0f, 1.0f);
                        fade_in.setDuration(500);
                        fade_in.setAnimationListener(animationListener);
                        m_ongoingCallLayout.startAnimation(fade_in);
                    } else {
                        AlphaAnimation fade_out = new AlphaAnimation(1.0f, 0.0f);
                        fade_out.setDuration(500);
                        fade_out.setAnimationListener(animationListener);
                        m_ongoingCallLayout.startAnimation(fade_out);
                    }
                }
            }
        });

        m_littleVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                m_localVideoOnLittleView = !m_localVideoOnLittleView;
//                List<MediaStream> stream = RainbowSdk.instance().webRTC().getStreams(PeerSession.PeerSessionType.AUDIO_VIDEO_SHARING);
                m_localVideoOnLittleView = !m_localVideoOnLittleView;
                List<MediaStream> stream = RainbowSdk.instance().webRTC().getStreams(PeerSession.PeerSessionType.AUDIO_VIDEO_SHARING);

                if (stream!=null) {
                    if (m_localVideoOnLittleView) {
                        renderRemoteVideo(stream, m_bigVideoView);
                        renderLocalVideo(RainbowSdk.instance().webRTC().getLocalVideoTrack(), m_littleVideoView);
                    } else {
                        renderRemoteVideo(stream, m_littleVideoView);
                        renderLocalVideo(RainbowSdk.instance().webRTC().getLocalVideoTrack(), m_bigVideoView);
                    }
                }

            }
        });

        // Listen to events
        RainbowSdk.instance().webRTC().registerTelephonyListener(this);
        updateLayoutWithCall(RainbowSdk.instance().webRTC().getCurrentCall());
        handleRinging();
    }

//    private void registerBroadCastReceiverForCall() {
//        // Create an IntentFilter instance.
//        IntentFilter intentFilter = new IntentFilter();
//
//        // Add network connectivity change action.
//        intentFilter.addAction("android.intent.action.PHONE_STATE");
//        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//
//        // Set broadcast receiver priority.
//        intentFilter.setPriority(100);
//
//        // Create a network change broadcast receiver.
//        callReceiver = new CallReceiver();
//
//        // Register the broadcast receiver with the intent filter object.
//        registerReceiver(callReceiver, intentFilter);
//    }

    private void unRegisterBroadCastReceiverForCall() {
        if (this.callReceiver != null) {
            unregisterReceiver(this.callReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        stopRinging();
        unRegisterBroadCastReceiverForCall();
        RainbowSdk.instance().webRTC().unregisterTelephonyListener(this);
        super.onDestroy();
    }

    @Override
    public void onCallAdded(WebRTCCall call) {
        awakeScreen();
    }

    @Override
    public void onCallModified(WebRTCCall call) {
        updateLayoutWithCall(call);
        stopRinging();
    }

    @Override
    public void onCallRemoved(WebRTCCall call) {
        stopRinging();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chronometer.stop();
                finish();
            }
        });
    }

    private void handleRinging() {
        if (m_mediaPlayer != null) {
            stopRinging();
        }

        try {
            String packageName = getApplicationContext().getPackageName();
            String ringtoneString = "android.resource://" + packageName + "/" + R.raw.call;

            if (RainbowSdk.instance().webRTC().getCurrentCall().getState() == MediaState.RINGING_OUTGOING) {
                ringtoneString = "android.resource://" + packageName + "/" + R.raw.outgoingrings;
            }

            Uri ringtoneUri = Uri.parse(ringtoneString);
            m_mediaPlayer = new MediaPlayer();
            m_mediaPlayer.setLooping(true);
            m_mediaPlayer.setDataSource(getApplicationContext(), ringtoneUri);
            m_mediaPlayer.prepare();
            m_mediaPlayer.start();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Impossible to get the default ringtone", e);
        }
    }

    private void stopRinging() {
        Log.e("CALLACT"," stopRinging ");
        if (m_mediaPlayer != null) {
            if (m_mediaPlayer.isPlaying()) {
                m_mediaPlayer.stop();
            }
            m_mediaPlayer.release();
            m_mediaPlayer = null;
        }
    }

    private void updateLayoutWithCall(final WebRTCCall call) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("CALLACT"," updateLayoutWithCall "+call.getState());
//                call.setState(MediaState.ACTIVE);
             Log.e("call state",call.getState()+"----");
                Log.e("isOutgoing",call.isOutgoing()+"----");

                if (call.getState() == MediaState.DIALING) {
                    callStatus.setText("Dialing...");
                    Log.e("call","dialing");
                }
                if (call.getState() == MediaState.IDLE) {
                    callStatus.setText("Call is in idle state...");
                    Log.e("call","IDLE");
                }
                if (call.getState() == MediaState.OFF_HOOK) {
                    callStatus.setText("Call is in idle state...");
                    Log.e("call","OFF_HOOK");
                }
                if (call.getState() == MediaState.RINGING_OUTGOING) {
                    showLayoutAndHideOthers(m_outgoingCallLayout);
                    if (call.wasInitiatedWithVideo()) {
                        callType.setText("VZTrack Video Call");
                    } else {
                        callType.setText("VZTrack Audio Call");

                    }
                    callStatus.setText("Calling To...");
//                    callStatus.setText(MediaState.RINGING_OUTGOING.toString());
                } else if (call.getState() == MediaState.RINGING_INCOMING) {

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

                    showLayoutAndHideOthers(m_incomingCallLayout);
                    m_answerVideoCallButton.setEnabled(call.wasInitiatedWithVideo());
                    callStatus.setText("Calling...");
//                    callStatus.setText(MediaState.RINGING_INCOMING.toString());
                    if (call.wasInitiatedWithVideo()) {
                        m_answerVideoCallButton.setVisibility(View.VISIBLE);
                        answerAudioCallButton.setVisibility(View.GONE);
                        callType.setText("VZTrack Video Call");
                    } else {
                        m_answerVideoCallButton.setVisibility(View.GONE);
                        answerAudioCallButton.setVisibility(View.VISIBLE);
                        callType.setText("VZTrack Audio Call");
                    }
                } else if (call.getState() == MediaState.ACTIVE) {
                    if (wakeLock != null) {
                        wakeLock.release();
                    }
                    callStatus.setText("Connected");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.stop();
                    chronometer.start();
                    chronometer.setVisibility(View.VISIBLE);

                    showLayoutAndHideOthers(m_ongoingCallLayout);
                    imageViewSwitchCamera.setVisibility(call.wasInitiatedWithVideo() ? View.VISIBLE : View.GONE);
                    imageViewAddVideo.setVisibility(call.wasInitiatedWithVideo() ? View.VISIBLE : View.GONE);
                    m_cardViewPhoto.setVisibility(call.wasInitiatedWithVideo() ? View.INVISIBLE : View.VISIBLE);
                    imageViewPhoto.setVisibility(call.wasInitiatedWithVideo() ? View.INVISIBLE : View.VISIBLE);
                    m_bigVideoView.setVisibility(call.wasInitiatedWithVideo() ? View.VISIBLE : View.GONE);
                    m_littleVideoView.setVisibility(call.wasInitiatedWithVideo() ? View.VISIBLE : View.GONE);
                }

                if (call.wasInitiatedWithVideo()) {
                    List<MediaStream> stream = RainbowSdk.instance().webRTC().getStreams(PeerSessionType.AUDIO_VIDEO_SHARING);
//                    List<VideoTrack> stream = RainbowSdk.instance().webRTC().getDistantVideoTracks(PeerSessionType.AUDIO_VIDEO_SHARING);

                    if (stream!=null) {
                        if (m_localVideoOnLittleView) {
                            renderRemoteVideo(stream, m_bigVideoView);
                            if (RainbowSdk.instance().webRTC().getLocalVideoTrack() != null) {
                                renderLocalVideo(RainbowSdk.instance().webRTC().getLocalVideoTrack(), m_littleVideoView);
                            }

                        } else {
                            renderRemoteVideo(stream, m_littleVideoView);
                            renderLocalVideo(RainbowSdk.instance().webRTC().getLocalVideoTrack(), m_bigVideoView);
                        }
                    }
                }
            }
        });
    }

    private void showLayoutAndHideOthers(final ViewGroup layout) {
        m_outgoingCallLayout.setVisibility(View.GONE);
        m_incomingCallLayout.setVisibility(View.GONE);
        m_ongoingCallLayout.setVisibility(View.GONE);
        if (layout != null) {
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void renderRemoteVideo(List<MediaStream> stream, SurfaceViewRenderer surfaceViewRenderer) {
        // set the remote renderer to this incoming m_stream:
        if (RainbowContext.getInfrastructure().getCapabilities().isVideoWebRtcAllowed() && stream.size()>0 && stream.get(0).videoTracks.size() > 0) {
            if (m_remoteVideoRenderer != null)
                stream.get(0).videoTracks.get(0).removeSink(m_remoteVideoRenderer);
            m_remoteVideoRenderer = surfaceViewRenderer;
            stream.get(0).videoTracks.get(0).addSink(m_remoteVideoRenderer);

        }
        surfaceViewRenderer.requestLayout();
    }

    private void renderLocalVideo(VideoTrack videoTrack, SurfaceViewRenderer surfaceViewRenderer) {
        if (m_localVideoRenderer != null)
            videoTrack.removeSink(m_localVideoRenderer);
        m_localVideoRenderer = surfaceViewRenderer;
        if(videoTrack!=null){
            videoTrack.addSink(m_localVideoRenderer);
        }
    }

    public void awakeScreen() {
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "VZTrack::WakelockTag");
        wakeLock.acquire();
    }

    private static Intent getIntent(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // dont call **super**, if u want disable back button in current screen.
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange<=0) {
            stopRinging();
            RainbowSdk.instance().webRTC().rejectCall();
        } else {
            handleRinging();
        }
    }

    public class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                stopRinging();
                RainbowSdk.instance().webRTC().rejectCall();
            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    stopRinging();
                    RainbowSdk.instance().webRTC().rejectCall();
                }
            }
        }
    }
}