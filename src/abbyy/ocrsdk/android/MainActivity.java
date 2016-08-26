package abbyy.ocrsdk.android;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMenuItemClickListener {

	static int flag;
	static int flag1;
	static int flag2;
	int textflag = 0;
	long btnid, btnid1;
	static String path;
	private final int TAKE_PICTURE = 0;
	private final int SELECT_FILE = 1;
	private final int BUIZ_CARD = 2;
	private final int EQN_CAM = 3;
	private final int EQN_FILE = 4;
	private final int TRA_CAM = 5;;
	private final int TRA_FILE = 6;
	// private final int BARCODE=7; /////////////

	private String resultUrl = "Result.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	/*
	 * public void showpopup1(View v) { PopupMenu popup = new PopupMenu(this,
	 * v); popup.setOnMenuItemClickListener(MainActivity.this); MenuInflater
	 * inflater = popup.getMenuInflater(); inflater.inflate(R.menu.popup_m,
	 * popup.getMenu()); popup.show(); btnid1 = v.getId(); }
	 * 
	 * public boolean onMenuItemClick1(MenuItem item) { // TODO Auto-generated
	 * method stub switch (item.getItemId()) {
	 * 
	 * case R.id.item4: Toast.makeText(getBaseContext(), item.toString(),
	 * Toast.LENGTH_SHORT).show(); if (btnid1 == R.id.equationclick) flag1 = 3;
	 * else if (btnid1 == R.id.transclick) flag1 = 4;
	 * 
	 * captureImageFromCamera();
	 * 
	 * return true;
	 * 
	 * case R.id.item5: Toast.makeText(getBaseContext(), item.toString(),
	 * Toast.LENGTH_SHORT).show(); // flag=1; if (btnid1 == R.id.equationclick)
	 * flag2 = 3; else if (btnid1 == R.id.transclick) flag2 = 4;
	 * 
	 * captureImageFromSdCard();
	 * 
	 * return true;
	 * 
	 * case R.id.item6: Toast.makeText(getBaseContext(), item.toString(),
	 * Toast.LENGTH_SHORT).show(); if (btnid1 == R.id.equationclick) textflag =
	 * 3; else if (btnid1 == R.id.transclick) textflag = 4;
	 * 
	 * takeinput(); return true;
	 * 
	 * default: return false; }
	 * 
	 * }
	 */
	public void showpopup(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener(MainActivity.this);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.activity_main, popup.getMenu());
		popup.show();
		btnid = v.getId();
	}

	public void showpopup1(View view) {
		if (view.getId() == R.id.equationclick) {
			flag2 = 3;
			flag1 = 3;
		}
		captureImageFromSdCard();
	}

	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.item1:
			Toast.makeText(getBaseContext(), item.toString(),
					Toast.LENGTH_SHORT).show();
			if (btnid == R.id.editorclick)
				flag1 = 1;
			else if (btnid == R.id.cardclick)
				flag1 = 2;
			else if (btnid == R.id.transclick)
				flag1 = 4;

			captureImageFromCamera();

			return true;

		case R.id.item2:
			Toast.makeText(getBaseContext(), item.toString(),
					Toast.LENGTH_SHORT).show();
			// flag=1;
			if (btnid == R.id.editorclick)
				flag2 = 1;

			else if (btnid == R.id.cardclick)
				flag2 = 2;
			else if (btnid == R.id.transclick)
				flag2 = 4;

			captureImageFromSdCard();

			return true;
		default:
			return false;
		}

	}

	public void captureImageFromSdCard() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_FILE);
		switch (flag2) {
		case 1:
			flag = 0;
			break;
		case 2:
			flag = 1;
			break;
		case 3:
			flag = 2;
			break;
		case 4:
			flag = 3;
			break;
		}
	}

	public void captureImageFromCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		Uri fileUri = getOutputMediaFileUri();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, TAKE_PICTURE);
		switch (flag1) {
		case 1:
			flag = 0;
			break;
		case 2:
			flag = 1;
			break;
		case 3:
			flag = 2;
			break;
		case 4:
			flag = 3;
			break;

		/*
		 * case R.id.barcode: flag=4; break;
		 */
		}
	}

	/*
	 * public void takeinput() {
	 * 
	 * switch (textflag) { case 3: flag = 2; Toast.makeText(this, "HELLO",
	 * Toast.LENGTH_SHORT).show(); break;
	 * 
	 * case 4: flag = 3; break; } Intent tf = new Intent(this, TEXT.class);
	 * startActivity(tf); }
	 */

	public static final int MEDIA_TYPE_IMAGE = 1;

	private static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "OCR OUTPUTS");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH-mm-ss");
		// path=mediaStorageDir.getPath() + File.separator +
		// dateFormat.format(date)+".jpg" ;

		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "OCRimage.jpg");

		return mediaFile;
	}

	public static int getFlag() {
		return flag;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;

		String imageFilePath = null;

		switch (requestCode) {
		case TAKE_PICTURE:
			imageFilePath = getOutputMediaFileUri().getPath();
			break;
		case SELECT_FILE: {
			Uri imageUri = data.getData();
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cur = getContentResolver().query(imageUri, projection, null,
					null, null); // /////////
			cur.moveToFirst();
			imageFilePath = cur.getString(cur
					.getColumnIndex(MediaStore.Images.Media.DATA));
		}
			break;
		/*
		 * case BUIZ_CARD:{ ///////////////////// imageFilePath =
		 * getOutputMediaFileUri().getPath(); break; } case EQN_CAM:{
		 * ///////////////////// imageFilePath =
		 * getOutputMediaFileUri().getPath(); break; } case EQN_FILE: { Uri
		 * imageUri = data.getData(); String[] projection = {
		 * MediaStore.Images.Media.DATA }; Cursor cur =
		 * getContentResolver().query(imageUri, projection, null, null, null);
		 * /////////// cur.moveToFirst(); imageFilePath =
		 * cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA)); }
		 * case TRA_CAM:{ ///////////////////// imageFilePath =
		 * getOutputMediaFileUri().getPath(); break; } case TRA_FILE: { Uri
		 * imageUri = data.getData(); String[] projection = {
		 * MediaStore.Images.Media.DATA }; Cursor cur =
		 * getContentResolver().query(imageUri, projection, null, null, null);
		 * /////////// cur.moveToFirst(); imageFilePath =
		 * cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA)); } /*
		 * case BARCODE:{ ///////////////////// imageFilePath =
		 * getOutputMediaFileUri().getPath(); break; }
		 */

		}
		deleteFile(resultUrl);

		if (flag == 3) {

			Intent targ = new Intent(this, Target.class);
			targ.putExtra("IMAGE_PATH", imageFilePath);
			targ.putExtra("RESULT_PATH", resultUrl);
			startActivity(targ);
		} else {
			Intent results = new Intent(this, ResultsActivity.class);
			results.putExtra("IMAGE_PATH", imageFilePath);
			results.putExtra("RESULT_PATH", resultUrl);
			startActivity(results);
		}
	}
}
