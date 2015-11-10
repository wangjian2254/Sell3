package com.android.api;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.wj.sell3.SellApplication;


public class UsbHelper
{
	private static final String ACTION_USB_PERMISSION = "com.android.api.USB_PERMISSION";
	public static UsbDevice OTGdevice = null;
	private static String TAG = "UsbHelper";
	private static final Object b;
	private static Hashtable<Integer, UsbWraper> deviceList;
	public static boolean hasPermission = false;
	private static Context mContext = null;
	private static final BroadcastReceiver mUsbReceiver;

	static
	{
		deviceList = new Hashtable();
		mUsbReceiver = new UsbHelper1();
		b = new Object();
		Application localApplication = getApplication();
		if (localApplication != null)
		{
			mContext = localApplication;
			IntentFilter localIntentFilter = new IntentFilter("com.android.api.USB_PERMISSION");
			mContext.registerReceiver(mUsbReceiver, localIntentFilter);
		}
	}

	private static UsbWraper doOpenDevice(int paramInt1, int paramInt2) {
		Log.e(TAG, "doOpenDevice [vid=" + paramInt2 + ",pid=" + paramInt1 + "]");

		if (mContext == null) {
			Log.e(TAG, "error: mContext == null");
		}
		while ((getDevicePermission(paramInt1, paramInt2) != 0) || (OTGdevice == null)) {
			return null;
		}

		UsbManager usbManager = (UsbManager) mContext.getSystemService("usb");
		int i = paramInt1 << paramInt2 + 16;
		
		UsbWraper usbWraper = (UsbWraper) deviceList.get(Integer.valueOf(i));
		Log.e(TAG, "close old usbWraper: " + usbWraper);
		// UsbWraper usbWraper = (UsbWraper) deviceList.get(0);
		if (usbWraper != null) {
			deviceList.remove(usbWraper);
			usbWraper.connection.close();
			usbWraper.connection.releaseInterface(usbWraper.usbInterface);
			usbWraper = null;
		}

		UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(OTGdevice);
		if (null == usbDeviceConnection) {
			Log.e(TAG, "usbDeviceConnection is null ");
			return null;
		}
		Log.e(TAG, "TGdevice.getInterfaceCount(): " + OTGdevice.getInterfaceCount());

		UsbInterface usbInterface = OTGdevice.getInterface(0);
		usbDeviceConnection.claimInterface(usbInterface, true);

		usbWraper = new UsbWraper();
		usbWraper.connection = usbDeviceConnection;
		usbWraper.usbInterface = usbInterface;
		usbWraper.device = OTGdevice;
		usbWraper.pid = paramInt1;
		usbWraper.vid = paramInt2;

		Log.e(TAG, "usbInterface.getEndpointCount(): " + usbInterface.getEndpointCount());
		for (int j = 0; j < usbInterface.getEndpointCount(); j++) {
			UsbEndpoint usbEndpoint = usbInterface.getEndpoint(j);
			if (usbEndpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
				if (usbEndpoint.getDirection() == UsbConstants.USB_DIR_IN) {
					usbWraper.inEndpoint = usbEndpoint;
				} else if (usbEndpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
					usbWraper.outEndpoint = usbEndpoint;
				}
			}
		}
		
		deviceList.put(Integer.valueOf(i), usbWraper);
		// deviceList.put(0, usbWraper);
		Log.e(TAG, "doOpenDevice ok!");
		return usbWraper;
	}

	private static Application getApplication()
	{
		return SellApplication.getInstance();
	}

