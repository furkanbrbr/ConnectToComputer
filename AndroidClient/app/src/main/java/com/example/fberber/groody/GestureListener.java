package com.example.fberber.groody;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureListener extends SimpleOnGestureListener {

	static final String LOG_TAG = "ConnectToComputer";
	
	Context mContext;
	MainActivityInterface mDelegate;
	
	public GestureListener(Context context, MainActivityInterface delegate) {
		super();
		mContext = context;
		mDelegate = delegate;
	}

//	@Override
//	public boolean onDoubleTap(MotionEvent e) {
//		String coord = e.getX() + "," + e.getY();
//		String gesture = "Double Tap, " + e.getPointerCount() + " Point(s)";
//		mDelegate.setTextCoord(coord);
//		mDelegate.setTextGesture(gesture);
//		return super.onDoubleTap(e);
//	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		String coord = e.getX() + "," + e.getY();
		int action = e.getAction() & MotionEvent.ACTION_MASK;
		String strAction = null;
		byte byAction = -1;
		boolean send = false;
		if (MotionEvent.ACTION_DOWN == action){
			strAction = "Down";
			byAction = Common.ACTION_DOUBLE_TAP_DOWN;
		} else if (MotionEvent.ACTION_UP == action){
			send = true;
			strAction = "Up";
			byAction = Common.ACTION_DOUBLE_TAP_UP;
		} else if (MotionEvent.ACTION_MOVE == action){
			strAction = "Move";
			byAction = Common.ACTION_DOUBLE_TAP_MOVE;
		} else {
			strAction = String.valueOf(action);
		}

		if(send){
			byte[] data = new byte[3];
			data[0] = Common.INPUT_TYPE_MOUSE;
			data[1] = byAction;
			data[2] = (byte)mDelegate.getPointerCount();
			mDelegate.sendData(data);
		}
		
		String gesture = "Double Tap Event "+ strAction +", " + e.getPointerCount() + " Point(s)";
		mDelegate.setTextCoord(coord);
		mDelegate.setTextGesture(gesture);
		return super.onDoubleTapEvent(e);
	}

//	@Override
//	public boolean onDown(MotionEvent e) {
//		String coord = e.getX() + "," + e.getY();
//		String gesture = "Down, " + e.getPointerCount() + " Point(s)";
//		mDelegate.setTextCoord(coord);
//		mDelegate.setTextGesture(gesture);
//		return super.onDown(e);
//	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		byte[] data = new byte[3];
		data[0] = Common.INPUT_TYPE_MOUSE;
		data[1] = Common.ACTION_FLING;
		data[2] = (byte)mDelegate.getPointerCount();
		mDelegate.sendData(data);
		
		String coord = String.format("%f,%f|%f,%f|%f,%f", e1.getX(), e1.getY(), e2.getX(), e2.getY(), velocityX, velocityY);
		String gesture = String.format("Fling, %d,%d Point(s)", e1.getPointerCount(), e2.getPointerCount());
		mDelegate.setTextCoord(coord);
		mDelegate.setTextGesture(gesture);
		return super.onFling(e1, e2, velocityX, velocityY);
	}

//	@Override
//	public void onLongPress(MotionEvent e) {
//		String coord = e.getX() + "," + e.getY();
//		String gesture = "Long Press, " + e.getPointerCount() + " Point(s)";
//		mDelegate.setTextCoord(coord);
//		mDelegate.setTextGesture(gesture);
//		super.onLongPress(e);
//	}

//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2,
//			float distanceX, float distanceY) {
//		String coord = String.format("%f,%f|%f,%f|%f,%f", e1.getX(), e1.getY(), e2.getX(), e2.getY(), distanceX, distanceY);
//		String gesture = String.format("Scroll, %d,%d Point(s)", e1.getPointerCount(), e2.getPointerCount());
//		mDelegate.setTextCoord(coord);
//		mDelegate.setTextGesture(gesture);
//		return super.onScroll(e1, e2, distanceX, distanceY);
//	}

//	@Override
//	public void onShowPress(MotionEvent e) {
//		String coord = e.getX() + "," + e.getY();
//		String gesture = "Show Press, " + e.getPointerCount() + " Point(s)";
//		mDelegate.setTextCoord(coord);
//		mDelegate.setTextGesture(gesture);
//		super.onShowPress(e);
//	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		byte[] data = new byte[3];
		data[0] = Common.INPUT_TYPE_MOUSE;
		data[1] = Common.ACTION_SINGLE_TAP;
		data[2] = (byte)mDelegate.getPointerCount();
		mDelegate.sendData(data);

		String coord = e.getX() + "," + e.getY();
		String gesture = "Single Tap Confirmed, " + e.getPointerCount() + " Point(s)";
		mDelegate.setTextCoord(coord);
		mDelegate.setTextGesture(gesture);
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		byte[] data = new byte[3];
		data[0] = Common.INPUT_TYPE_MOUSE;
		data[1] = Common.ACTION_SINGLE_TAP_UP;
		data[2] = (byte)mDelegate.getPointerCount();
		mDelegate.sendData(data);
		
		String coord = e.getX() + "," + e.getY();
		String gesture = "Single Tap Up, " + mDelegate.getPointerCount() + " Point(s)";
		mDelegate.setTextCoord(coord);
		mDelegate.setTextGesture(gesture);
		return super.onSingleTapUp(e);
	}

}
