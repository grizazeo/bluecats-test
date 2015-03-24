package com.showme.tst;

import java.util.List;


import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BlueCatsSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BeaconsSnifferAdapter extends BaseAdapter {
	private List<BCBeacon> mBeacons;
	private LayoutInflater mInflater;
	private int[] mRowColours = new int[] { Color.parseColor("#33b5e5"), Color.parseColor("#0099cc") };
	    
	public BeaconsSnifferAdapter(Context context, List<BCBeacon> beacons) {
		mBeacons = beacons;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		if (mBeacons != null) {
			return mBeacons.size();
		}
		return 0;
	}

	public BCBeacon getItem(int position) {
		if (mBeacons != null) {
			return mBeacons.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		BeaconsViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.beacon_sniffer_list_row, null);
			
			holder = new BeaconsViewHolder();
			holder.txtName = (TextView)convertView.findViewById(R.id.name);
			holder.txtRSSI = (TextView)convertView.findViewById(R.id.rssi);
			holder.txtCategories = (TextView)convertView.findViewById(R.id.categories);
			holder.txtProximity = (TextView)convertView.findViewById(R.id.proximity);
			convertView.setTag(holder);
		} else {
			holder = (BeaconsViewHolder)convertView.getTag();
		}

		BCBeacon beacon = mBeacons.get(position);
		holder.txtName.setText(beacon.getIBeaconKey().substring(32));
		holder.txtRSSI.setText(String.valueOf(beacon.getRSSI()) + " rssi");
		String categories = "";
		for (int i = 0; i < beacon.getCategories().length; i++) {
			if (i > 0) {
				categories += ", ";
			}
			categories += beacon.getCategories()[i].getName();
		}
		holder.txtCategories.setText(categories);
		holder.txtProximity.setText(beacon.getProximity().toString());
		
		System.out.println("soy yoooooo---"+categories);
		
		if(beacon.getProximity().toString()=="BC_PROXIMITY_IMMEDIATE" && categories=="black"){
			
			Intent beaconIntent = new Intent(null, BlackImage.class);
			Activity.class.cast(beaconIntent);
			
		}
		else{
			System.out.println("######################### fuuuuu");
		}
		
		int colourPos = position % mRowColours.length;
		convertView.setBackgroundColor(mRowColours[colourPos]);

		return convertView;
	}

	private static class BeaconsViewHolder {
		TextView txtName;
		TextView txtRSSI;
		TextView txtCategories;
		TextView txtProximity;
	}
}
