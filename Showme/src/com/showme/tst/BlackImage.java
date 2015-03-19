package com.showme.tst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconVisit;
import com.bluecats.sdk.BCMicroLocation;
import com.bluecats.sdk.BCMicroLocationManager;
import com.bluecats.sdk.BCMicroLocationManagerCallback;
import com.bluecats.sdk.BCSite;
import com.bluecats.sdk.BlueCatsSDK;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;

public class BlackImage extends Activity {
	
	
	
	private android.view.View.OnClickListener startRecive;
	private BluetoothAdapter bAdapter;
	private LocationManager locationManager;
	private ListView mBeaconsList;
	private List<BCBeacon> mBeacons;
	private BeaconsSnifferAdapter mAdapterBeacons;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
	}
}