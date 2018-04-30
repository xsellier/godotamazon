package org.godotengine.godot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class GodotAmazonCommon {
	protected static int instance_id;
	protected static Activity activity = null;
	protected static Context context = null;

	public void init(final int p_instance_id) {
		this.instance_id = p_instance_id;
	}

	public void onStart() {
		// Nothing to do
	}

	public void onPause() {
		// Nothing to do
	}

	public void onResume() {
		// Nothing to do
	}

	public void onStop() {
		activity = null;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Nothing to do
	}
}