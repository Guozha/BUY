package com.guozha.buy.activity.mpage;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.activity.global.SearchResultActivity;
import com.guozha.buy.adapter.SeasonItemListAdapter;
import com.guozha.buy.entry.mpage.season.Season;
import com.guozha.buy.entry.mpage.season.SeasonAdviceItem;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AutoViewFlipper;
import com.guozha.buy.view.AutoViewFlipper.OnSlopTouchListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 时令界面
 * @author PeggyTong
 *
 */
public class SeasonActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SeasonPage";
	
	private static final int SEASON_PRE = 0;    //上一个季节
	private static final int SEASON_CURRENT = 1;//当前季节
	private static final int SEASON_NEXT = 2;	//下一个季节
	
	private static final int HAND_REQUEST_SEASON_COMPLETED = 0x0001; //请求时令信息完成
	private static final int HAND_UPDATE_SEASON_PIC = 0X0002;  //更新时令图片
	
	private ListView mSeasonItemList;
	
	private List<Season> mSeasonsList;
	private AutoViewFlipper mAutoViewFilpper;
	private List<Bitmap> seasonBitmaps;
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
	private List<SeasonAdviceItem> mAdviceItem;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_REQUEST_SEASON_COMPLETED:
				requestSeasonImage();
				updateSeasonAdviceList(SEASON_CURRENT);
				break;
			case HAND_UPDATE_SEASON_PIC:
				int position = msg.arg1;
				Bitmap bitmap = (Bitmap) msg.obj;
				updateSeasonImage(bitmap, position);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_season);
		customActionBarStyle("时令养生");
		initView();
		//获取数据
		initData();
	}

	/**
	 * 初始化View
	 */
	private void initView(){
		mSeasonItemList = (ListView) findViewById(R.id.season_vegetable_list);
		mSeasonItemList.setAdapter(new SeasonItemListAdapter(SeasonActivity.this, mAdviceItem, mBitmapCache));
		mSeasonItemList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SeasonAdviceItem item = mAdviceItem.get(position);
				Intent intent = new Intent(SeasonActivity.this, SearchResultActivity.class);
				if(item != null){
					LogUtil.e("word = " + item.getWord());
					intent.putExtra("KeyWord", item.getWord());
				}
				SeasonActivity.this.startActivity(intent);
			}
		});
		
		mAutoViewFilpper = 
				(AutoViewFlipper) findViewById(R.id.season_auto_flipper_view);
		mAutoViewFilpper.stopAutoPlay();
		mAutoViewFilpper.slopToCenter();
		mAutoViewFilpper.setOnSlopTouchListener(new OnSlopTouchListener() {
			@Override
			public void scrollChanged(int position) {
				updateSeasonAdviceList(position);
			}
			@Override
			public void onTouchedView() { }
		});
		/**
		List<ImageView> imageViews = mAutoViewFilpper.getImageViews();
		for(int i = 0; i < imageViews.size(); i++){
			imageViews.get(i).setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					int index = (Integer) view.getTag();
					updateSeasonAdviceList(index);
					return false;
				}
			});
		}
		**/
	}
	
	private void initData(){
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + "season/list", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mSeasonsList = gson.fromJson(response, new TypeToken<List<Season>>() { }.getType());
				handler.sendEmptyMessage(HAND_REQUEST_SEASON_COMPLETED);
			}
		});
	}

	/**
	 * 更新顶部时令图片
	 */
	private void requestSeasonImage() {
		if(mSeasonsList != null){
			for(int i = 0; i < mSeasonsList.size(); i++){
				final int position = i;
				Season season = mSeasonsList.get(i);
				if(season == null) continue;
				String imageUrl = season.getSeasonPicUrl();
				if(imageUrl == null) continue;
				LogUtil.e("image_url = " + HttpManager.URL + imageUrl);
				HttpManager.getInstance(this).volleyImageRequest(
					HttpManager.URL + imageUrl, new Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							Message msg = new Message();
							msg.what = HAND_UPDATE_SEASON_PIC;
							msg.obj = response;
							msg.arg1 = position;
							handler.sendMessage(msg);
							LogUtil.e("position = " + position +", ....");
						}
				});
			}
		}
	}
	
	/**
	 * 更新上面的图片
	 */
	private void updateSeasonImage(Bitmap bitmap, int position){
		mAutoViewFilpper.setImage(bitmap, position);
	}
	
	/**
	 * 更改列表数据
	 * @param index
	 */
	private void updateSeasonAdviceList(int index){
		if(mSeasonsList == null) return;
		Season season = mSeasonsList.get(index);
		if(season == null) return;
		mAdviceItem = season.getGoodsList();
		mSeasonItemList.setAdapter(new SeasonItemListAdapter(SeasonActivity.this, mAdviceItem, mBitmapCache));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
