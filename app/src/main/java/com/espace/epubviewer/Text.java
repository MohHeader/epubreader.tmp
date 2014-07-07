package com.espace.epubviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by mohheader on 04/06/14.
 */
public class Text extends TextView {
    private GestureDetector gestureDetector;
    TouchListener listener;
    public Text(Context context,TouchListener epub) {
        super(context);
        listener = epub;
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        gestureDetector = new GestureDetector(context, new GestureListener());
        setTextIsSelectable(true);
    }

    public Text(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Text(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        listener.onSwipeRight();
                    } else {
                        listener.onSwipeLeft();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            super.onDown(e);
            return true;
        }
    }
    public interface TouchListener{
        void onSwipeRight();
        void onSwipeLeft();
    }
}
