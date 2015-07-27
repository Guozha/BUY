package com.guozha.buy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 为了解决ScrollView ,GridView, ListView冲突问题
 * @author PeggyTong
 *
 */
public class MultiListView extends ListView{
	
	public MultiListView(Context context) {
		super(context);
	}
	
	public MultiListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MultiListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
    }  
}
