package org.hmoe.hm_music.view;

import org.hmoe.hm_music.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class PlayPageView extends RelativeLayout {

	Handler handler=null;
	int LyricX1=0,LyricX2=0,LyricY1=0,LyricY2=0;

	public PlayPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		handler=new Handler();
	}

	public PlayPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler=new Handler();
	}

	public PlayPageView(Context context) {
		super(context);
		handler=new Handler();
	}
	
	
	@Override
	protected void onFinishInflate() {
		delayToReshpe();
		super.onFinishInflate();
	}

	
	void delayToReshpe()
	{
		
		new Thread(){  
            public void run(){  
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	handler.post(new Runnable() {
					
					@Override
					public void run() {
						LayoutParams PlayUpLayoutParams = new LayoutParams(findViewById(R.id.play_up_layout).getLayoutParams());
			            PlayUpLayoutParams.height=findViewById(R.id.play_container).getHeight()-findViewById(R.id.play_down_layout).getHeight();
			            findViewById(R.id.play_up_layout).setLayoutParams(PlayUpLayoutParams);
			            
			            LyricX1 = findViewById(R.id.LyricView1).getLeft();
			            LyricX2 = LyricX1+findViewById(R.id.LyricView1).getWidth();
			            LyricY1 = findViewById(R.id.LyricView1).getTop();
			            LyricY2 = LyricY1+findViewById(R.id.LyricView1).getHeight();
					}
				});
	            
            }  
        }.start();  
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X=(int) event.getX();
		int Y=(int) event.getY();
		if(X>LyricX1&&X<LyricX2)
			if(Y>LyricY1&&Y<LyricY2)
			{
				findViewById(R.id.LyricView1).onTouchEvent(event);
				return true;
			}
		Throwable t=null;
		
		t.getCause();
		
		return super.onTouchEvent(event);
	}
}
