package com.example.jaisingh.bluelink;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Handler;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jaisingh on 14/2/16.
 */
public class BluetoothService extends IntentService
{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Handler myHandler;
    BluetoothDevice myDevice;
    BluetoothAdapter adptr;
    BluetoothSocket mySocket;
    private InputStream myInputStream;
    private OutputStream myOutputStream;
    public BluetoothService() {
        super("Bluetooth_Service");
        myHandler = new Handler();
        myHandler.post(new DisplayToast(this, "Hello World!"));
        adptr = BluetoothAdapter.getDefaultAdapter();

    }
    private void Toaster(String s)
    {
        myHandler.post(new DisplayToast(this, s));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(BluetoothService.this, "Creation Time", Toast.LENGTH_SHORT).show();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "Service started....", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service stopped...", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Intent it = new Intent(this, BluetoothService.class);
        startService(it);
    }
    private void setDevice() {
        //startListening();
        if(!adptr.isEnabled())
        {
            adptr.enable();
        }
        Set pairedDevices = adptr.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(Object device : pairedDevices)
            {
                BluetoothDevice bDevice = (BluetoothDevice)device;
                if(bDevice.getName().equals("HC-05")) //change this to match the name of your device
                {
                    Toaster("Found HC-05 in paired devices");
                    myDevice = bDevice;
                    return;
                }
            }
        }
        Toaster("Device HC-05  not found in paired devices");
    }
    public void connect() {

        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            mySocket = myDevice.createRfcommSocketToServiceRecord(uuid);
            mySocket.connect();
            myOutputStream = mySocket.getOutputStream();
            //mmInputStream = mmSocket.getInputStream();
            //Toast.makeText(getApplicationContext(), "connected with "+myDevice.getName(), Toast.LENGTH_SHORT).show();
            myHandler.post(new DisplayToast(this, "connected with "+myDevice.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            myHandler.post(new DisplayToast(this, e.toString()));

        }
    }
    public void message(String s)
    {
        try {
            myOutputStream.write(s.getBytes());
            myHandler.post(new DisplayToast(this,"Sent:" + s));

        } catch (Exception e) {
            e.printStackTrace();
            myHandler.post(new DisplayToast(this, "Cannot send message: "+ e.toString()));
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                setDevice();
                connect();
                wait(1000);
                message(":)\n");
                myOutputStream.close();
                mySocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
