package com.baiyun.activity.life;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.baiyun.activity.R;
import com.baiyun.activity.webview.WebViewFragment2;
import com.baiyun.base.BaseFragmentActivity;
import com.baiyun.util.ui.FragmentUtil;
import com.baiyun.vo.parcelable.LifeAssociationPar;

public class LAssociationActivity extends BaseFragmentActivity{
	private FragmentManager fragmentManager;
	
	private LAssociationFragment associationFragment = null;
	private LAssociationDetailFragment detailFragment = null;
	private WebViewFragment2 webViewFragment2 = null;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setBackPressEnabled(true);
		fragmentManager = getSupportFragmentManager();
		
		showLAssociationFragment();
	}
	
	private void showLAssociationFragment(){
		if (associationFragment == null) {
			associationFragment = LAssociationFragment.newInstance();
		}
		FragmentUtil.replaceNormal(associationFragment, fragmentManager, R.id.fl_container_common);
	}
	
	public void showLAssociationDetailFragment(LifeAssociationPar associationPar){
		detailFragment = new LAssociationDetailFragment();
		
		Bundle args = new Bundle();
		args.putParcelable(LAssociationDetailFragment.KEY_LIFE_ASSOCIATION_PAR, associationPar);
		
		FragmentUtil.replaceAddToBack(detailFragment, fragmentManager, R.id.fl_container_common, args);
	}
	
	public void showWebViewFragment2(String urlLast, String title){
		Bundle args = new Bundle();
		args.putString(WebViewFragment2.KEY_URL_LAST, urlLast);
		args.putString(WebViewFragment2.KEY_TITLE, title);
		if (webViewFragment2 == null) {
			webViewFragment2 = WebViewFragment2.newInstance();
		}
		FragmentUtil.replaceAddToBack(webViewFragment2, fragmentManager, R.id.fl_container_common, args);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
