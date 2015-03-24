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

public class MainActivity extends Activity {
	
	
	
	private android.view.View.OnClickListener startRecive;
	private BluetoothAdapter bAdapter;
	private LocationManager locationManager;
	private ListView mBeaconsList;
	private List<BCBeacon> mBeacons;
	private BeaconsSnifferAdapter mAdapterBeacons;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Button button1= (Button)findViewById(R.id.button1);
		//Button button2= (Button)findViewById(R.id.button2);
		
		
		
		mBeacons = Collections.synchronizedList(new ArrayList<BCBeacon>());
		mBeaconsList = (ListView) findViewById(R.id.list_beacons_sniffer);
		mAdapterBeacons = new BeaconsSnifferAdapter(this, mBeacons);
		mBeaconsList.setAdapter(mAdapterBeacons);


		
	}
	
	public void startImageActivity(View button){
		
		Intent intentImageView = new Intent(this,BlackImage.class);
		
		startActivity(intentImageView);
	}
	
	@SuppressLint("ShowToast") public void startRecive(View button){
		boolean hasAdapters=true;
		bAdapter = BluetoothAdapter.getDefaultAdapter();
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		// habilitar bluetooth si no está habilitado
			if (!bAdapter.isEnabled()) {
					hasAdapters=false;
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
					alertDialogBuilder.setMessage("Esta app requiere de bluetooth 4.0 habilitado, deseas habilitarlo ahora?")
					.setPositiveButton("Si", mBluetoothDialogClickListener)
					.setNegativeButton("No", mBluetoothDialogClickListener).show();
					
			}
				// habilitar gps si no está habilitado
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && 
				!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				hasAdapters=false;
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
				alertDialogBuilder.setMessage("Esta app requiere de servicios de geolocalización, desea activarlos ahora?")
				.setPositiveButton("Si", mLocationServicesClickListener)
				.setNegativeButton("No", mLocationServicesClickListener).show();
			} 
				
				
		if(hasAdapters){
			BlueCatsSDK.startPurringWithAppToken(getApplicationContext(), "e11e46df-4882-450b-9e1e-94406b50bbc4");
			Toast.makeText(this, "Start transmition", 10).show();
			button.setVisibility(View.GONE);
			
			BCMicroLocationManager.getInstance().startUpdatingMicroLocation(mMicroLocationManagerCallback);
		
		}
	}
	
	private DialogInterface.OnClickListener mBluetoothDialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
				Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(enableBluetoothIntent);
	            break;
	        case DialogInterface.BUTTON_NEGATIVE:
	            // do nothing
	            break;
	        }
	    }
	};
	//activate gps
	private DialogInterface.OnClickListener mLocationServicesClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
				Intent enableLocationServicesIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(enableLocationServicesIntent);
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            // do nothing
	            break;
	        }
	    }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//-methods and all about connection
	//activate bluetooth
	private BCMicroLocationManagerCallback mMicroLocationManagerCallback = new BCMicroLocationManagerCallback() {
		@Override
		public void onDidEnterSite(BCSite site) {

		}

		@Override
		public void onDidExitSite(BCSite site) {

		}

		@Override
		public void onDidUpdateNearbySites(List<BCSite> sites) {

		}

		@Override
		public void onDidRangeBeaconsForSiteID(final BCSite site, final List<BCBeacon> beacons) {
			// to enable this method call BCMicroLocationManager.getInstance().startRangingBeaconsInSite(site)
			// from the onDidEnterSite callback.
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					for (BCBeacon beacon: beacons) {
						if (mBeacons.contains(beacon)) {
							BCBeacon beaconToUpdate = mBeacons.get(mBeacons.indexOf(beacon));
							beaconToUpdate.setRSSI(beacon.getRSSI());
							beaconToUpdate.setProximity(beacon.getProximity());
							
						} else {
							mBeacons.add(beacon);
							
						}
					}

					mAdapterBeacons.notifyDataSetChanged();
				}
			});
		}

		@Override
		public void onDidUpdateMicroLocation(final List<BCMicroLocation> microLocations) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (microLocations.size() > 0) {
						BCMicroLocation microLocation = microLocations.get(microLocations.size() - 1);
						
						Iterator<Entry<String, List<BCBeacon>>> iterator = microLocation.getBeaconsForSiteID().entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, List<BCBeacon>> entry = iterator.next();
							
							for (BCBeacon beacon: entry.getValue()) {
								if (mBeacons.contains(beacon)) {
									BCBeacon beaconToUpdate = mBeacons.get(mBeacons.indexOf(beacon));
									beaconToUpdate.setRSSI(beacon.getRSSI());
									beaconToUpdate.setProximity(beacon.getProximity());
								} else {
									mBeacons.add(beacon);
								}
							}
						}
						
						mAdapterBeacons.notifyDataSetChanged();
					}
				}
			});
		}

		@Override
		public void onDidBeginVisitForBeacon(BCBeaconVisit beaconVisit, BCBeacon beacon) {
			
		}

		@Override
		public void onDidEndVisitForBeacon(BCBeaconVisit beaconVisit, BCBeacon beacon) {
			
		}
	};

}
