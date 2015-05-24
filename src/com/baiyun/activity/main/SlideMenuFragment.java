package com.baiyun.activity.main;

import com.baiyun.activity.R;
import com.baiyun.custom.CircleImageView;
import com.baiyun.http.HttpURL;
import com.baiyun.sharepreferences.UserInfoSP;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideMenuFragment extends Fragment{
	public static final int MENU_INFO = 1;//用户信息
	public static final int MENU_LOGIN = 2;//用户登陆
	public static final int MENU_TOOLS = 3;//实用工具
	public static final int MENU_SETTING = 4;//设置
	public static final int MENU_HELP = 5;//使用帮助
	public static final int MENU_ABOUT = 6;//关于我们
	public static final int MENU_EXIT = 7;//退出
	private View rootView;
	
	private CircleImageView cvHeader;
	private TextView tvName;
	
	public OnSlideMenuFragmentEventListener onSlideMenuFragmentEventListener;
	public void setOnSlideMenuFragmentEventListener(
			OnSlideMenuFragmentEventListener onSlideMenuFragmentEventListener) {
		this.onSlideMenuFragmentEventListener = onSlideMenuFragmentEventListener;
	}
	public interface OnSlideMenuFragmentEventListener{
		public void onSlideMenuFragmentEvent(int menuType);
	};
	
	public static SlideMenuFragment newInstance() {
		return new SlideMenuFragment();
	}
	
	public SlideMenuFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_slide_menu, container, false);
		
		//头像
		cvHeader = (CircleImageView)rootView.findViewById(R.id.cv_header);
//		String headerPathLast = UserInfoSP.getSingleInstance(getActivity()).getImg();
//		if (!TextUtils.isEmpty(headerPathLast)) {
//			String picUrl = HttpURL.HOST+headerPathLast.substring(1);
//			System.out.println("====> picUrl = "+picUrl);
//			ImageLoader.getInstance().displayImage(picUrl, cvHeader);
//		}
		
		//名字
		tvName = (TextView)rootView.findViewById(R.id.tv_name);
//		String name = UserInfoSP.getSingleInstance(getActivity()).getRealName();
//		if (!TextUtils.isEmpty(name)) {
//			tvName.setText(name);
//		}
		
		LinearLayout llInfo = (LinearLayout)rootView.findViewById(R.id.ll_info);
		llInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_INFO);
			}
		});
		
		RelativeLayout rlLogin = (RelativeLayout)rootView.findViewById(R.id.rl_login);
		rlLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_LOGIN);
			}
		});
		
		RelativeLayout rlTools = (RelativeLayout)rootView.findViewById(R.id.rl_tools);
		rlTools.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_TOOLS);
			}
		});
		
		RelativeLayout rlSetting = (RelativeLayout)rootView.findViewById(R.id.rl_setting);
		rlSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_SETTING);
			}
		});
		
		RelativeLayout rlHelp = (RelativeLayout)rootView.findViewById(R.id.rl_help);
		rlHelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_HELP);
			}
		});
		
		RelativeLayout rlAbout = (RelativeLayout)rootView.findViewById(R.id.rl_about);
		rlAbout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_ABOUT);
			}
		});
		
		RelativeLayout rlExit = (RelativeLayout)rootView.findViewById(R.id.rl_exit);
		rlExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSlideMenuFragmentEventListener.onSlideMenuFragmentEvent(MENU_EXIT);
			}
		});
		
		
		return rootView;
	}
	
	
}
