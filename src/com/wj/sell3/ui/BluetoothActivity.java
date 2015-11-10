package com.wj.sell3.ui;

/**
 * Created by fanjunwei003 on 15/4/5.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.wj.sell3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends Activity {
    private BluetoothAdapter bluetoothAdapter;
    List bluetoothDevices;
    private ArrayList listItem;
    private SimpleAdapter listItemAdapter;
    private ListView listView_btAddr;
    private OnClickListener myClickListener;
    private OnItemClickListener myListItemClickListener;

    private void bundleID() {
        this.listView_btAddr = ((ListView) findViewById(R.id.btAdrr_list));
        this.listView_btAddr.setOnItemClickListener(this.myListItemClickListener);
    }

    private void getBundledBluetoothDevice() {
        if (this.bluetoothAdapter == null)
            Toast.makeText(this, "此设备没有蓝牙功能", Toast.LENGTH_SHORT).show();


        if (!this.bluetoothAdapter.isEnabled()) {
            startActivity(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"));
            return;
        }
        Set localSet = this.bluetoothAdapter.getBondedDevices();
        if (localSet.size() > 0) {
            Iterator localIterator = localSet.iterator();
            while (localIterator.hasNext()) {
                BluetoothDevice localBluetoothDevice = (BluetoothDevice) localIterator.next();
                HashMap localHashMap = new HashMap();
                localHashMap.put("bluetoothName", localBluetoothDevice.getName());
                localHashMap.put("bluetoothAddr", localBluetoothDevice.getAddress());
                this.listItem.add(localHashMap);
                this.listItemAdapter.notifyDataSetChanged();
            }
        }

    }

    private void initListView() {
        this.listItemAdapter = new SimpleAdapter(this, this.listItem, R.layout.list_style_bluetooth, new String[]{"bluetoothName", "bluetoothAddr"}, new int[]{R.id.bluetoothName, R.id.bluetoothAddr});
        this.listView_btAddr.setAdapter(this.listItemAdapter);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.myListItemClickListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                Intent localIntent = new Intent();
                localIntent.putExtra("addr", ((HashMap) BluetoothActivity.this.listItem.get((int) paramAnonymousLong)).get("bluetoothAddr").toString());
                BluetoothActivity.this.setResult(-1, localIntent);
                BluetoothActivity.this.finish();
            }
        };
        this.listItem = new ArrayList();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_bluetooth);
        bundleID();
        initListView();
        getBundledBluetoothDevice();
    }
}
