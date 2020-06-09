/**
 * BLE Device List Adapter
 */

package com.example.sleep;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LeDeviceListAdapter extends BaseAdapter {

    // BLE Device Object를 저장할 ArrayList Object 생성
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();


    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final Context context = parent.getContext();

        if(view == null) {
            // Android에서 View를 생성하는 기본적인 방법인 LayoutInflater를 사용
            LayoutInflater mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflator.inflate(R.layout.listitem_device, parent, false);
        }

        TextView device_name = (TextView) view.findViewById(R.id.device_name);
        TextView device_address = (TextView) view.findViewById(R.id.device_address);

        BluetoothDevice device = mLeDevices.get(i);
        final String deviceName = device.getName();

        if(deviceName != null && deviceName.length() > 0)
            device_name.setText(deviceName);
        else
            device_name.setText("This device is unknown...");

        device_address.setText(device.getAddress());

        return view;
    }

    // Custom Methods
    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }
}
