package com.poe.lewen;

import com.poe.lewen.util.HttpUtil;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Activity_Login extends BaseActivity {

	private Button back;
	private ImageView img_switch;
	private boolean SAVE_STATE =false;
	private ImageButton login;
	private EditText edit_user,edit_passwd;
	
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
		login			=	(ImageButton) findViewById(R.id.btn_loginOfLogin);
		edit_user	=	(EditText) findViewById(R.id.editUserNameOfLogin);
		edit_passwd	=	(EditText) findViewById(R.id.editPasswdOfLogin);
		
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
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//1.验证用户名不为空
				if(TextUtils.isEmpty(edit_user.getText().toString())){
					
					MyApplication.getInstance().throwTips("请输入用户名！");
					
				}else		if(TextUtils.isEmpty(edit_passwd.getText().toString())){
						//2.密码不为空
					MyApplication.getInstance().throwTips("请输入密码！");
					
				}else if(HttpUtil.checkNet(MyApplication.getInstance())){
					new loginTask().execute();
				}
			}
		});
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	class loginTask extends AsyncTask<Void, integer, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.e("poe", "doInBackground loging...");
			HttpUtil.login(edit_user.getText().toString(), edit_passwd.getText().toString());
			return null;
		}
		
	}
}
