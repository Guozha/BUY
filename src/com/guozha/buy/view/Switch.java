package com.guozha.buy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.guozha.buy.R;

public class Switch extends ImageView implements OnClickListener{
	
	private boolean isCheck;
	private OnChangedListener changeListener;
	public interface OnChangedListener {
		abstract void onCheckedChanged(Switch checkSwitch, boolean CheckState);
	}

	public Switch(Context context) {
		super(context);
		init(context);
	}

	public Switch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public Switch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		setImageResource(R.drawable.main_switch_off);
		setOnClickListener(this);
	}

	public void setChecked(boolean checked) {
		setCheckStyle(checked);
		if(changeListener != null){
			changeListener.onCheckedChanged(this, isCheck);
		}
	}
	
	public void setCheckStyle(boolean checked){
		isCheck = checked;
		if(checked){
			setImageResource(R.drawable.main_switch_on);
		}else{
			setImageResource(R.drawable.main_switch_off);
		}
		
	}

	public boolean isChecked() {
		return isCheck;
	}


	@Override
	public void onClick(View v) {
		isCheck = !isCheck;
		setChecked(isCheck);
	}
	
	public void setOnChangedListener(OnChangedListener l) {// 设置监听器,当状态修改的时候
		changeListener = l;
	}
}
