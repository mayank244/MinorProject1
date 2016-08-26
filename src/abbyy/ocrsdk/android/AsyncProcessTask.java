package abbyy.ocrsdk.android;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.abbyy.ocrsdk.*;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
public class AsyncProcessTask extends AsyncTask<String, String, Boolean> {
int flag=MainActivity.getFlag();                /////////
Task task;                                      /////////

	public AsyncProcessTask(ResultsActivity activity) {
		this.activity = activity;
		dialog = new ProgressDialog(activity);
	}

	private ProgressDialog dialog;
	/** application context. */
	private final ResultsActivity activity;

	protected void onPreExecute() {
		dialog.setMessage("Processing");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void onPostExecute(Boolean result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		activity.updateResults(result);
	}

	@Override
	protected Boolean doInBackground(String... args) {

		String inputFile = args[0];
		String outputFile = args[1];

		try {
			Client restClient = new Client();


			restClient.applicationId = "asdfghjl";
			restClient.password = "wmPzypOo5iCMyJSelRO+wIQg";

			SharedPreferences settings = activity.getPreferences(Activity.MODE_PRIVATE);
			String instIdName = "installationId";
			if( !settings.contains(instIdName)) {
				String deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

				publishProgress( "First run: obtaining installation id..");
				String installationId = restClient.activateNewInstallation(deviceId);
				publishProgress( "Done. Installation id is '" + installationId + "'");

				SharedPreferences.Editor editor = settings.edit();
				editor.putString(instIdName, installationId);
				editor.commit();
			}

			String installationId = settings.getString(instIdName, "");
			restClient.applicationId += installationId;

			publishProgress( "Uploading image...");

			String language = "English";

			ProcessingSettings processingSettings = new ProcessingSettings();
			processingSettings.setOutputFormat( ProcessingSettings.OutputFormat.txt);
			processingSettings.setLanguage(language);
			processingSettings.setTextType("normal,ocrA,ocrB,typewriter,handprinted");

			publishProgress("Uploading..");

		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "OCR OUTPUTS");

		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            return null;
		        }
		    }
		   
		    String filePath = mediaStorageDir.getPath() + File.separator + "OCRimage.jpg";


			BusCardSettings busCardSettings = new BusCardSettings();
			busCardSettings.setLanguage(language);
			busCardSettings.setOutputFormat(BusCardSettings.OutputFormat.vCard);

			TextFieldSettings tf=new TextFieldSettings();
			tf.setLanguage("English,Digits");
			tf.setTextType("normal,ocrB,typewriter,handprinted");
	//		tf.setLetterset("abcdefghijklmnopqrstuvwxyz0123456789+-=*^/");
			tf.setRegion("-1,-1,-1,-1");

		//	remove for barcode                                                                    /////////
		//	BarcodeSettings brc=new BarcodeSettings();
		//	brc.setType(BarcodeSettings.barcodeType);

			Task task1 = restClient.processBusinessCard(filePath, busCardSettings);                /////////
	    	Task task2 = restClient.processImage(inputFile, processingSettings);                   /////////
       //   Task task3 = restClient.processBarcodeField(filePath, brc);                            //////////////
            Task task4 = restClient.processTextField(filePath, tf);

			if (flag==0||flag==3||flag==2)                                                         ///////
			{task=task2;}                                                                          /////////
			else if(flag==1)                                                                       /////////
			{task=task1;}                                                                         /////////
		//	else if(flag==2)
		//	{task=task4;}
	//		else if(flag==4)
	//		{task=task3;}



			while( task.isTaskActive() ) {

				Thread.sleep(5000);
				publishProgress( "Waiting.." );
				task = restClient.getTaskStatus(task.Id);
			}

			if( task.Status == Task.TaskStatus.Completed ) {
				publishProgress( "Downloading.." );

				FileOutputStream fos = activity.openFileOutput(outputFile,Context.MODE_PRIVATE);

				try {
					restClient.downloadResult(task, fos);
				} finally {
					fos.close();
				}

				publishProgress( "Ready" );
			} else if( task.Status == Task.TaskStatus.NotEnoughCredits ) {
				throw new Exception( "Not enough credits to process task. Add more pages to your application's account." );
			} else {
				throw new Exception( "Task failed" );
			}

			return true;
		} catch (Exception e) {
			final String message = "Error1: " + e.getMessage();
			publishProgress( message);
			activity.displayMessage(message);
			return false;
		}
	}

	@Override
	protected void onProgressUpdate(String... values)
	{
		// TODO Auto-generated method stub
		String stage = values[0];
		dialog.setMessage(stage);
		// dialog.setProgress(values[0]);
	
	}
}
