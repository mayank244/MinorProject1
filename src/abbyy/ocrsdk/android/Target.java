package abbyy.ocrsdk.android;



	import android.os.Bundle;
	import android.view.Menu;
	import android.view.MenuItem;
import android.app.Activity;
import android.app.AliasActivity;
import android.content.Intent;
import android.os.AsyncTask;
	import android.os.Bundle;
	import android.util.Log;
	import android.view.View;
	import android.widget.AdapterView;
	import android.widget.ArrayAdapter;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.ProgressBar;
	import android.widget.Spinner;
	import android.widget.TextView;
	import android.widget.Toast;
	import java.lang.String;
	import org.json.JSONArray;
	import org.json.JSONException;
	import org.json.JSONObject;
	import org.json.JSONTokener;

	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;
	import java.util.ArrayList;
	import java.util.List;
	import android.widget.AdapterView.OnItemSelectedListener;



	public class Target extends Activity implements OnItemSelectedListener {

	    EditText emailText;
	    TextView responseView;
	    ProgressBar progressBar;
	   // static final String API_KEY = "AIzaSyB82Q-nTI80_F70k3D9axlEfE1Z0F8cbC4";
	String a,b,c;
	  //  static final String API_URL = "https://www.googleapis.com/language/translate/v2?";

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.target);


	        Bundle extras = getIntent().getExtras();
			if( extras != null) {
				b = extras.getString("IMAGE_PATH" );
				c = extras.getString( "RESULT_PATH" );
			}
	        Button queryButton = (Button) findViewById(R.id.queryButton);
	        queryButton.setOnClickListener(new View.OnClickListener() {
	        
	        	public void onClick(View v) {
	        		 Intent results = new Intent(getApplicationContext(),ResultsActivity.class);
	        	    	results.putExtra("TARGET", a);
	        	    	results.putExtra("IMAGE_PATH", b);
	        	    	results.putExtra("RESULT_PATH", c);

	        	    	startActivity(results);	    
	        	    	finish();}
	        });
	        // Spinner element
	        
	        Spinner spinner = (Spinner) findViewById(R.id.spinner);

	        // Spinner click listener
	        spinner.setOnItemSelectedListener(this);

	        // Spinner Drop down elements
	        List<String> categories = new ArrayList<String>();
	        categories.add("English");
	        categories.add("German");
	        categories.add("French");
	        categories.add("Spanish");
	        categories.add("Dutch");
	        categories.add("Hindi");

	        // Creating adapter for spinner
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	        // attaching data adapter to spinner
	        spinner.setAdapter(dataAdapter);
	    }
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	        // On selecting a spinner item
	        String item = parent.getItemAtPosition(position).toString();
	if(item =="Hindi")
	    a="hi";
	        else if(item =="German")
	    a="de";
	        else if(item =="English")
	    a="en";
	        else if(item =="French")
	    a="fr";
	        else if(item =="Dutch")
	    a="nl";
	        // Showing selected spinner item
	Log.i("ASd",a);
	        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
	    }
	    public void onNothingSelected(AdapterView<?> arg0) {
	        // TODO Auto-generated method stub
	    }
}
