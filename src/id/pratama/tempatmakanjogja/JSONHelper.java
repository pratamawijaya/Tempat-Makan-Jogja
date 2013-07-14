/*
 * Pratama Nur Wijaya (c) 2013 
 * 
 * Project       : Tempat Makan Jogja
 * Filename      : JSONHelper.java
 * Creation Date : Apr 7, 2013 time : 1:47:27 PM
 *
 */

package id.pratama.tempatmakanjogja;

import id.pratama.tempatmakanjogja.entity.TempatMakan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class JSONHelper
{
	private InputStream		is				= null;
	private JSONObject		jsonObject		= null;
	private String			json			= "";

	private final String	TAG_TEMPATMAKAN	= "tempatmakan";
	private final String	TAG_ID			= "id";
	private final String	TAG_NAMA		= "nama";
	private final String	TAG_ALAMAT		= "alamat";
	private final String	TAG_LAT			= "lat";
	private final String	TAG_LNG			= "lng";
	private final String	TAG_ROUTES		= "routes";
	private final String	TAG_LEGS		= "legs";
	private final String	TAG_STEPS		= "steps";
	private final String	TAG_POLYLINE	= "polyline";
	private final String	TAG_POINTS		= "points";
	private final String	TAG_START		= "start_location";
	private final String	TAG_END			= "end_location";

	public JSONObject getJSONFromURL(String url)
	{
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}

			is.close();
			json = sb.toString();
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		try
		{
			jsonObject = new JSONObject(json);

		} catch (JSONException e)
		{
			// TODO: handle exception
		}

		return jsonObject;
	}

	public ArrayList<TempatMakan> getTempatMakanAll(JSONObject jobj)
	{
		ArrayList<TempatMakan> listTempatMakan = new ArrayList<TempatMakan>();

		try
		{
			JSONArray arrayTempatMakan = jobj.getJSONArray(TAG_TEMPATMAKAN);

			for (int i = 0; i < arrayTempatMakan.length(); i++)
			{
				JSONObject jobject = arrayTempatMakan.getJSONObject(i);

				Log.d("log", "muter ke " + i);
				listTempatMakan.add(new TempatMakan(jobject.getInt(TAG_ID), jobject.getString(TAG_NAMA), jobject
						.getString(TAG_ALAMAT), jobject
						.getDouble(TAG_LAT), jobject.getDouble(TAG_LNG)));

			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return listTempatMakan;
	}

	/*
	 * Untuk decode Polyline
	 * 
	 * @params String
	 * 
	 * @return List<LatLng>
	 */
	private List<LatLng> decodePoly(String encoded)
	{
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len)
		{
			int b, shift = 0, result = 0;
			do
			{
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do
			{
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng(lat / 1E5, lng / 1E5);
			poly.add(position);
		}
		return poly;

	}

	/*
	 * Untuk mendapatkan direction
	 * 
	 * @params JSONObject
	 * 
	 * @return List<LatLng>
	 */
	public List<LatLng> getDirection(JSONObject jObj)
	{

		List<LatLng> directions = new ArrayList<LatLng>();

		try
		{
			JSONObject objRoute = jObj.getJSONArray(TAG_ROUTES).getJSONObject(0);
			JSONObject objLegs = objRoute.getJSONArray(TAG_LEGS).getJSONObject(0);
			JSONArray arraySteps = objLegs.getJSONArray(TAG_STEPS);
			for (int wi2t = 0; wi2t < arraySteps.length(); wi2t++)
			{
				JSONObject step = arraySteps.getJSONObject(wi2t);
				JSONObject objStart = step.getJSONObject(TAG_START);
				JSONObject objEnd = step.getJSONObject(TAG_END);
				double latStart = objStart.getDouble(TAG_LAT);
				double lngStart = objStart.getDouble(TAG_LNG);

				directions.add(new LatLng(latStart, lngStart));

				JSONObject poly = step.getJSONObject(TAG_POLYLINE);
				String encodedPoly = poly.getString(TAG_POINTS);

				List<LatLng> decodedPoly = decodePoly(encodedPoly);
				for (int eka = 0; eka < decodedPoly.size(); eka++)
				{
					directions.add(new LatLng(decodedPoly.get(eka).latitude, decodedPoly.get(eka).longitude));
				}

				double latEnd = objEnd.getDouble(TAG_LAT);
				double lngEnd = objEnd.getDouble(TAG_LNG);
				directions.add(new LatLng(latEnd, lngEnd));

			}
		} catch (JSONException e)
		{
			// TODO: handle exception
		}

		return directions;
	}
}
