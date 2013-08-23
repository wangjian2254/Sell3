package com.wj.sell3.ui;

import android.app.Application;
import android.content.Context;
import java.util.HashMap;
import org.apache.http.cookie.Cookie;

public class ChannelApplication extends Application
{
  public static boolean DEBUG;
  public static boolean OPEN_LOG = true;
  public static HashMap<String, Cookie> cookies;
  public static boolean loadImage;
  public static String localImageURL;
  public static String localURL;
  public static Context mContext;
  public static String remoteImageURL;
  public static String remoteURL;

  static
  {
    DEBUG = false;
    localURL = "";
    localImageURL = "";
    remoteURL = "";
    remoteImageURL = "";
    loadImage = true;
    cookies = new HashMap();
  }

  public void onCreate()
  {
    super.onCreate();
    mContext = getApplicationContext();
  }

  @Deprecated
  public static abstract interface COOKIE
  {
    public static final String CART = "CMCCEBIZ";
    public static final String USER = "ailkage-linkage";
  }
}