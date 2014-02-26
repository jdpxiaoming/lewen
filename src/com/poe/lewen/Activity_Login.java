package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.util.HttpUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Login extends Activity {

	private Button back,btn_url_set;
	private ImageView img_switch;
	private boolean SAVE_STATE =false;
	private ImageButton login;
	private EditText edit_user,edit_passwd;
	private SharedPreferences sharedPreferences;
	private LinearLayout progress;
	private Handler handler ;
	
	//show demo
	private TextView text_show;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_login);
		init();
	}

	public void init() {
		
		sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		progress = (LinearLayout) findViewById(R.id.progressbarOfLogin);
		progress.setVisibility(View.GONE);
		
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarLogin);
		img_switch	=	(ImageView) findViewById(R.id.imgSwitchOfLogin);
		login			=	(ImageButton) findViewById(R.id.btn_loginOfLogin);
		edit_user	=	(EditText) findViewById(R.id.editUserNameOfLogin);
		edit_passwd	=	(EditText) findViewById(R.id.editPasswdOfLogin);
		btn_url_set	=	(Button) findViewById(R.id.rightButtonOfToperBarLogin);
		
		edit_user.setText(sharedPreferences.getString("username", ""));
		edit_passwd.setText(sharedPreferences.getString("password", ""));
		
		
		text_show 	=	(TextView) findViewById(R.id.text_show_displayOfLogin);
		text_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Activity_Login.this,Activity_Home.class));
				finish();
			}
		});
		
//		edit_user.setText("SuperAdmin");
//		edit_passwd.setText("123456");
//		edit_user.setText("cxm");//备用账号：cxm
//		edit_passwd.setText("111111");
		
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
		
		btn_url_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(Activity_Login.this,SystemUrlSet.class));
				
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
					
					if(MyApplication.rsp_login!=null){
						MyApplication.getInstance().throwTips("您已经登录了！");
						startActivity(new Intent(Activity_Login.this,Activity_Home.class));
						finish();
					}else{
						progress.setVisibility(View.VISIBLE);
						MyApplication.packet.login(username, password,handler);
					}
				}
			}
		});
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				//接受登录返回数据
				progress.setVisibility(View.GONE);
				
				String result_login =(String) msg.obj;
				try {
					MyApplication.rsp_login = XmlToListService.GetLogin(result_login);
					if(MyApplication.rsp_login!=null){
						System.out.println(MyApplication.rsp_login.getUserId());
						if(MyApplication.rsp_login.getUserId()!=null&&MyApplication.rsp_login.getUserId().length()>1){
//							MyApplication.getInstance().throwTips("登录成功，自动跳转到播放列表页面！");
//							TcpUtil.startPolling();
							startActivity(new Intent(Activity_Login.this,Activity_Home.class));
							finish();
						}else{
							System.out.println("登录错误，rsp_login滞空："+MyApplication.rsp_login.getErr());
							MyApplication.getInstance().throwTips(MyApplication.rsp_login.getErr());
							MyApplication.rsp_login = null;
//							MyApplication.packet.close();
						}
					}else{
						MyApplication.getInstance().throwTips("登录失败！");
						MyApplication.rsp_login = null;
//						Packet.close();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		
		//如果首次登陆 进入 登陆界面 ，否则 直接结束当前页面
		 if(MyApplication.getPreferenceData("first")==null){
			 MyApplication.pushPreferenceData("first", "yes");
				startActivity(new Intent(Activity_Login.this,HelpShowImageActivity.class));
		 }
	}
	
}
