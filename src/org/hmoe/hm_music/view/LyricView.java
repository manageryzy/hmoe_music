package org.hmoe.hm_music.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class LyricView extends TextView {

	public LyricView(Context context) {
		super(context);
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

}
