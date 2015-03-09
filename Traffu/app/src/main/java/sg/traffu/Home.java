package sg.traffu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;


public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		ImageView iv1 = (ImageView) findViewById(R.id.iv1);
		ImageView iv2 = (ImageView) findViewById(R.id.iv2);
		ImageView iv3 = (ImageView) findViewById(R.id.iv3);
		ImageView iv4 = (ImageView) findViewById(R.id.iv4);
		ImageView iv5 = (ImageView) findViewById(R.id.iv5);
		ImageView iv6 = (ImageView) findViewById(R.id.iv6);
		ImageView iv7 = (ImageView) findViewById(R.id.iv7);
		ImageView iv8 = (ImageView) findViewById(R.id.iv8);
		ImageView iv9 = (ImageView) findViewById(R.id.iv9);
		ImageView iv10 = (ImageView) findViewById(R.id.iv10);
		ImageView iv11 = (ImageView) findViewById(R.id.iv11);

		iv2.setOnDragListener(new MyDragListener()); 
		iv3.setOnDragListener(new MyDragListener());
		iv4.setOnDragListener(new MyDragListener());
		iv5.setOnDragListener(new MyDragListener());
		iv6.setOnDragListener(new MyDragListener());
		iv7.setOnDragListener(new MyDragListener());
		iv8.setOnDragListener(new MyDragListener());
		iv9.setOnDragListener(new MyDragListener());
		iv10.setOnDragListener(new MyDragListener());
		iv11.setOnDragListener(new MyDragListener());

		iv1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d("EVENT", Integer.toString(event.getAction()));

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.d("ACTION", "ACTION DOWN");
					v.setVisibility(View.INVISIBLE);
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					v.startDrag(null, shadowBuilder, v, 0);
					break;
				case MotionEvent.ACTION_UP:
					v.performClick();
					break;
				default:
					break;
				}

				return true;
			}
		});

		//		final Handler handler = new Handler();
		//        Runnable runnable = new Runnable() {
		//        	int curr = 1;
		//            public void run() {
		//            	
		//            	if (curr == 1) {
		//            		iv1.setImageResource(R.drawable.c2);
		//            		curr = 2;
		//            		Log.d("Rs", "1");
		//            		
		//            	}
		//            	
		//            	else if (curr == 2) {
		//            		iv1.setImageResource(R.drawable.c3);
		//            		curr = 3;
		//            		Log.d("Rs", "2");
		//            		
		//            	}
		//            	
		//            	else {
		//            		iv1.setImageResource(R.drawable.c1);
		//            		curr = 1;
		//            		Log.d("Rs", "3");
		//            		
		//            	}
		//            	           
		//            	handler.postDelayed(this, 500);
		//            }
		//        };
		//        handler.postDelayed(runnable, 500);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

class MyDragListener implements OnDragListener {

	@Override
	public boolean onDrag(final View v, DragEvent event) {

		String msg = "ACTION";
		Log.d(msg, "Drag Listener Activated");

		switch(event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED: 
			Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
			Log.e("MESSAGE", v.getTag().toString());
			Intent intent = new Intent(v.getContext(), ExpressWay.class);
			intent.putExtra("express_way", v.getTag().toString());
			v.getContext().startActivity(intent);
			break;
		case DragEvent.ACTION_DRAG_EXITED :
			Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
			break;
		case DragEvent.ACTION_DRAG_LOCATION  :
			Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
			break;
		case DragEvent.ACTION_DRAG_ENDED   :
			final View droppedView = (View) event.getLocalState();
			droppedView.post(new Runnable(){
				@Override
				public void run() {
					v.getRootView().findViewById(R.id.iv1).setVisibility(View.VISIBLE);
				}	
			});
			Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");		
			break;
		case DragEvent.ACTION_DROP:
			Log.d(msg, "ACTION_DROP event");
			break;
		default: break;
		}
		return true;
	}

} 
