package com.baiyun.constants;

/**
 * 手机客户端的静态常量类
 * @author Holyn
 * @create 2015-1-18
 * @modified
 */
public class Constants {
	//设备类型
	public static final String DEVICE_TYPE = "Android";
	
	//利用sharepreference保存用户信息的xml文件名
	public static final String USER_INFO = "userInfo";
	//利用sharepreference保存App的一些设置信息的xml文件名
	public static final String APP_SETTING = "appSetting";
	
	//登录成功的广播（Broadcast）的Action
	public static final String INTENT_ACTION_LOGIN_SUCCESS = "com.baiyun.intent.action.LOGIN_SUCCESS";
	
	//用户加密数据的秘钥
	public static final String SEED_AES = "holyn";
	
	//apk下载的Notification的id
	public static final int ID_APK_DOWN_NOTI = 001;
	
	//intent的标识与value
	public static final String INTENT_EXIT_STATE = "intent_exit_state";
	public static final String SYSTEM_EXIT = "system_exit";
	
	//通过Bundle或Itent传值的key
	public static final String KEY_USER_INFO_PAR = "key_UserInfoPar";
	
}
