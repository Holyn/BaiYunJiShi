package com.baiyun.activity.recruit;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.baiyun.activity.R;
import com.baiyun.activity.webview.WebViewActiviry;
import com.baiyun.base.BaseFragment;
import com.baiyun.httputils.RecruitHttpUtils;

public class ApplyFragment2 extends BaseFragment{
	private RecruitHttpUtils httpUtils;
	
	public static ApplyFragment2 newInstance() {
		return new ApplyFragment2();
	}
	
	public ApplyFragment2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_apply_2;
	}

	@Override
	public void createMyView(View rootView) {
		httpUtils = new RecruitHttpUtils(getActivity());
		
		Button btnRegister = (Button)rootView.findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoadingDialog();
				httpUtils.getRCUrl("2", new RecruitHttpUtils.OnGetRCUrlListener() {
					
					@Override
					public void onGetRCUrl(String url) {
						// TODO Auto-generated method stub
						closeLoadingDialog();
						if (url != null) {
							Intent intent = new Intent(getActivity(), WebViewActiviry.class);
							intent.putExtra(WebViewActiviry.KEY_WEB_VIEW_TYPE, WebViewActiviry.R_Register);
							intent.putExtra(WebViewActiviry.KEY_CONTENT_URL, url);
							getActivity().startActivity(intent);
						}
					}
				});
			}
		});
		Button btnConsult = (Button)rootView.findViewById(R.id.btn_consult);
		btnConsult.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoadingDialog();
				httpUtils.getRCUrl("1", new RecruitHttpUtils.OnGetRCUrlListener() {
					
					@Override
					public void onGetRCUrl(String url) {
						// TODO Auto-generated method stub
						closeLoadingDialog();
						if (url != null) {
							Intent intent = new Intent(getActivity(), WebViewActiviry.class);
							intent.putExtra(WebViewActiviry.KEY_WEB_VIEW_TYPE, WebViewActiviry.R_Consult);
							intent.putExtra(WebViewActiviry.KEY_CONTENT_URL, url);
							getActivity().startActivity(intent);
						}
					}
				});
			}
		});
	
	}
}
