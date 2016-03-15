package com.jaisingh.android.blueterm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothGatt gatt;
    @Bind(R.id.mac)
    TextView textMac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textMac.setText("Mac Address");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(scanFinished, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void scan(View view) {
        Toast.makeText(MainActivity.this, "Scanning", Toast.LENGTH_SHORT).show();
        if (BTAdapter.isDiscovering())
            {
                BTAdapter.cancelDiscovery();
            }
        BTAdapter = null;
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        BTAdapter.enable();
        BTAdapter.startDiscovery();
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                String macAddres = "00:73:E0:8C:6B:4A";
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                //device.createBond();
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                if (!address.equals( macAddres))
                {
                    Toast.makeText(MainActivity.this, rssi + " " + name+"\n$$"+address+"$$" , Toast.LENGTH_SHORT).show();
                    BTAdapter.startDiscovery();
                    return;
                }
                textMac.setText(rssi + " " + name + "\n$$" + address + "$$");
                Toast.makeText(MainActivity.this, "Scanning again", Toast.LENGTH_SHORT).show();
            }
            else
            BTAdapter.startDiscovery();

        }
    };
    private final BroadcastReceiver scanFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scan(findViewById(R.id.scan));
        }
    };
    private void stop() {
        BTAdapter.cancelDiscovery();
    }
}
