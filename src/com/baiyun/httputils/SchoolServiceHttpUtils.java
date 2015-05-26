package com.baiyun.httputils;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.baiyun.http.HttpRecode;
import com.baiyun.http.HttpURL;
import com.baiyun.util.Base64Util;
import com.baiyun.vo.parcelable.LostPar;
import com.baiyun.vo.parcelable.Vo1Par;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.holyn.selectlocalpiclib.LocalImageVo;
import com.holyn.selectlocalpiclib.util.PictureCompressUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SchoolServiceHttpUtils extends HttpUtils {
	private Context context;
	private int countAllUplodImg = -1;//计算所有上传的照片（包括成功与失败）
	private int countFailureUplodImg = 0;//计算上传失败的照片数目

	public SchoolServiceHttpUtils(Context context) {
		this.context = context;
	}

	public interface OnGetNoticeListener {
		public void onGetNotice(List<Vo1Par> vo1Pars);
	}

	public interface OnGetLostParListener {
		public void onGetLostPar(List<LostPar> lostPars);
	}

	public interface OnPostLostContentListener {
		public void onPostLostContent(String id);
	}

	public interface OnPostLostPicturesListener {
		public void onPostLostPictures(boolean isSuccess);
	}

	public void getNotice(int page, final OnGetNoticeListener onGetVo1ListListener) {
		String pageStr = String.valueOf(page);
		String url = HttpURL.S_NOTICE + pageStr;
		send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				List<Vo1Par> vo1Pars = null;
				try {
					JsonParser parser = new JsonParser();
					JsonObject jsonObject = parser.parse(responseInfo.result).getAsJsonObject();

					JsonElement recodeEle = jsonObject.get("recode");
					if (recodeEle.isJsonPrimitive()) {
						String recode = recodeEle.getAsString();
						if (recode.equalsIgnoreCase(HttpRecode.GET_SUCCESS)) {
							JsonElement dataEle = jsonObject.get("data");
							if (dataEle.isJsonObject()) {
								jsonObject = dataEle.getAsJsonObject();
								// int total =
								// jsonObject.get("total").getAsInt();
								JsonElement itemsEle = jsonObject.get("items");
								if (itemsEle.isJsonArray()) {
									JsonArray jsonArray = itemsEle.getAsJsonArray();
									java.lang.reflect.Type type = new TypeToken<List<Vo1Par>>() {
									}.getType();
									vo1Pars = new Gson().fromJson(jsonArray.toString(), type);
								}
							}
						}
					}
				} catch (Exception e) {
					vo1Pars = null;
					System.out.println(e);
				}
				onGetVo1ListListener.onGetNotice(vo1Pars);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				onGetVo1ListListener.onGetNotice(null);
				Toast.makeText(context, "数据请求失败", Toast.LENGTH_SHORT).show();
			}

		});
	}

	public void getLostPar(String name, final OnGetLostParListener onGetLostParListener) {
		String url = null;
		if (TextUtils.isEmpty(name)) {
			url = HttpURL.S_LOST_FOUND;
		} else {
			url = HttpURL.S_LOST_FOUND + "?userName=" + name;
		}
		send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				List<LostPar> lostPars = null;
				try {
					JsonParser parser = new JsonParser();
					JsonObject jsonObject = parser.parse(responseInfo.result).getAsJsonObject();

					JsonElement recodeEle = jsonObject.get("recode");
					if (recodeEle.isJsonPrimitive()) {
						String recode = recodeEle.getAsString();
						if (recode.equalsIgnoreCase(HttpRecode.GET_SUCCESS)) {
							JsonElement dataEle = jsonObject.get("data");
							if (dataEle.isJsonArray()) {
								JsonArray jsonArray = dataEle.getAsJsonArray();
								java.lang.reflect.Type type = new TypeToken<List<LostPar>>() {
								}.getType();
								lostPars = new Gson().fromJson(jsonArray.toString(), type);
							}
						}
					}
				} catch (Exception e) {
					lostPars = null;
					System.out.println(e);
				}
				onGetLostParListener.onGetLostPar(lostPars);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				onGetLostParListener.onGetLostPar(null);
				Toast.makeText(context, "数据请求失败", Toast.LENGTH_SHORT).show();
			}

		});
	}

	public void postLostContent(String brief, String userName, final OnPostLostContentListener onPostLostContentListener) {
		RequestParams params = new RequestParams();
		params.addBodyParameter(HttpURL.PARAM_BRIEF, brief);
		params.addBodyParameter(HttpURL.PARAM_USER_NAME, userName);
		send(HttpMethod.POST, HttpURL.S_LOST_PUBLISH_CONTENT, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String id = null;
				try {
					JsonParser parser = new JsonParser();
					JsonObject jsonObject = parser.parse(responseInfo.result).getAsJsonObject();

					JsonElement recodeEle = jsonObject.get("recode");
					if (recodeEle.isJsonPrimitive()) {
						String recode = recodeEle.getAsString();
						if (recode.equalsIgnoreCase(HttpRecode.INSERT_SUCCESS)) {
							JsonElement dataEle = jsonObject.get("data");
							if (dataEle.isJsonObject()) {
								JsonObject dataObject = dataEle.getAsJsonObject();
								JsonElement idEle = dataObject.get("id");
								if (idEle.isJsonPrimitive()) {
									id = idEle.getAsString();
								}
							}
						}
					}
				} catch (Exception e) {
					id = null;
					System.out.println(e);
				}
				onPostLostContentListener.onPostLostContent(id);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				onPostLostContentListener.onPostLostContent(null);
			}

		});
	}

	public void postLostPictures(String id, List<LocalImageVo> selectImageVos, final OnPostLostPicturesListener onPostLostPicturesListener) {
		countAllUplodImg = selectImageVos.size();
		if (countAllUplodImg == 0) {
			onPostLostPicturesListener.onPostLostPictures(true);
		} else if (countAllUplodImg > 0) {
			Iterator<LocalImageVo> iterator = selectImageVos.iterator();
			while (iterator.hasNext()) {
				LocalImageVo localImageVo = (LocalImageVo) iterator.next();

				RequestParams params = new RequestParams();
				params.addBodyParameter(HttpURL.PARAM_ID, id);

				Bitmap bitmap = PictureCompressUtil.getInstance().compress(localImageVo.getPath(), 400, 800, 400);
				String pic64 = Base64Util.getImage64FromBitmap(bitmap);
				params.addBodyParameter(HttpURL.PARAM_IMG, pic64);

				send(HttpMethod.POST, HttpURL.S_LOST_PUBLISH_PIC, params, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JsonParser parser = new JsonParser();
							JsonObject jsonObject = parser.parse(responseInfo.result).getAsJsonObject();

							JsonElement recodeEle = jsonObject.get("recode");
							if (recodeEle.isJsonPrimitive()) {
								String recode = recodeEle.getAsString();
								if (recode.equalsIgnoreCase(HttpRecode.INSERT_SUCCESS)) {
									countAllUplodImg = countAllUplodImg - 1;
									if (countAllUplodImg == 0) {
										if (countFailureUplodImg == 0) {
											onPostLostPicturesListener.onPostLostPictures(true);
										}else if (countFailureUplodImg > 0) {
											System.out.println("====> countFailureUplodImg = "+countFailureUplodImg);
											onPostLostPicturesListener.onPostLostPictures(false);
										}
									}
								}
							}
						} catch (Exception e) {
							countAllUplodImg = countAllUplodImg - 1;
							countFailureUplodImg = countFailureUplodImg + 1;
							if (countAllUplodImg == 0) {
								System.out.println("====> countFailureUplodImg = "+countFailureUplodImg);
								onPostLostPicturesListener.onPostLostPictures(false);
							}
							System.out.println(e);
						}
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						countAllUplodImg = countAllUplodImg - 1;
						countFailureUplodImg = countFailureUplodImg + 1;
						if (countAllUplodImg == 0) {
							System.out.println("====> countFailureUplodImg = "+countFailureUplodImg);
							onPostLostPicturesListener.onPostLostPictures(false);
						}
					}

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);
						System.out.println("====> totlal:" + total + "/ current:" + current);
					}
				});
			}
		}

	}

}
