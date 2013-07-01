package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnConn, btnLedOn, btnLedOff, btnBeep;
	BluetoothSocket btSocket;
	BluetoothAdapter adapter;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address = "11:22:33:DD:EE:FF";
	private static final String TAG = "BLUEKEY_CLIENT";
	private static final Boolean D = true;
	private OutputStream outStream = null;  
    private InputStream inStream = null;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");
        btnConn = (Button)findViewById(R.id.btnConn);
        btnLedOn = (Button)findViewById(R.id.btnLedOn);
        btnLedOff = (Button)findViewById(R.id.btnLedOff);
        btnBeep = (Button)findViewById(R.id.btnBeep);
        
        btnConn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 提示文字
				Toast.makeText(MainActivity.this, "正在连接蓝牙设备...",Toast.LENGTH_LONG).show();
				
				// 蓝牙适配
				adapter = BluetoothAdapter.getDefaultAdapter();
				if (adapter == null){
					Toast.makeText(MainActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
					return;
				}
				adapter.isEnabled();
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, 0x1);
				//Toast.makeText(MainActivity.this, "通过系统设置中启用蓝牙将发出一个请求",Toast.LENGTH_LONG).show();
			
				Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);  
				discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			
				

			}
		});
        
        btnLedOn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 开始扫描蓝牙设备
				//adapter.startDiscovery();
		        BluetoothDevice device = adapter.getRemoteDevice(address);
		        Log.e(TAG, "device:"+device.getName());
		        Toast.makeText(MainActivity.this, "蓝牙设备连接成功"+device.getName()+"！",Toast.LENGTH_LONG).show();

		        try {
		            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
		        }


		        adapter.cancelDiscovery();
		        try {
		            btSocket.connect();
		            Log.e(TAG, "连接蓝牙成功");
		            Toast.makeText(MainActivity.this, "蓝牙建立连接！",Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		        	Log.e(TAG, "连接蓝牙失败", e);
		        	Toast.makeText(MainActivity.this, "蓝牙建立失败！",Toast.LENGTH_LONG).show();
		        	return;
		        }
		        try {
		            outStream = btSocket.getOutputStream();
		            inStream = btSocket.getInputStream();
		        } catch (IOException e) {
		            Log.e(TAG, "流创建失败", e);
		        }
		 
		        String message = "LED=ON\r\n";
		        System.out.println("message: " + message);
		        byte[] msgBuffer = message.getBytes();
		        try {
		            outStream.write(msgBuffer);
		            Toast.makeText(MainActivity.this, "向蓝牙串口发送指令："+msgBuffer,Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Exception during write.", e);
		        }
		        // 提示连接成功
				Toast.makeText(MainActivity.this, "指令已经送达"+device.getName()+"！",Toast.LENGTH_LONG).show();
	            try {
	                btSocket.close();
	            } catch (IOException e2) {
	                Log.e(TAG,
	                    "ON RESUME: Unable to close socket during connection failure", e2);
	                Toast.makeText(MainActivity.this, "未能关闭连接！",Toast.LENGTH_LONG).show();
	            }
			}
			
        });
        
        btnLedOff.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 开始扫描蓝牙设备
				//adapter.startDiscovery();
		        BluetoothDevice device = adapter.getRemoteDevice(address);
		        Log.e(TAG, "device:"+device.getName());
		        Toast.makeText(MainActivity.this, "蓝牙设备连接成功"+device.getName()+"！",Toast.LENGTH_LONG).show();

		        try {
		            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
		        }


		        adapter.cancelDiscovery();
		        try {
		            btSocket.connect();
		            Log.e(TAG, "连接蓝牙成功");
		            Toast.makeText(MainActivity.this, "蓝牙建立连接！",Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		        	Log.e(TAG, "连接蓝牙失败", e);
		        	Toast.makeText(MainActivity.this, "蓝牙建立失败！",Toast.LENGTH_LONG).show();
		        	return;
		        }
		        try {
		            outStream = btSocket.getOutputStream();
		            inStream = btSocket.getInputStream();
		        } catch (IOException e) {
		            Log.e(TAG, "流创建失败", e);
		        }
		 
		        String message = "LED=OFF\r\n";
		        System.out.println("message: " + message);
		        byte[] msgBuffer = message.getBytes();
		        try {
		            outStream.write(msgBuffer);
		            Toast.makeText(MainActivity.this, "向蓝牙串口发送指令："+msgBuffer,Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Exception during write.", e);
		        }
		        // 提示连接成功
				Toast.makeText(MainActivity.this, "指令已经送达"+device.getName()+"！",Toast.LENGTH_LONG).show();
	            try {
	                btSocket.close();
	            } catch (IOException e2) {
	                Log.e(TAG,
	                    "ON RESUME: Unable to close socket during connection failure", e2);
	                Toast.makeText(MainActivity.this, "未能关闭连接！",Toast.LENGTH_LONG).show();
	            }
			}
			
        });
        
        btnBeep.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 开始扫描蓝牙设备
				//adapter.startDiscovery();
		        BluetoothDevice device = adapter.getRemoteDevice(address);
		        Log.e(TAG, "device:"+device.getName());
		        Toast.makeText(MainActivity.this, "蓝牙设备连接成功"+device.getName()+"！",Toast.LENGTH_LONG).show();

		        try {
		            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
		        }


		        adapter.cancelDiscovery();
		        try {
		            btSocket.connect();
		            Log.e(TAG, "连接蓝牙成功");
		            Toast.makeText(MainActivity.this, "蓝牙建立连接！",Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		        	Log.e(TAG, "连接蓝牙失败", e);
		        	Toast.makeText(MainActivity.this, "蓝牙建立失败！",Toast.LENGTH_LONG).show();
		        	return;
		        }
		        try {
		            outStream = btSocket.getOutputStream();
		            inStream = btSocket.getInputStream();
		        } catch (IOException e) {
		            Log.e(TAG, "流创建失败", e);
		        }
		 
		        String message = "BEEP\r\n";
		        System.out.println("message: " + message);
		        byte[] msgBuffer = message.getBytes();
		        try {
		            outStream.write(msgBuffer);
		            Toast.makeText(MainActivity.this, "向蓝牙串口发送指令："+msgBuffer,Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		            Log.e(TAG, "ON RESUME: Exception during write.", e);
		        }
		        // 提示连接成功
				Toast.makeText(MainActivity.this, "指令已经送达"+device.getName()+"！",Toast.LENGTH_LONG).show();
	            try {
	                btSocket.close();
	            } catch (IOException e2) {
	                Log.e(TAG,
	                    "ON RESUME: Unable to close socket during connection failure", e2);
	                Toast.makeText(MainActivity.this, "未能关闭连接！",Toast.LENGTH_LONG).show();
	            }
			}
			
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
