package ro.quadroq.commonclasses.colorgenerator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ro.quadroq.commonclasses.R;

/**
 * Created by mateisuica on 21/06/15.
 */
public class ColorGeneratorView extends LinearLayout {


    private final GestureDetector mDetector;
    private final CodeGeneratorGestureListener gestureListener;

    public ColorGeneratorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_generator_view, this, true);
        TextView textView = (TextView) findViewById(R.id.textView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        gestureListener = new CodeGeneratorGestureListener(context, 0, textView, imageView);
        mDetector = new GestureDetector(context, gestureListener);

    }

    public void setColor(int color) {
        gestureListener.setColor(color);
    }

    public int getColor() {
        return gestureListener.getColor();
    }

    public void setOnDoubleTapListener(DoubleTapListener doubleTapListener) {
        gestureListener.setOnDoubleTapListener(doubleTapListener);
    }

    public void setOnLongPressListener(LongPressListener longPressListener) {
        gestureListener.setLongPressListener(longPressListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return true;
    }

    public interface LongPressListener {
        void onLongPress();
    }

    public interface DoubleTapListener {
        void onDoubleTap();
    }

}