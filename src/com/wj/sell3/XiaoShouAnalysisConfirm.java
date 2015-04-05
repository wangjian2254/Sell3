package com.wj.sell3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wj.sell.db.models.UserInfo;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.ImageViewWithBorder;
import com.wj.sell3.ui.TitleBar;

public class XiaoShouAnalysisConfirm extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	AlertDialogCustom localAlertDialogCustom;
	
	public ProgressDialog myDialog = null;
	TitleBar titleBar;
	ImageViewWithBorder image;
	TextView nameText;
	TextView sexText;
	TextView birthdayText;
	TextView addressText;
	TextView cardNoTextView;
	String tel;
	String name;
	String number;
	String address;
	String photo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        tel=bunde.getString("tel");
        name=bunde.getString("name");
        number=bunde.getString("number");
        address=bunde.getString("address");
        photo=bunde.getString("photo");
        user=SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
        setContentView(R.layout.real_name_registration_confirm);
        
        
    		 this.titleBar = ((TitleBar)findViewById(R.id.titlebar));
    		    this.titleBar.setTitle(R.string.confirm_title);
    		    this.titleBar.setBackListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					finish();
    				}
    			});
    		    this.titleBar.setUp();
	    this.image = ((ImageViewWithBorder)findViewById(R.id.image));
	    this.image.setBorderColor(Color.BLUE);
	    this.image.setBorderWidth(1);
	    this.image.setImageBitmap(stringtoBitmap(this.photo));
	    this.nameText = ((TextView)findViewById(R.id.name));
	    this.nameText.setText(this.name);
	    this.sexText= ((TextView)findViewById(R.id.sex));
	    if(Integer.valueOf(this.number.substring(16, 17))%2==0){
	    	this.sexText.setText("女");
	    }else{
	    	this.sexText.setText("男");
	    	
	    }
	    this.birthdayText = ((TextView)findViewById(R.id.birthday));
	    this.birthdayText.setText(this.number.substring(6, 14));
	    this.addressText = ((TextView)findViewById(R.id.address));
	    this.addressText.setText(this.address);
	    this.cardNoTextView = ((TextView)findViewById(R.id.card_no));
	    this.cardNoTextView.setText(this.number);
    }
    
    
    public Bitmap stringtoBitmap(String paramString)
    {
    	Bitmap localObject = null;
      try
      {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        localObject = localBitmap;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      return localObject;
    }
    
    public void successResult(View view){
    	Intent mainIntent = new Intent(this,XiaoShouAnalysis2.class);
    	Bundle extras=new Bundle();
    	extras.putString("tel", tel);
    	extras.putString("name", name);
    	extras.putString("number", number);
    	extras.putString("address", address);
//    	extras.putString("photo", photo);
    	mainIntent.putExtras(extras);
    	con.startActivity(mainIntent);
    	finish();
    }
    
    public void queryCancel(View view){
    	finish();
    }
    

	
    
    
    public void onResume(){
    	super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}