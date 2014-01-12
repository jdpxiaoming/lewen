package com.poe.lewen;

import com.poe.lewen.util.HttpUtil;
import com.poe.lewen.util.Packet;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Activity_Login extends Activity {

	private Button back;
	private ImageView img_switch;
	private boolean SAVE_STATE =false;
	private ImageButton login;
	private EditText edit_user,edit_passwd;
	private SharedPreferences sharedPreferences;
	private Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_login);
		init();
	}

	public void init() {
		
		mActivity = this;
		sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarLogin);
		img_switch	=	(ImageView) findViewById(R.id.imgSwitchOfLogin);
		login			=	(ImageButton) findViewById(R.id.btn_loginOfLogin);
		edit_user	=	(EditText) findViewById(R.id.editUserNameOfLogin);
		edit_passwd	=	(EditText) findViewById(R.id.editPasswdOfLogin);
		
		edit_user.setText(sharedPreferences.getString("username", ""));
		edit_passwd.setText(sharedPreferences.getString("password", ""));
		
		edit_user.setText("SuperAdmin");
		edit_passwd.setText("123456");
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					final String username = edit_user.getText().toString();
					final String password = edit_passwd.getText().toString();
					
					if(SAVE_STATE){
						//save state
						Editor editor = sharedPreferences.edit();// 获取编辑器
						editor.putString("username", username);
						editor.putString("password", password);
						editor.commit();// 提交修改
					}
					
//					MyApplication.getInstance().reLogin(username, password, new loaded4login() {
//						
//						@Override
//						public void done() {
//							startActivity(new Intent(mActivity,Activity_Video.class));
//						}
//					});
					
					
					if(MyApplication.rsp_login!=null){
						MyApplication.getInstance().throwTips("您已经登录了！");
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								Packet.getPlayingList(MyApplication.rsp_login.getUserId());
							}
						}).start();
						
					}else{
						Packet.login(username, password);
					}
				}
			}
		});
	}
	
	
}