	public static int getDevicePermission(int paramInt1, int paramInt2) {
		Log.e(TAG, "getDevicePermission: [vid=" + paramInt2 + ",pid=" + paramInt1 + "]");
		if (mContext == null) {
			Log.e(TAG, "context get failed");
			return -1;
		}

		UsbManager usbManager = (UsbManager) mContext.getSystemService("usb");
		Iterator iterator = usbManager.getDeviceList().values().iterator();

		while (iterator.hasNext()) {
			UsbDevice usbdevice = (UsbDevice) iterator.next();
			Log.e(TAG, usbdevice.getDeviceName() + " "
					+ Integer.toHexString(usbdevice.getVendorId()) + " "
					+ Integer.toHexString(usbdevice.getProductId()));
			if ((usbdevice.getVendorId() == paramInt2) && (usbdevice.getProductId() == paramInt1)) {
				OTGdevice = usbdevice;
				if (usbManager.hasPermission(OTGdevice)) {
					hasPermission = true;
					Log.e(TAG, "has permission already...");
					return 0;
				}
			}
		}

		if (null == OTGdevice) {
			return -1;
		}
		Log.e(TAG, "try to get permission...");
		PendingIntent lpi = PendingIntent.getBroadcast(mContext, 0, new Intent("com.android.api.USB_PERMISSION"), 0);
		usbManager.requestPermission(OTGdevice, lpi);

		for (int i = 300; i >= 0; i--) {
			if (usbManager.hasPermission(OTGdevice)) {
				break;
			}
			try {
				Thread.sleep(200L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		hasPermission = true;
		return 0;
	}

	public static int isOTGDevice(int paramInt1, int paramInt2) {
		Log.e(TAG, "isOTGDevice: vid:" + paramInt2 + ",pid=" + paramInt1);
		if (mContext == null) {
			Log.e(TAG, "context get failed[vid=" + paramInt2 + ",pid=" + paramInt1 + "]");
			return -1;
		}

		HashMap ds = ((UsbManager) mContext.getSystemService("usb")).getDeviceList();
		Log.e(TAG, "getDeviceList: " + ds);

		if (ds.size() == 0) {
			return -1;
		}
		Iterator iterator = ds.values().iterator();
		while (iterator.hasNext()) {
			UsbDevice d = (UsbDevice) iterator.next();
			Log.e(TAG, "device: " + d.getDeviceName() + " vid: " + Integer.toHexString(d.getVendorId()) + " pid: "
					+ Integer.toHexString(d.getProductId()));
		}
		return 0;
	}

	public static int openDevice(int paramInt1, int paramInt2)
	{
		Log.e(TAG, "java openDevice: " + paramInt1 + " , " + paramInt2);
		if (doOpenDevice(paramInt1, paramInt2) == null) {
			return 0;
		}
		return 1;
	}

	public static int readData(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
	{
		int i = paramInt1 << paramInt2 + 16;
		UsbWraper localUsbWraper = (UsbWraper) deviceList.get(Integer.valueOf(i));
		if (localUsbWraper == null) {
			localUsbWraper = doOpenDevice(paramInt1, paramInt2);
		}
		if (localUsbWraper != null)
		{
			int j = localUsbWraper.connection
					.bulkTransfer(localUsbWraper.inEndpoint, paramArrayOfByte, paramInt3, 3000);
			StringBuilder localStringBuilder = new StringBuilder();
			for (int k = 0;; k++)
			{
				if (k >= j)
				{
					Log.i(TAG, "Read Data From IDCard:");
					Log.i(TAG, localStringBuilder.toString());
					return j;
				}
				if ((k != 0) && (k % 16 == 0)) {
					localStringBuilder.append("\n");
				}
				String str = Integer.toHexString(0xFF & paramArrayOfByte[k]);
				if (str.length() < 2) {
					str = "0" + str;
				}
				localStringBuilder.append(str + " ");
			}
		}
		return -1;
	}

	public static int writeData(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
	{
		StringBuilder localStringBuilder = new StringBuilder();
		System.out.println("paramInt1: " + paramInt1 + " paramInt2:" + paramInt2 + "   " + paramInt3);

		for (int i = 0;; i++)		{
			if (i >= paramInt3)		{
				Log.i(TAG, "Write Data to IDCard:");
				Log.i(TAG, localStringBuilder.toString());
				int j = paramInt1 << paramInt2 + 16;
				//UsbWraper localUsbWraper = (UsbWraper) deviceList.get(Integer.valueOf(0));
				UsbWraper localUsbWraper = (UsbWraper) deviceList.get(Integer.valueOf(j));
				if (localUsbWraper == null) {
					localUsbWraper = doOpenDevice(paramInt1, paramInt2);
				}
				if (localUsbWraper == null) {
					break;
				}
				return localUsbWraper.connection.bulkTransfer(localUsbWraper.outEndpoint, paramArrayOfByte, paramInt3,
						3000);
			}
			if ((i != 0) && (i % 16 == 0)) {
				localStringBuilder.append("\n");
			}
			String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
			if (str.length() < 2) {
				str = "0" + str;
			}
			localStringBuilder.append(str + " ");
		}
		return -1;
	}

	public static class UsbHelper1 extends BroadcastReceiver
	{
		public void onReceive(Context paramContext, Intent paramIntent)
		{
			if ("com.android.api.USB_PERMISSION".equals(paramIntent.getAction())) {
				Log.d("UsbHelper1", "come into mUsbReceiver");
			}
			try
			{
				UsbAccessory localUsbAccessory = (UsbAccessory) paramIntent.getParcelableExtra("accessory");
				if (!paramIntent.getBooleanExtra("permission", false)) {
					Log.d("UsbHelper1", "permission denied for accessory " + localUsbAccessory);
				}
				return;
			} finally {
			}
		}
	}
}
