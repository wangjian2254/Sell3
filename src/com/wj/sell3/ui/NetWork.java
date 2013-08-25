package com.wj.sell3.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetWork
{
  private static final String TAG = "NetWork.class";

  public static int getConnectionType(Context paramContext)
  {
    if (isWifi(paramContext)){
    	return 1;
    }
    if(isNet()){
    	return 2;
    }
    return -1;
   
  }

  public static boolean isNet()
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)ChannelApplication.mContext.getSystemService("connectivity");
    if (localConnectivityManager == null)
      return false;
    NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()) || (localNetworkInfo.getState() != NetworkInfo.State.CONNECTED)){
    	return false;
    }
      return true;
  }

  public static boolean isWifi(Context paramContext)
  {
    int i = 0;
    WifiManager localWifiManager = (WifiManager)paramContext.getSystemService("wifi");
    if (localWifiManager == null)
    {
//      EveLog.e("NetWork.class", "NetWork-class error with wifiManager!");
      return false;
    }
    WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
    if (localWifiInfo == null);
    for (int j = 0; ; j = localWifiInfo.getIpAddress())
    {
      if ((localWifiManager.isWifiEnabled()) && (j != 0));
      i = 1;
      return true;
    }
  }

  public static abstract interface CONNECTION_TYPE
  {
    public static final int NET = 2;
    public static final int NO_NETWORK = -1;
    public static final int WIFI = 1;
  }
}
