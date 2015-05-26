package com.baiyun.activity.schoolservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baiyun.activity.R;
import com.baiyun.base.BaseFragment;
import com.baiyun.custom.CommonAdapter;
import com.baiyun.httputils.SchoolServiceHttpUtils;
import com.baiyun.picturesviewer.PicturesViewPagerActivity;
import com.baiyun.sharepreferences.UserInfoSP;
import com.baiyun.vo.parcelable.LostPar;
import com.holyn.selectlocalpiclib.LocalImageVo;
import com.holyn.selectlocalpiclib.SelectLocalPicActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SLostPublishFragment extends BaseFragment{
	public static final int REQUEST_CODE_SELECT_PIC = 0;
	private SchoolServiceHttpUtils httpUtils;
	
	private ListView listView;
	private MyListAdapter listAdapter;
	private List<LostPar> lostPars = new ArrayList<LostPar>();
	
	private EditText etContentSubmit;
	private LinearLayout llImgSelect;//横向存放待上传的图片
	private List<LocalImageVo> selectImageVos = new ArrayList<LocalImageVo>();
	private ImageView ivImageAdd;
	private LinearLayout llImageAddDescribe;
	private Button btnSubmit;
	
	public static SLostPublishFragment newInstance() {
		return new SLostPublishFragment();
	}
	
	public SLostPublishFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		httpUtils = new SchoolServiceHttpUtils(getActivity());
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.listview_common_2;
	}

	@Override
	public void createMyView(View rootView) {
		// TODO Auto-generated method stub
		initView(rootView);
		getMyLostPublish(false);
	}
	
	private void initView(View rootView){
		listView = (ListView) rootView.findViewById(R.id.listview);
		listView.addHeaderView(getHeaderView());
		listAdapter = new MyListAdapter(getActivity(), lostPars);
		listView.setAdapter(listAdapter);
	}
	
	private View getHeaderView(){
		View headerView = (View) LayoutInflater.from(getActivity()).inflate(R.layout.header_view_s_lost_publish, null);
		etContentSubmit = (EditText)headerView.findViewById(R.id.et_content);
		llImgSelect = (LinearLayout)headerView.findViewById(R.id.ll_img_select);
		ivImageAdd = (ImageView)headerView.findViewById(R.id.iv_img_add);
		ivImageAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//从本地选择图片
				Intent intent = new Intent(getActivity(), SelectLocalPicActivity.class);
				intent.putExtra(SelectLocalPicActivity.EXTRA_MAX_SELECT, 4-selectImageVos.size());//最多可选择4张
				startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
			}
		});
		llImageAddDescribe = (LinearLayout)headerView.findViewById(R.id.ll_img_add_describe);
		btnSubmit = (Button)headerView.findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitContent();
			}
		});
		
		return headerView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 接收本地选择的图片
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_SELECT_PIC) {
				ArrayList<LocalImageVo> list = data.getParcelableArrayListExtra(SelectLocalPicActivity.EXTRA_SELECT_IMAGEVOS);
				if (list.size() != 0) {
					selectImageVos.addAll(list);
					showSelectImg(list);
				}
			}
		}
	}
	
	/**
	 * 展示选择后的图片，点击图片右上角的删除按钮可以删除
	 */
	private void showSelectImg(List<LocalImageVo> imageVos){
		for (int i = 0; i < imageVos.size(); i++) {
			View containerView = LayoutInflater.from(getActivity()).inflate(R.layout.imageview_lost_publish, llImgSelect, false);
			ImageView ivImgSelect = (ImageView)containerView.findViewById(R.id.iv_img_select);
			ImageLoader.getInstance().displayImage("file://"+imageVos.get(i).getPath(), ivImgSelect);
			
			ivImgSelect.setTag(containerView);
			ivImgSelect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<String> imagePathList = new ArrayList<String>();
					imagePathList.addAll(getImagePathListFromVos(selectImageVos));
					
					Intent intent = new Intent(getActivity(), PicturesViewPagerActivity.class);
					intent.putStringArrayListExtra(PicturesViewPagerActivity.EXTRA_IMAGE_LIST, imagePathList);
					intent.putExtra(PicturesViewPagerActivity.EXTRA_CUR_POSITION, llImgSelect.indexOfChild((View) v.getTag()));
					intent.putExtra(PicturesViewPagerActivity.EXTRA_IS_LOCAL, true);
					getActivity().startActivity(intent);
				}
			});
			//点击删除
			ImageView ivDelete = (ImageView)containerView.findViewById(R.id.iv_img_delete);
			ivDelete.setTag(imageVos.get(i).getId());
			ivDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int imageId = (Integer)v.getTag();
					int index = -1;//将要移除的图片在横向布局上的位置
					for (int j = 0; j < selectImageVos.size(); j++) {
						if (imageId == selectImageVos.get(j).getId()) {
							index = j;
							break;
						}
					}
					if (index != -1) {
						selectImageVos.remove(index);
						llImgSelect.removeViewAt(index);
						
						ivImageAdd.setVisibility(View.VISIBLE);
						llImageAddDescribe.setVisibility(View.VISIBLE);
					}
				}
			});
			
			llImgSelect.addView(containerView, llImgSelect.getChildCount() - 2);
		}
		
		if (selectImageVos.size() == 4) {
			ivImageAdd.setVisibility(View.INVISIBLE);
			llImageAddDescribe.setVisibility(View.INVISIBLE);
		}
	}
	
	private void submitContent(){
		String brief = etContentSubmit.getText().toString();
		if (TextUtils.isEmpty(brief)) {
			Toast.makeText(getActivity(), "内容为空", Toast.LENGTH_SHORT).show();
			return;
		}
		showLoadingDialog("正在发布....");
		String userName = UserInfoSP.getSingleInstance(getActivity()).getAccount();
		httpUtils.postLostContent(brief, userName, new SchoolServiceHttpUtils.OnPostLostContentListener() {
			
			@Override
			public void onPostLostContent(String id) {
				if (id == null) {
					closeLoadingDialog();
					Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT).show();
					return;
				}else {
					httpUtils.postLostPictures(id, selectImageVos, new SchoolServiceHttpUtils.OnPostLostPicturesListener() {
						
						@Override
						public void onPostLostPictures(boolean isSuccess) {
							closeLoadingDialog();
							if (isSuccess) {
								Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
								getMyLostPublish(true);
							}else {
								Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}
	
	//从List<LocalImageVo>中取出路径的list
	private ArrayList<String> getImagePathListFromVos(List<LocalImageVo> localImageVos) {
		ArrayList<String> imagePathList = new ArrayList<String>();
		Iterator<LocalImageVo> iterator = localImageVos.iterator();
		while (iterator.hasNext()) {
			LocalImageVo localImageVo = (LocalImageVo) iterator.next();
			imagePathList.add(localImageVo.getPath());
		}
		return imagePathList;
	}

	private void getMyLostPublish(final boolean isRefresh){
		((SLostPublishActivity) getActivity()).setLoadingBarVisible();
		String name = UserInfoSP.getSingleInstance(getActivity()).getAccount();
		httpUtils.getLostPar(name, new SchoolServiceHttpUtils.OnGetLostParListener() {
			
			@Override
			public void onGetLostPar(List<LostPar> pars) {
				if (getActivity() != null) {
					((SLostPublishActivity) getActivity()).setLoadingBarGone();
				}
				if (pars != null) {
					if (isRefresh) {
						lostPars.clear();
					}
					lostPars.addAll(pars);
					listAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((SLostPublishActivity) getActivity()).setLoadingBarGone();
	}

	private class MyListAdapter extends CommonAdapter<LostPar>{
		
		public MyListAdapter(Context context, List<LostPar> mDatas) {
			this(context, mDatas, R.layout.list_item_lost_my_pulished);
		}

		public MyListAdapter(Context context, List<LostPar> mDatas, int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
		}

		@Override
		public void convert(com.baiyun.custom.ViewHolder helper, final LostPar item) {
			helper.setText(R.id.tv_content, item.getBrief());
			helper.setText(R.id.tv_time, item.getContentCreateTime());
			
			//编辑
			Button btnEdit = helper.getView(R.id.btn_edit);
			btnEdit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			
			//删除
			Button btnCancel = helper.getView(R.id.btn_cancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					lostPars.remove(item);
					MyListAdapter.this.notifyDataSetChanged();
				}
			});
		}
		
	}

}
