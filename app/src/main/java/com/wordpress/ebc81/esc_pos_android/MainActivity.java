/**
 * Copyright (c) <2015>, <Christian Ebner cebner@gmx.at>
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.wordpress.ebc81.esc_pos_android;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wordpress.ebc81.esc_pos_lib.USBPort;
import com.wordpress.ebc81.esc_pos_lib.ESC_POS_EPSON_ANDROID;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final int RESULT_SETTINGS = 1;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";


    private static final String TAG = "ESC_POS_SAMPLE";

    // Intent
    private PendingIntent mPermissionIntent;
    // USB
    private UsbManager mUsbManager;
    private USBPort mUsbPort;

    private ESC_POS_EPSON_ANDROID mEscPos;

    //GUI
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // TODO
        // Regist BroadCast Receiver. (To acquire Permission.)
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        filter = new IntentFilter(mUsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
        filter = new IntentFilter(mUsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mUsbReceiver, filter);

        mUsbPort = new USBPort(mUsbManager);
        mEscPos  = new ESC_POS_EPSON_ANDROID(mUsbPort);


        ListView listview = (ListView) findViewById(R.id.listViewExolore);
        list.add("USB Device List");
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        mAdapter.notifyDataSetChanged();
        listview.setAdapter(mAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent settingActivity = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mUsbReceiver);
        try {
            mUsbPort.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action))
            {
                synchronized (this)
                {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        if (device != null)
                        {
                            // call method to set up device communication
                        }
                    }
                    else
                    {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
            {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null)
                {
                    // call your method that cleans up and closes communication with the device
                    mUsbPort.SetUSBConnectionFlag(false);
                    list.clear();
                    list.add("USB Device ATTACHED");
                    mAdapter.notifyDataSetChanged();
                }
            }

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null)
                {
                    // call your method that cleans up and closes communication with the device
                    mUsbPort.SetUSBConnectionFlag(false);
                    list.clear();
                    list.add("USB Device detached");
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void OnClickButtonSearchDevices( View v)
    {
        list.clear();
        list.add("USB Device List");
        ArrayList<String> usb_devices_list = mUsbPort.get_usb_devices_list();
        final int size = usb_devices_list.size();
        for (int i = 0; i < size; i++)
        {
            list.add(usb_devices_list.get(i));
        }

        list.add("Don't forget, please enter VendorID and ProductID into setting dialog");
        mAdapter.notifyDataSetChanged();
    }

    public void OnClickButtonConnectDevices( View v)
    {
        list.clear();


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String vendorIDstr = sharedPrefs.getString("printer_vendorId", "0");
        int vendorID = 0;
        try {
            vendorID = Integer.parseInt(vendorIDstr);
        } catch(NumberFormatException nfe) {
            vendorID = 0;
        }
        String productIDstr = sharedPrefs.getString("printer_productId","0");
        int productID = 0;
        try {
            productID = Integer.parseInt(productIDstr);
        } catch(NumberFormatException nfe) {
            productID = 0;
        }

        UsbDevice foundDevice = mUsbPort.search_device(vendorID,productID);
        if ( foundDevice == null) {
            list.add("USB Device vendorId=" + vendorIDstr + " productID=" + productIDstr + " not found");
            list.add("Try to search devices");
            list.add("Don't forget to enter the VendorID and ProductID into the settings dialog");
            mAdapter.notifyDataSetChanged();
            return;
        }
        else {
            list.add("Device found...");
        }
        try {
            if ( !this.mUsbManager.hasPermission(foundDevice) )
                list.add("Need Authification, please repeat...");
            this.mUsbManager.requestPermission(foundDevice, mPermissionIntent);


            if ( mUsbPort.connect_device(foundDevice)) {
                list.add("Device connected...");

            }
            else
                list.add("Device not connected...");

        }
        catch ( Exception  e )
        {
            list.clear();
            //list.add(e.getLocalizedMessage());
            list.add(e.getMessage());
            mAdapter.notifyDataSetChanged();
        }

        mAdapter.notifyDataSetChanged();

    }

    public void OnClickButtonSample( View v)
    {
        mEscPos.print_sample();
    }

    public void OnClickButtonSample1( View v)
    {
        mEscPos.print_sample1();
    }


    private void showUserSettings() {
/*
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append("\n Username: "
                + sharedPrefs.getString("prefUsername", "NULL"));

        builder.append("\n Send report:"
                + sharedPrefs.getBoolean("prefSendReport", false));

        builder.append("\n Sync Frequency: "
                + sharedPrefs.getString("prefSyncFrequency", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);

        settingsTextView.setText(builder.toString());*/
    }

}
