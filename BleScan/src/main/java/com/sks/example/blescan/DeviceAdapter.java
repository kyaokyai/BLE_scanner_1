package com.sks.example.blescan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sks.example.util.ScannedDevice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeviceAdapter extends ArrayAdapter<ScannedDevice> {
    private static final String PREFIX_RSSI = "RSSI:";
    private List<ScannedDevice> mList;
    private LayoutInflater mInflater;
    private int mResId;
    public static final String BEACON_ADDRESS_1 = "DC:A6:32:CC:1A:1D"; //指定のビーコン1
    public static final String BEACON_ADDRESS_2 = "DC:A6:32:71:2D:B9"; //指定のビーコン2
    public static final String BEACON_ADDRESS_3 = "B8:27:EB:85:20:32"; //指定のビーコン3
    public static final String BEACON_ADDRESS_4 = "DC:A6:32:CB:C9:59"; //指定のビーコン4

    public static double BEACON_DISTANCE_1 = 0; //指定のビーコン1
    public static double BEACON_DISTANCE_2 = 0; //指定のビーコン2
    public static double BEACON_DISTANCE_3 = 0; //指定のビーコン3
    public static double BEACON_DISTANCE_4 = 0; //指定のビーコン4

    public DeviceAdapter(Context context, int resId, List<ScannedDevice> objects) {
        super(context, resId, objects);
        mResId = resId;
        mList = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScannedDevice item = (ScannedDevice) getItem(position);
         double d = 0;


        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        name.setText(item.getDisplayName());
        TextView address = (TextView) convertView.findViewById(R.id.device_address);
        address.setText(item.getDevice().getAddress());
        TextView rssi = (TextView) convertView.findViewById(R.id.device_rssi);
        rssi.setText(PREFIX_RSSI + Integer.toString(item.getRssi()));
        //Returns the transmission power level of the packet in dBm
        TextView txPower = (TextView)convertView.findViewById(R.id.device_tx_power);


        if(item.getDevice().getAddress()!=null) {
            TextView scantime = (TextView) convertView.findViewById(R.id.scan_time); //リストのスペース確保
            final DateFormat df = new SimpleDateFormat("HH:mm:ss");           //時刻を受け取るフォーマットの決定
            final Date date = new Date(System.currentTimeMillis());
            scantime.setText(df.format(date));                                       //システムから時刻を取得
        }

        //Log.d("ECRT", "TxPower: "+item.getTxPower());
        d = Math.pow(10.0, (-53.615 - item.getRssi()) / 14.89);  //RSSIでの距離計算


        txPower.setText("距離: " +(Math.floor(d*100))/100 +"m"); //小数点第二位切り捨て
        //txPower.setText("距離: " +d);                           //切り捨て無し


        if(BEACON_ADDRESS_1.equals(item.getDevice().getAddress())) {
            // add new BluetoothDevice
            BEACON_DISTANCE_1 = d;
        }

        if(BEACON_ADDRESS_2.equals(item.getDevice().getAddress())) {
            // add new BluetoothDevice
            BEACON_DISTANCE_2 = d;
        }

        if(BEACON_ADDRESS_3.equals(item.getDevice().getAddress())) {
            // add new BluetoothDevice
            BEACON_DISTANCE_3 = d;
        }

        if(BEACON_ADDRESS_4.equals(item.getDevice().getAddress())) {
            // add new BluetoothDevice
            BEACON_DISTANCE_4 = d;
            TextView omake = (TextView) convertView.findViewById(R.id.omake);
            //omake.setText("hello");
        }







        return convertView;
    }

    /** add or update BluetoothDevice */
    public void update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return;
        }

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                device.setRssi(rssi); // update
                break;
            }
        }
        if (!contains) {
            if(BEACON_ADDRESS_1.equals(newDevice.getAddress())) {
                // add new BluetoothDevice

                mList.add(new ScannedDevice(newDevice, rssi));
            }
        }
        notifyDataSetChanged();
    }

    /** add or update BluetoothDevice */
    public void update(BluetoothDevice newDevice, int rssi, ScanRecord scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return;
        }

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                device.setRssi(rssi); // update
                Log.d("ECRT", "TxPower2: "+scanRecord.getTxPowerLevel());
                device.setTxPower(scanRecord.getTxPowerLevel());
                break;
            }
        }
        if (!contains) {
            //if(BEACON_ADDRESS_1.equals(newDevice.getAddress())) {
                // add new BluetoothDevice
            //  mList.add(new ScannedDevice(newDevice, rssi));
          // }

          // if(BEACON_ADDRESS_2.equals(newDevice.getAddress())) {
           //     // add new BluetoothDevice
            //   mList.add(new ScannedDevice(newDevice, rssi));
           // }

            if(BEACON_ADDRESS_4.equals(newDevice.getAddress())||BEACON_ADDRESS_2.equals(newDevice.getAddress())||BEACON_ADDRESS_1.equals(newDevice.getAddress())||BEACON_ADDRESS_3.equals(newDevice.getAddress())) {
                // add new BluetoothDevice
                mList.add(new ScannedDevice(newDevice, rssi));
            }
        }
        notifyDataSetChanged();
    }
}
