package com.guozha.buy.controller.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;

/**
 * 活动规则
 * @author PeggyTong
 *
 */
public class ActiveRuleActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_active_rule);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		initView();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		findViewById(R.id.active_rule_free_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActiveRuleActivity.this.finish();
			}
		});
		TextView ruleTitle = (TextView) findViewById(R.id.rule_title);
		ruleTitle.setText(Html.fromHtml(ConfigManager.getInstance().getInvateRuleTitle()));
		TextView ruleText = (TextView) findViewById(R.id.rule_content);
		ruleText.setText(Html.fromHtml(ConfigManager.getInstance().getInvateRuleContent()));
	}
}
