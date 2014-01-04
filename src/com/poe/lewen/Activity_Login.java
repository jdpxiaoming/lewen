package com.poe.lewen;

import java.util.ArrayList;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_Login;
import com.mm.android.avnetsdk.param.AV_OUT_Login;
import com.mm.android.avnetsdk.param.ConnectStatusListener;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.util.HttpUtil;
import com.poe.lewen.util.Tool;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils; 
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
	private boolean netSDKIsInit = false; // NetSDK是否初始化成功标志
	private  AV_HANDLE log_handle = null; // 登陆句柄
	private AV_IN_Login refInParam = null; // 登陆输入参数
	private AV_OUT_Login refOutParam = null; // 登陆输出参数
	
	private Activity mActivity;
	private ArrayAdapter<String> spinnerAdapter;	
	private int error = 0;
	private int mChannelCount=0;	//连接设备的通道数
	private ArrayList<String> mChannelList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_login);
		init();
	}

	public void init() {
		
		mActivity = this;
		
		netSDKIsInit = AVNetSDK.AV_Startup(mActivity
				.getPackageName());						//初始化网络SDK
		sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarLogin);
		img_switch	=	(ImageView) findViewById(R.id.imgSwitchOfLogin);
		login			=	(ImageButton) findViewById(R.id.btn_loginOfLogin);
		edit_user	=	(EditText) findViewById(R.id.editUserNameOfLogin);
		edit_passwd	=	(EditText) findViewById(R.id.editPasswdOfLogin);
		
		edit_user.setText(sharedPreferences.getString("username", ""));
		edit_passwd.setText(sharedPreferences.getString("password", ""));
		
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

	
	/**
	 * 知心登录请求
	 * @author poe.Cai
	 *
	 */
	class loginTask extends AsyncTask<Void, integer, String>{

		@Override
		protected void onPreExecute() {
			String username = edit_user.getText().toString();
			String password = edit_passwd.getText().toString();

			if(SAVE_STATE){
				Editor editor = sharedPreferences.edit();// 获取编辑器
//			editor.putString("ip", Constant.str_login_ip);
//			editor.putString("port",Constant.login_port+"");
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();// 提交修改
			}
			//构造登陆输入参数
			refInParam = new AV_IN_Login();
			refInParam.strDevIP = Constant.str_login_ip;
			refInParam.nDevPort = Constant.login_port;
			refInParam.strUsername = username;
			refInParam.strPassword = password;
			refInParam.bReconnect = false;
			refInParam.connStatusListener = new ConnectStatusListener() {

				@Override
				public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
						String arg2, int arg3, Object arg4) {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			//构造登陆输出参数
			refOutParam = new AV_OUT_Login();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.e("poe", "doInBackground loging...");
//			HttpUtil.login(edit_user.getText().toString(), edit_passwd.getText().toString());
			if (netSDKIsInit) { // 如果NetSDK初始化成功才登陆
				log_handle = AVNetSDK.AV_Login(refInParam, refOutParam);// 登录失败返回null，调用AV_GetLastError来获取具体的错误信息
				Log.d("jhe", refOutParam.strDeviceType+":"+refOutParam.nAnalogChnNum+":"+refOutParam.nChannelCount+":"+refOutParam.nDigitalChnNum+":"+refOutParam.nProtocolVersion);
				mChannelCount=refOutParam.nChannelCount;
				for(int i =0;i<mChannelCount;i++)
				{
					String channel = "通道 " +String.format("%02d", i+1);
					mChannelList.add(i,channel);
					
				}
				spinnerAdapter=new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, mChannelList);
				spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//spinner.setSelection(0);
				
				return "success";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(null!=result){
				Tool.showMsg(mActivity, "登录成功，跳转到Video播放页面！");
				
				startActivity(new Intent(mActivity,Activity_Video.class));
			}
		}
		
	}
}
