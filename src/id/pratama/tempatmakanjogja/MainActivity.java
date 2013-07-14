package id.pratama.tempatmakanjogja;

import id.pratama.tempatmakanjogja.entity.TempatMakan;
import java.util.List;
import org.json.JSONObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnInfoWindowClickListener
{
	private GoogleMap			map;
	private JSONHelper			json;
	private ProgressDialog		pDialog;
	private LatLng				myLocation;

	private List<TempatMakan>	listTempatMakan;
	private final String		URL_API			= "http://api.pratamawijaya.com/tempatmakan.php";

	public static final String	KEY_NAMA		= "nama";
	public static final String	KEY_ALAMAT		= "alamat";
	public static final String	KEY_LAT_TUJUAN	= "lat_tujuan";
	public static final String	KEY_LNG_TUJUAN	= "lng_tujuan";
	public static final String	KEY_LAT_ASAL	= "lat_asal";
	public static final String	KEY_LNG_ASAL	= "lng_asal";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		json = new JSONHelper();

		if (isKonekInternet())
		{
			new AsynTaskMain().execute();

			setupMapIfNeeded();

		} else
		{
			ShowAlert(this, "Warning", "Anda tidak tersambung dengan internet");
		}
	}

	private void setupMapIfNeeded()
	{
		if (map == null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.maps);
			map = supportMapFragment.getMap();

			if (map != null)
			{
				setupMap();
			}
		}

	}

	/*
	 * Cek internet connection
	 */
	private boolean isKonekInternet()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null)
		{
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}

		}
		return false;
	}

	public void ShowAlert(Context context, String title, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		alertDialog.show();

	}

	private void setupMap()
	{
		map.setMyLocationEnabled(true);
		map.setOnInfoWindowClickListener(this);
		moveToMyLocation();
	}

	private void moveToMyLocation()
	{

		if (map.getMyLocation() != null)
		{
			Toast.makeText(this, "" + map.getMyLocation().getLatitude(), Toast.LENGTH_SHORT).show();

			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map
					.getMyLocation().getLongitude()), 15));

		}

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (resCode != ConnectionResult.SUCCESS)
		{
			GooglePlayServicesUtil.getErrorDialog(resCode, this, 1);
		}
	}

	private class AsynTaskMain extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO Auto-generated method stub
			pDialog.dismiss();
			runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					for (int i = 0; i < listTempatMakan.size(); i++)
					{

						map.addMarker(new MarkerOptions()
								.position(new LatLng(listTempatMakan.get(i).getLat(),
										listTempatMakan.get(i).getLng()))
								.title(listTempatMakan.get(i).getNama())
								.snippet(listTempatMakan.get(i).getAlamat()));

					}
				}
			});

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading....");
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			// TODO Auto-generated method stub

			JSONObject jObject = json.getJSONFromURL(URL_API);
			listTempatMakan = json.getTempatMakanAll(jObject);
			return null;
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker)
	{
		// marker id -> m0, m1, m2 dst..
		String id = marker.getId();
		id = id.substring(1);

		myLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());

		if (myLocation != null)
		{
			Bundle bundle = new Bundle();
			bundle.putString(KEY_NAMA, listTempatMakan.get(Integer.parseInt(id)).getNama());
			bundle.putString(KEY_ALAMAT, listTempatMakan.get(Integer.parseInt(id)).getAlamat());
			bundle.putDouble(KEY_LAT_TUJUAN, marker.getPosition().latitude);
			bundle.putDouble(KEY_LNG_TUJUAN, marker.getPosition().longitude);
			bundle.putDouble(KEY_LAT_ASAL, myLocation.latitude);
			bundle.putDouble(KEY_LNG_ASAL, myLocation.longitude);

			Intent i = new Intent(MainActivity.this, InfoTempatMakanActivity.class);
			i.putExtras(bundle);
			startActivity(i);

		} else
		{
			Toast.makeText(this, "Tidak dapat menemukan lokasi anda ", Toast.LENGTH_LONG).show();
		}
	}
}
