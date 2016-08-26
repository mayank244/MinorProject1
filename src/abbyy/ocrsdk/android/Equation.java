package abbyy.ocrsdk.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class Equation extends AsyncTask<String, Void, String> {

  //  static String appids = "&appid=7YJPAA-37EK5QYGY2&format=plaintext";
     ResultsActivity r;
     String ip,iip,fp;
     String myurl=  "http://api.wolframalpha.com/v2/query?&appid=7YJPAA-37EK5QYGY2&format=plaintext&includepodid=*Solution&input=";
     public Equation(Context c) {
       r=(ResultsActivity)c;
     }
     
    protected void onPreExecute() {
    //    progressBar.setVisibility(View.VISIBLE);
    //    responseView.setText("");
    }

    protected String doInBackground(String... urls) {
        // Do some validation here
    	
    	
    	
    		{ip=urls[0].replaceAll("\\s+","");
    	    iip=r.quad(ip);
    	    fp=iip.replaceAll("\\+","%2B");
    		}
        try {
            URL url = new URL((myurl+fp));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
           //     return stringBuilder.toString();
                StringBuilder xml = new StringBuilder();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader (stringBuilder.toString()));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
       /*          if(eventType == XmlPullParser.START_DOCUMENT) {
                     xml.append("Start document").append(System.getProperty("line.separator"));
                 } else if(eventType == XmlPullParser.END_DOCUMENT) {
                     xml.append("End document").append(System.getProperty("line.separator"));
                 } else if(eventType == XmlPullParser.START_TAG) {
                	 xml.append("Start tag "+xpp.getName()).append(System.getProperty("line.separator"));
                 } else if(eventType == XmlPullParser.END_TAG) {
                	 xml.append("End tag "+xpp.getName()).append(System.getProperty("line.separator")); */
                   if(eventType == XmlPullParser.TEXT ) {
                	 xml.append(xpp.getText()).append(System.getProperty("line.separator"));
                	 
                 }
                eventType = xpp.next();
                }
                return xml.toString();
                }
        
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
//        progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
   //     responseView.setText(response);
        r.displayMessage("INPUT:"+iip+"\n"+response);
    }
}
