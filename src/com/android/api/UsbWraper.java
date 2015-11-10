package com.android.api;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

public class UsbWraper
{
	UsbDeviceConnection connection;
	UsbDevice device;
	UsbEndpoint inEndpoint;
	UsbEndpoint outEndpoint;
	int pid;
	UsbInterface usbInterface;
	int vid;
}
