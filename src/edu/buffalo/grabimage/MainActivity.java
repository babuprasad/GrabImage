package edu.buffalo.grabimage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.httpclient.HttpException;

import edu.buffalo.hack.image.GoogleResponse;
import edu.buffalo.hack.image.ObjectDetails;
import edu.buffalo.hack.image.ObjectRecognition;
import edu.buffalo.hack.image.Ticker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, Camera.ShutterCallback{

	private Camera cameraObject;
	private ShowCamera showCamera;
	private ImageView pic;
	private ImageView objectImage;
	private Button start;
	private Button capture;
	private Button analyze;
	private Button moreResults;
	private FrameLayout preview;
	private TextView logs;
	private TextView objectTitle;
	private LinearLayout objectDetailsLayout;

	final private String TAG = "GrabImage";
	private String bmpPath = "";
	private String result ="";
	private ProcessImage processImage = new ProcessImage();
	private ObjectDetails mFirstObjectDetails = null;
	private ArrayList<ObjectDetails> mObjectDetailsList = null;
	private ArrayList<ObjectDetails> mAmazonObjDetailsList = null;
	private ArrayList<ObjectDetails> mFinanceObjList = null;
	private TextToSpeech textToSpeech = null;

	public static Camera getCameraObject(){
		Camera object = null;
		try {
			object = Camera.open(); 
			object.setDisplayOrientation(90);
		}
		catch (Exception e){
		}
		return object; 
	}

	private PictureCallback capturedIt = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
			if(bitmap==null){
				Toast.makeText(getApplicationContext(), "Error !", Toast.LENGTH_SHORT).show();
				logs.append("Error in taking image\n");
			}
			else
			{
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap rotatedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
				Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();   
				pic.setImageBitmap(bitmap);
				saveImageToDisk(rotatedBmp);
				logs.append("Image saved to sdcard\n");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pic = (ImageView)findViewById(R.id.imageView1);
		objectImage = (ImageView) findViewById(R.id.objectImage);
		start = (Button) findViewById(R.id.start);
		capture = (Button) findViewById(R.id.capture);
		analyze = (Button) findViewById(R.id.analyze);
		logs = (TextView) findViewById(R.id.logs);
		objectTitle = (TextView) findViewById(R.id.objectTitle);
		cameraObject = getCameraObject();
		objectDetailsLayout = (LinearLayout) findViewById(R.id.objectDetails);
		objectDetailsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mFirstObjectDetails != null) {
					Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
					intent.putExtra("url", mFirstObjectDetails.getObjectUrl());
					startActivity(intent);
				}
			}
		});
		Parameters params = cameraObject.getParameters();
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);		
		showCamera = new ShowCamera(this, cameraObject);
		preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i(TAG, "Trying to autofocus camera...");
				cameraObject.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						Log.i(TAG, "Autofocus : " +success);
					}
				});
				return false;
			}
		});
		preview.addView(showCamera);
		capture.setOnClickListener(this);
		analyze.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Thread analyzeThread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							String finalUrl = processImage.getImageToken(bmpPath);
							if (finalUrl == null)
								addToLogs("Invalid token !");
							else {
								for (int i = 0; i < 5; i++) {
									try {
										addToLogs("Processing image ...");
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									result = processImage.processImage(finalUrl);
									addToLogs((result != null)?result:"Not recognized. Retrying " +(i+1)+ " ...");
									if(result != null) {
										result = result.replaceAll("\\d+\\s*.*", "");
										textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
										addToLogs("Fetching product details...");
										try {
											//ObjectRecognition objr = new ObjectRecognition();
											//mObjectDetailsList = objr.getObjectDetails(result);
											
											GoogleResponse googleRespose = new GoogleResponse();
											mObjectDetailsList = googleRespose.getRelavantItems(result);
											if(mObjectDetailsList != null) {
												mFirstObjectDetails = mObjectDetailsList.get(0);
												if(mFirstObjectDetails.getObjectImage() == "")
													mFirstObjectDetails = mObjectDetailsList.get(1);
												if(mFirstObjectDetails.getObjectImage() == "")
													mFirstObjectDetails = mObjectDetailsList.get(2);
												Log.i(TAG, "Object Title: "+ mFirstObjectDetails.getObjectTitle());
												Log.i(TAG, "Object Image: "+ mFirstObjectDetails.getObjectImage());
												Log.i(TAG, "Object Url  : "+ mFirstObjectDetails.getObjectUrl());
												Log.i(TAG, "Relevant Url: " + mFirstObjectDetails.getRelevantUrl());
												URL url = new URL(mFirstObjectDetails.getObjectImage());
												Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
												updateObjectDetails(mFirstObjectDetails.getObjectTitle(), image);
												addToLogs("product details updated.");
											}
											else
											{
												updateObjectDetails("No matching products found !", null);	
												addToLogs("No matching products found !.");
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									}
								}
							}
						}
						catch (FileNotFoundException e) {
							addToLogs("Not recognized !");
							e.printStackTrace();
						}
					}
				});
				analyzeThread.start();
				resetObjectDetails();
				logs.append("Generating image token...\n");
			}
		});
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cameraObject.startPreview();
				logs.setText("");
				resetObjectDetails();
			}
		});
		textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					textToSpeech.setLanguage(Locale.UK);
				}				
			}
		});
		moreResults = (Button) findViewById(R.id.moreResults);
		moreResults.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logs.append("Generating relevant results...\n");
				Thread googleRequestThread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							//GoogleResponse googleResponse = new GoogleResponse();
							//ArrayList<ObjectDetails> objectDetailsList = googleResponse.getRelavantItems(result);
							//if(mAmazonObjDetailsList == null || (mAmazonObjDetailsList != null && mAmazonObjDetailsList.size() == 0))
							mAmazonObjDetailsList = new ObjectRecognition().getObjectDetails(result);
							mFinanceObjList = Ticker.getStockDetails(result);
							addToLogs("search results retrieved.\n");
							Intent intent = new Intent(getApplicationContext(), ListActivity.class);
							intent.putParcelableArrayListExtra("objectDetailsList", mObjectDetailsList);
							intent.putParcelableArrayListExtra("amazonObjectList", mAmazonObjDetailsList);
							intent.putParcelableArrayListExtra("financeList", mFinanceObjList);
							startActivity(intent);
						} catch (Exception e) {
							addToLogs("Error in retrieving results !\n");
							e.printStackTrace();
						}
					}
				});
				googleRequestThread.start();

			}
		});
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cameraObject.stopPreview();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//cameraObject = getCameraObject();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cameraObject.release();
	}

	public void resetObjectDetails()
	{
		objectImage.setImageBitmap(null);
		objectImage.refreshDrawableState();
		objectTitle.setText("");
	}

	public void snapIt(View view){
		cameraObject.takePicture(this , null, capturedIt);
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



	@Override
	public void onClick(View v) {
		snapIt(v);
	}



	@Override
	public void onShutter() {
		// TODO Auto-generated method stub

	}

	public void saveImageToDisk(Bitmap bmp)
	{
		FileOutputStream out = null;
		try {
			String dir = "/sdcard/Pictures/GrabImage";
			bmpPath = dir + "/image.png";
			File file = new File(dir);
			file.mkdirs();
			out = new FileOutputStream(bmpPath);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addToLogs(final String message) 
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				logs.append(message + "\n");
			}
		});
	}

	public void updateObjectDetails(final String title, final Bitmap bmp)
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				objectImage.setImageBitmap(bmp);
				objectTitle.setText(title);
				objectImage.refreshDrawableState();
			}
		});
	}

}
