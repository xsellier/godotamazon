package org.godotengine.godot.gamecircle;

import android.app.Activity;

import java.util.EnumSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.godotengine.godot.Dictionary;
import org.godotengine.godot.GodotAmazonCommon;
import org.godotengine.godot.GodotLib;

import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.overlay.PopUpLocation;

public class GameCircleClient extends GodotAmazonCommon {
	private static GameCircleClient mInstance = null;
	private static final String TAG = "GameCircleClient";

	private AmazonGamesClient mClient;
	private AmazonGamesCallback amazonGamesCallback;
	private EnumSet<AmazonGamesFeature> mFeatures = EnumSet.of(AmazonGamesFeature.Achievements, AmazonGamesFeature.Leaderboards, AmazonGamesFeature.Whispersync);
	private boolean isConnected;

	public static GameCircleClient getInstance(Activity activity) {
		synchronized (GameCircleClient.class) {
			if (mInstance == null) {
				mInstance = new GameCircleClient(activity);
			}
		}

		return mInstance;
	}

	public GameCircleClient(Activity activity) {
		this.activity = activity;
		this.context = activity.getApplicationContext();

		amazonGamesCallback = new AmazonGamesCallback() {
			@Override
			public void onServiceNotReady(AmazonGamesStatus status) {
				Log.w(TAG, "Amazon game is not ready due to " + String.valueOf(status));

				mClient = null;
				isConnected = false;
				GodotLib.calldeferred(instance_id, "amazon_auth_connect_failed", new Object[]{ String.valueOf(status) });
			}

			@Override
			public void onServiceReady(AmazonGamesClient amazonGamesClient) {
				mClient = amazonGamesClient;
				isConnected = true;

				mClient.setPopUpLocation(PopUpLocation.TOP_CENTER);
				GodotLib.calldeferred(instance_id, "amazon_auth_connected", new Object[]{ });
			}
		};
	}

	@Override
	public void onStart() {
		connect();
	}

	@Override
	public void onResume() {
		connect();
	}

	@Override
	public void onPause() {
		if (amazonGamesCallback != null && isConnected) {
			mClient.release();
		}
	}

	public void connect() {
		if (!isConnected || mClient == null) {
			AmazonGamesClient.initialize(activity, amazonGamesCallback, mFeatures);
		} else {
			isConnected = true;

			GodotLib.calldeferred(instance_id, "amazon_auth_connected", new Object[]{ });
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

	public AmazonGamesClient getAmazonGamesClient() {
		return mClient;
	}
}