package com.baiyun.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.baiyun.activity.R;
import com.baiyun.activity.home.DepartmentActivity;
import com.baiyun.activity.home.JobActivity;
import com.baiyun.activity.home.NewsActivity;
import com.baiyun.activity.home.OverviewActivity;
import com.baiyun.activity.home.TrafficActivity;
import com.baiyun.activity.home.VideoActivity;
import com.baiyun.activity.webview.WebViewActiviry;
import com.baiyun.base.BaseFragment;
import com.baiyun.custom.CircleMenuLayout;
import com.baiyun.custom.CircleMenuLayout.OnMenuItemClickListener;
import com.baiyun.httputils.HomeHttpUtils;
import com.baiyun.httputils.RecruitHttpUtils;

public class HomeFragment_5 extends BaseFragment{
	private CircleMenuLayout mCircleMenuLayout;
	private int[] mItemImgs = new int[] { R.drawable.home_school_summary_4, R.drawable.home_school_reports_4, R.drawable.home_video_4,
			R.drawable.home_sys_setting_4, R.drawable.home_consult_4, R.drawable.home_online_4, R.drawable.home_job_4,
			R.drawable.home_traffic_4 };
	private HomeHttpUtils httpUtils;

	private String onlineUrl = null;// 白云在线的跳转链接
	
	public static HomeFragment_5 newInstance() {
		return new HomeFragment_5();
	}

	public HomeFragment_5() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		httpUtils = new HomeHttpUtils(getActivity());
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.fragment_home_5;
	}

	@Override
	public void createMyView(View rootView) {
		// TODO Auto-generated method stub
		initView(rootView);
		getNetData();
	}
	
	private void initView(View rootView){
		mCircleMenuLayout = (CircleMenuLayout) rootView.findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs);

		mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void itemClick(View view, int pos) {
				switch (pos) {
				case 0:// 学校概况
					getActivity().startActivity(new Intent(getActivity(), OverviewActivity.class));
					break;
				case 1:// 技师微报
					getActivity().startActivity(new Intent(getActivity(), NewsActivity.class));
					break;
				case 2:// 视频白云
					getActivity().startActivity(new Intent(getActivity(), VideoActivity.class));
					break;
				case 3:// 系部设置
					getActivity().startActivity(new Intent(getActivity(), DepartmentActivity.class));
					break;
				case 4:// 招生咨询
//					new KeFuManager(getActivity()).startChat();
					
					showLoadingDialog();
					(new RecruitHttpUtils(getActivity())).getRCUrl("3", new RecruitHttpUtils.OnGetRCUrlListener() {
						
						@Override
						public void onGetRCUrl(String url) {
							// TODO Auto-generated method stub
							closeLoadingDialog();
							if (url != null) {
								Intent intent = new Intent(getActivity(), WebViewActiviry.class);
								intent.putExtra(WebViewActiviry.KEY_WEB_VIEW_TYPE, WebViewActiviry.H_Consult);
								intent.putExtra(WebViewActiviry.KEY_CONTENT_URL, url);
								getActivity().startActivity(intent);
							}
						}
					});
					break;
				case 5:// 白云在线
					if (onlineUrl != null) {
						if (!(onlineUrl.contains("http://"))) {
							onlineUrl = "http://" + onlineUrl;
						}
						new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage("跳转到：" + onlineUrl)
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (URLUtil.isNetworkUrl(onlineUrl)) {
											Uri uri = Uri.parse(onlineUrl);
											Intent intent = new Intent(Intent.ACTION_VIEW, uri);
											getActivity().startActivity(intent);
										} else {
											Toast.makeText(getActivity(), "网站链接不正确\n" + onlineUrl, Toast.LENGTH_SHORT).show();
										}
									}
								}).setNegativeButton("取消", null).create().show();
					} else {
						Toast.makeText(getActivity(), "链接为空" + onlineUrl, Toast.LENGTH_SHORT).show();
					}
					break;
				case 6:// 就业服务
					getActivity().startActivity(new Intent(getActivity(), JobActivity.class));
					break;
				case 7:// 交通查询
					getActivity().startActivity(new Intent(getActivity(), TrafficActivity.class));
					break;

				default:
					break;
				}
			}

			@Override
			public void itemCenterClick(View view) {// 转盘中心的点击事件

			}
		});
	}
	
	private void getNetData() {
		httpUtils.getOnlineUrl(new HomeHttpUtils.OnGetOnlineUrlListener() {

			@Override
			public void onGetOnlineUrl(String url) {
				if (url != null) {
					onlineUrl = url;
				}

			}
		});
	}

}