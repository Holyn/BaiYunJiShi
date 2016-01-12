package com.baiyun.activity.main;

import com.baiyun.activity.MyApplication;
import com.baiyun.activity.R;
import com.baiyun.baidu_push.BaiduPushManager;
import com.baiyun.base.BaseSlidingFragmentActivity;
import com.baiyun.custom.DialogFactory;
import com.baiyun.fragment.sliding.AboutFragment;
import com.baiyun.fragment.sliding.HelpFragment;
import com.baiyun.fragment.sliding.LoginFragment;
import com.baiyun.fragment.sliding.SettingFragment;
import com.baiyun.fragment.sliding.ToolsFragment;
import com.baiyun.fragment.sliding.UserInfoFragment;
import com.baiyun.httputils.SlideMenuHttpUtils;
import com.baiyun.vo.parcelable.VersionPar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseSlidingFragmentActivity {
	private FragmentManager fragmentManager;
	
	private ContainerFragment containerFragment = null;
	private Fragment curFragment;
	
	private int curPosition = -1;
	private UserInfoFragment userInfoFragment = null;
	private LoginFragment loginFragment = null;
	private ToolsFragment toolsFragment = null;
	private SettingFragment settingFragment = null;
	private HelpFragment helpFragment = null;
	private AboutFragment aboutFragment = null;
	
	private boolean isSetBtnMenu2EnableFalse = false;
	
	private long exitTime = 0;
	
	@Override
	public void init() {
		checkVersionAuto();//自动检测新版本
		
		setTopBarTitle("广州市白云工商技师学院");
		setTopBarRightBtnEnable(true);
        fragmentManager = getSupportFragmentManager(); 
        
        containerFragment = ContainerFragment.newInstance();
		curFragment = containerFragment;
		fragmentManager.beginTransaction().add(R.id.fl_container_common, curFragment).commit();
        
		BaiduPushManager.startWork(getApplicationContext());//启动百度推送
        
        initTopBarRightClickListener();
        initgetSlideMenuFramentListener();
	}

	private void initTopBarRightClickListener(){
        getTopBarRightImageButton().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSlideMenuFragment();
			}
		});
    }
	
	public void showLoginFragment() {
		setBackPressEnabled(true);
		if (isBtnMenu2Enable()) {
			setBtnMenu2Enable(false);
			isSetBtnMenu2EnableFalse = true;
		}
		setTopBarTitle("用户登录");
		switchFragment(1);
	}
    
    private void initgetSlideMenuFramentListener(){
        getSlideMenuFragment().setOnSlideMenuFragmentEventListener(new SlideMenuFragment.OnSlideMenuFragmentEventListener() {
			
			@Override
			public void onSlideMenuFragmentEvent(int menuType) {
				switch (menuType) {
				case SlideMenuFragment.MENU_INFO:
					if (((MyApplication)getApplication()).isLogin()) {
						setTopBarTitle("用户信息");
						switchFragment(1);
					}else {
						setTopBarTitle("用户登录");
						switchFragment(2);
					}
					closeSlideMenuFragmetAndShowContent();
					break;
				case SlideMenuFragment.MENU_LOGIN:
					if (((MyApplication)getApplication()).isLogin()) {
						Toast.makeText(getApplicationContext(), "你已登录", Toast.LENGTH_SHORT).show();
					}else {
						setTopBarTitle("用户登录");
						switchFragment(2);
						closeSlideMenuFragmetAndShowContent();
					}
					break;
				case SlideMenuFragment.MENU_TOOLS:
					setTopBarTitle("实用工具");
					switchFragment(3);
					closeSlideMenuFragmetAndShowContent();
					break;
				case SlideMenuFragment.MENU_SETTING:
					setTopBarTitle("设置");
					switchFragment(4);
					closeSlideMenuFragmetAndShowContent();
					break;
				case SlideMenuFragment.MENU_HELP:
					setTopBarTitle("使用帮助");
					switchFragment(5);
					closeSlideMenuFragmetAndShowContent();
					break;
				case SlideMenuFragment.MENU_ABOUT:
					setTopBarTitle("关于我们");
					switchFragment(6);
					closeSlideMenuFragmetAndShowContent();
					break;
				case SlideMenuFragment.MENU_EXIT:
					new AlertDialog.Builder(MainActivity.this)
						.setTitle("温馨提示")
						.setMessage("确定退出应用吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								appExit();
							}
						})
						.setNegativeButton("取消", null)
						.create().show();
					break;
				default:
					break;
				}
			}
		});
    }
    
	public void switchFragment(int position) {
//		System.out.println("cur="+curPosition+" pos="+position);
		
		if (position != 0) {//如果非首页，则显示回退按钮
			setBackPressEnabled(true);
			if (isBtnMenu2Enable()) {
				setBtnMenu2Enable(false);
				isSetBtnMenu2EnableFalse = true;
			}
		}
		
		Fragment nextFragment = null;
		if (position == 0) {
			if (containerFragment == null) {
				containerFragment = ContainerFragment.newInstance();
			}
			nextFragment = containerFragment;
		}else if (position == 1) {
			if (userInfoFragment == null) {
				userInfoFragment = UserInfoFragment.newInstance();
			}
			nextFragment = userInfoFragment;
		}else if (position == 2) {
			if (loginFragment == null) {
				loginFragment = LoginFragment.newInstance();
			}
			nextFragment = loginFragment;
		}else if (position == 3) {
			if (toolsFragment == null) {
				toolsFragment = ToolsFragment.newInstance();
			}
			nextFragment = toolsFragment;
		}else if (position == 4) {
			if (settingFragment == null) {
				settingFragment = SettingFragment.newInstance();
			}
			nextFragment = settingFragment;
		}else if (position == 5) {
			if (helpFragment == null) {
				helpFragment = HelpFragment.newInstance();
			}
			nextFragment = helpFragment;
		}else if (position == 6) {
			if (aboutFragment == null) {
				aboutFragment = AboutFragment.newInstance();
			}
			nextFragment = aboutFragment;
		}
		
		if (curPosition != position) {
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			if (!nextFragment.isAdded()) {    // 先判断是否被add过  
                transaction.hide(curFragment).add(R.id.fl_container_common, nextFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中  
			} else {  
                transaction.hide(curFragment).show(nextFragment).commit(); // 隐藏当前的fragment，显示下一个  
            } 
			/* ToolsFragment可能需要实时更新，所以在hide的同时remove*/
//			if (curFragment == toolsFragment) {
//				fragmentManager.beginTransaction().remove(curFragment).commit();
//			}
			
			curPosition = position;
			curFragment = nextFragment;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (containerFragment.isHidden()) {
			switchFragment(0);
			containerFragment.setChildTitle();
			setBackPressEnabled(false);
			if (isSetBtnMenu2EnableFalse) {
				setBtnMenu2Enable(true);
				isSetBtnMenu2EnableFalse = false;
			}
		}else {
//			super.onBackPressed();
		}
	}
	
	/** 捕捉按下返回键操作 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 按两次返回键退出应用程序
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && containerFragment.isVisible()) {
			// 判断间隔时间 大于2秒就退出应用
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// 应用名
				String applicationName = getResources().getString(
						R.string.app_name);
				String msg = "再按一次返回键退出" + applicationName;
				//String msg1 = "再按一次返回键回到桌面";
				Toast.makeText(MainActivity.this, msg, 0).show();
				// 计算两次返回键按下的时间差
				exitTime = System.currentTimeMillis();
			} else {
				// 关闭应用程序
//				finish();
				// 返回桌面操作
				 Intent home = new Intent(Intent.ACTION_MAIN);
				 home.addCategory(Intent.CATEGORY_HOME);
				 startActivity(home);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void appExit(){//退出app
    	MainActivity.this.finish();
    	System.exit(0);
    }
	
	private void checkVersionAuto(){
		try {
			String curVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			final Float curVersionNameFloat = Float.parseFloat(curVersionName);
			
			new SlideMenuHttpUtils(MainActivity.this).getVersion(curVersionName, new SlideMenuHttpUtils.OnGetVersionListener() {
				
				@Override
				public void onGetVersion(VersionPar versionPar) {
					// TODO Auto-generated method stub
					if (versionPar != null) {
						String serVersionName = versionPar.getLatestVersion();
						Float serVersionNameFloat = Float.parseFloat(serVersionName);
						if (curVersionNameFloat < serVersionNameFloat) {//有新的版本
							DialogFactory.showVersionNotice(MainActivity.this, versionPar);
						}
					}
				}
			});
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
