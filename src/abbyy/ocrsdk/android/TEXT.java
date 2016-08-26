package abbyy.ocrsdk.android;

import com.abbyy.ocrsdk.TextFieldSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TEXT extends Activity{

	EditText e=(EditText)this.findViewById(R.id.editText1);
	Button b=(Button)this.findViewById(R.id.button1);
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textfld);
		
		final String s=e.getText().toString();
		
		b.setOnClickListener(new View.OnClickListener() {
	        
        	public void onClick(View v) {
        		 Intent results = new Intent(getApplicationContext(),ResultsActivity.class);
        	    	results.putExtra("TEXT", s);
        	    	
        	    	startActivity(results);	    
        	    	finish();}
        });
		
	}

	
	
	
}
