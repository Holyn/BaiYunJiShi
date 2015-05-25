package com.baiyun.fragment.sliding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.baiyun.activity.R;
import com.baiyun.activity.main.MainActivity;
import com.baiyun.base.BaseFragment;
import com.baiyun.cache.CachePath;
import com.baiyun.constants.Constants;
import com.baiyun.http.HttpURL;
import com.baiyun.http.MyCookieStore;
import com.baiyun.httputils.SlideMenuHttpUtils;
import com.baiyun.sharepreferences.UserInfoSP;
import com.baiyun.util.FilePathUtil;
import com.baiyun.util.FormatUtil;
import com.baiyun.vo.parcelable.UserInfoPar;
import com.holyn.selectlocalpiclib.LocalImageVo;
import com.holyn.selectlocalpiclib.util.LoclImageLoader;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class LoginFragment extends BaseFragment {
	private EditText etName, etPassword, etVeriCode;
	private ImageView ivName, ivPassword, ivVeriCode;
	private ImageButton ibVerCode;
	private Button btnSubmit;

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	public LoginFragment() {

	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_login;
	}

	@Override
	public void createMyView(View rootView) {
		etName = (EditText) rootView.findViewById(R.id.et_name);
		ivName = (ImageView) rootView.findViewById(R.id.iv_name);
		etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ivName.setImageResource(R.drawable.ic_login_pre);
				} else {
					ivName.setImageResource(R.drawable.ic_login_nor);
				}

			}
		});

		etPassword = (EditText) rootView.findViewById(R.id.et_password);
		ivPassword = (ImageView) rootView.findViewById(R.id.iv_password);
		etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ivPassword.setImageResource(R.drawable.ic_password_pre);
				} else {
					ivPassword.setImageResource(R.drawable.ic_password_nor);
				}

			}
		});

		etVeriCode = (EditText) rootView.findViewById(R.id.et_veri_code);
		ivVeriCode = (ImageView) rootView.findViewById(R.id.iv_veri_code);
		etVeriCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ivVeriCode.setImageResource(R.drawable.ic_veri_code_pre);
				} else {
					ivVeriCode.setImageResource(R.drawable.ic_veri_code_nor);
				}

			}
		});
		ibVerCode = (ImageButton) rootView.findViewById(R.id.ib_veri_code);
		displayImage(ibVerCode, HttpURL.S_GET_RANDOM_STRING_IMAGE);
		ibVerCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayImage(ibVerCode, HttpURL.S_GET_RANDOM_STRING_IMAGE);
			}
		});

		btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitLogin();
			}
		});
	}

	private void displayImage(final ImageButton imageButton, String uri) {
		final HttpUtils http = new HttpUtils();
		http.download(uri, CachePath.getFilePath(CachePath.getImgCacheDir(getActivity()), "randomString.jpg"), new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				String path = responseInfo.result.getPath();
//				System.out.println("====> path = " + path);
				try {
					FileInputStream fis = new FileInputStream(path);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					imageButton.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				// 取得sessionid.........................
				DefaultHttpClient dh = (DefaultHttpClient) http.getHttpClient();
				MyCookieStore.cookieStore = dh.getCookieStore();
				
//				CookieStore cs = dh.getCookieStore();
//				List<Cookie> cookies = cs.getCookies();
//				System.out.println("====> cookies.size() = "+cookies.size());
//				System.out.println("====> cookies = "+cookies);
//				String aa = null;
//				for (int i = 0; i < cookies.size(); i++) {
//					if ("JSESSIONID".equals(cookies.get(i).getName())) {
//						aa = cookies.get(i).getValue();
//						break;
//					}
//					System.out.println(cookies.get(i).getName()+" = "+cookies.get(i).getValue());
//				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void submitLogin() {
		String userName = etName.getText().toString().trim();
		if (TextUtils.isEmpty(userName)) {
			Toast.makeText(getActivity(), "用户名为空", Toast.LENGTH_SHORT).show();
			return;
		}

		String password = etPassword.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(getActivity(), "密码为空", Toast.LENGTH_SHORT).show();
			return;
		}

		String randomString = etVeriCode.getText().toString().trim();
		if (TextUtils.isEmpty(randomString)) {
			Toast.makeText(getActivity(), "验证码为空", Toast.LENGTH_SHORT).show();
			return;
		}

		showLoadingDialog();
		
		UserInfoSP userInfoSP = UserInfoSP.getSingleInstance(getActivity());
		String mobileUserId = userInfoSP.getMobileUserId();
		String mobileChannelId = userInfoSP.getMobileChannelId();

		SlideMenuHttpUtils httpUtils = new SlideMenuHttpUtils(getActivity());
		httpUtils.configCookieStore(MyCookieStore.cookieStore);
		httpUtils.postLogin(userName, password, randomString, mobileUserId, mobileChannelId,
				new SlideMenuHttpUtils.OnPostLoginListener() {

					@Override
					public void onPostLogin(UserInfoPar userInfoPar) {
						closeLoadingDialog();
						// TODO Auto-generated method stub
						if (userInfoPar != null) {
							etName.setText("");
							etPassword.setText("");
							etVeriCode.setText("");
							
							Intent intent = new Intent(Constants.INTENT_ACTION_LOGIN_SUCCESS);
							intent.putExtra(Constants.KEY_USER_INFO_PAR, userInfoPar);
							getActivity().sendBroadcast(intent);
						}
						((MainActivity)getActivity()).onBackPressed();
					}
				});

	}
}
