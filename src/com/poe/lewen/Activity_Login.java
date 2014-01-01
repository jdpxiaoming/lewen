package com.poe.lewen;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Activity_Login extends BaseActivity {

	private Button back;
	private ImageView img_switch;
	private boolean SAVE_STATE =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_login);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarLogin);
		img_switch	=	(ImageView) findViewById(R.id.imgSwitchOfLogin);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		
		img_switch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SAVE_STATE = !SAVE_STATE;
				
				if(SAVE_STATE){
					img_switch.setImageResource(R.drawable.icon_login_switch_on);
				}else{
					img_switch.setImageResource(R.drawable.icon_login_switch_off);
				}
			}
		});
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

}
