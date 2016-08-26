package abbyy.ocrsdk.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("DefaultLocale")
public class ResultsActivity extends Activity {
  
	String outputPath;
	TextView tv;
	File mediaStorageDir; 
	String filename="OCRtext"; 
	Button b1,b2;
	int ee=0,tflag=0;
	File file=null;
	StringBuffer contents = new StringBuffer();  
	String text = null;
	String extention;
	int flag=MainActivity.getFlag();
	String target,textfld="";
	
	
	{
	if(flag==0||flag==2||flag==3||flag==4)               ///////////
	{
	extention=".txt";
	}
	else if(flag==1)
	{extention=".vcf";}
	}                      ///////////
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);                  ////////////////

		
		tv = (TextView)this.findViewById(R.id.textView1);          //////////////////
		tv.setMovementMethod(new ScrollingMovementMethod());       /////////////////
		String imageUrl = "unknown";
		
		b1=(Button)this.findViewById(R.id.fromEqnCam);             ///////////////
		b1.setOnClickListener(save);                               /////////////////
		b2=(Button)this.findViewById(R.id.fromEqnFile);            ///////////////
		b2.setOnClickListener(edit);                               /////////////////
		
		Bundle extras = getIntent().getExtras();
		if( extras != null) {
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
			target=extras.getString("TARGET");
			textfld=extras.getString("TEXT");
		}
		
		if(textfld!="")
		{tflag=1;}
		// Starting recognition process

		new AsyncProcessTask(this).execute(imageUrl, outputPath);
	}
                      
	
	
	
	public void updateResults(Boolean success) {
		if (!success)
			return;
		try {
				
			 contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				
				while ((text = bufReader.readLine()) != null) {
				if(flag==2||flag==3)
						{contents.append(text);}
				else
					{contents.append(text).append(System.getProperty("line.separator"));
							
					}
				}
			} 
			
			
			finally {
				fis.close();
			}
			
		if(flag==2)
		{
			Equation e=new Equation(this);
			e.execute(contents.toString(),textfld);
		}
		else if(flag==3)
		{
			Translator t=new Translator(this);
			t.execute(contents.toString(),target,textfld);
		}
		else
			    {			
			      displayMessage(contents.toString());
		    	}
	  } catch (Exception e) {
			displayMessage("Error3: " + e.getMessage());
		}
	}
	
	
	
	public void displayMessage( String text )
	{
		tv.post( new MessagePoster( text ) );
	}
	
	private OnClickListener save = new OnClickListener() { //////////////////////
        public void onClick(View v) {                             //////////////////
        	
            ee=1;
        	FileOutputStream outputStream;
			try {
				
				mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "OCR OUTPUTS");
				 if (! mediaStorageDir.exists()){
				        if (! mediaStorageDir.mkdirs()){
				            return ;
				        }
				    }
				 Date date = new Date() ;
				 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss") ;
			    file =  new File(mediaStorageDir.getPath() + File.separator +  dateFormat.format(date) + extention);

		 
			    outputStream = new FileOutputStream(file);
			    outputStream.write(tv.getText().toString().getBytes());
			    outputStream.close();
			    Toast.makeText(getApplicationContext(), "FILE SAVED!!", Toast.LENGTH_LONG).show();
			    
			} catch (Exception e) {
			    e.printStackTrace();
			}           
        }                                                                /////////////////////
    };                                    
	
    
    private OnClickListener edit = new OnClickListener() {               //////////////////////
        public void onClick(View v) {                                   //////////////////
        	
   if (ee==0)
   {		
	Toast.makeText(getApplicationContext(), "Save File First", Toast.LENGTH_LONG).show();
    }
    else
    {
        	 Intent intent = new Intent();
             intent.setAction(android.content.Intent.ACTION_VIEW);
             MimeTypeMap mime = MimeTypeMap.getSingleton();
             String ext=file.getName().substring(file.getName().indexOf(".")+1).toLowerCase();
             String type = mime.getMimeTypeFromExtension(ext);
           
             intent.setDataAndType(Uri.fromFile(file),type);
            
             startActivity(intent);
    }
        }                                                           /////////////////////
    };                 
    static String quad(String a)
   {  a=a.toLowerCase();
  int c=0;

  int n=a.length();
      for(int i=0;i<n;i++)
      {
      if(a.charAt(i)>='a' && a.charAt(i)<='z')
       {        

          if(a.charAt(i+1)>='1'&&a.charAt(i+1)<='9')
          { 
             
               c=i+1;
               a=a.substring(0,c)+"^"+a.substring(c,a.length());

          }
       }
      }
     
      return a;
  }
  
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	class MessagePoster implements Runnable {
		public MessagePoster( String message )
		{
			_message = message;
		}

		public void run() {
			tv.setText( _message + "\n" ); 
		}
		public final String _message;
	}
}
