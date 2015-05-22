package com.guozha.buy.ui.found;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.FoundSubjectDetailListAdapter;
import com.guozha.buy.ui.BaseActivity;
import com.guozha.buy.util.DimenUtil;

/**
 * 发现-主题 的详情
 * @author PeggyTong
 *
 */
public class FoundSubjectDetailActivity extends BaseActivity{
	
	private ImageView mHeadImage;   //头部图片
	private TextView mHeadTitle;	//头部标题
	private TextView mHeadDescript; //头部描述
	private ListView mDetailList;
	
	private FoundSubjectDetailListAdapter mSubjectDetailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_subject_detail);
		
		initView();
		
	}
	
	private void initView(){
		mDetailList = (ListView) findViewById(R.id.subject_detail_list);
		mDetailList.addHeaderView(getLayoutInflater()
				.inflate(R.layout.list_found_subject_header, null));
		mHeadImage = (ImageView) findViewById(R.id.subject_detail_head_image);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				DimenUtil.getScreenWidth(this), DimenUtil.getScreenWidth(this) / 2);
		mHeadImage.setLayoutParams(params);
		mHeadTitle = (TextView) findViewById(R.id.subject_detail_head_title);
		mHeadDescript = (TextView) findViewById(R.id.subject_detail_head_descript);
		
		
		mSubjectDetailAdapter = new FoundSubjectDetailListAdapter(this);
		
		mHeadImage.setImageResource(R.drawable.temp_subject_item_imag);
		mHeadTitle.setText("[端午]远归的自己，回家给爸妈做顿饭");
		mHeadDescript.setText("端午节起源于中国，最初是中国人民祛病防疫的节日，吴越之地春秋之前有在农历五月初五以龙舟竞渡形式举行部落图腾祭祀的习俗；后因诗人屈原在这一天死去，便成了中国汉族人民纪念屈原的传统节日；部分地区也有纪念伍子胥、曹娥等说法。");
		
		mDetailList.setAdapter(mSubjectDetailAdapter);
		
		
	}
	
	
}
