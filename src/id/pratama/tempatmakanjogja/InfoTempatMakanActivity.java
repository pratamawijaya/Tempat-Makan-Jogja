/*
 * Pratama Nur Wijaya (c) 2013 
 * 
 * Project       : Tempat Makan Jogja
 * Filename      : InfoTempatMakanActivity.java
 * Creation Date : Apr 7, 2013 time : 1:47:27 PM
 *
 */

package id.pratama.tempatmakanjogja;

import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InfoTempatMakanActivity extends Activity implements OnClickListener
{

	private TextView	tvNama;
	private TextView	tvAlamat;
	private Button		btnGetDirection;

	private LatLng		lokasiTujuan;
	private LatLng		lokasiAwal;
	private String		nama;
	private String		alamat;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_tempat_makan);

		initialize();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			nama = bundle.getString(MainActivity.KEY_NAMA);
			alamat = bundle.getString(MainActivity.KEY_ALAMAT);
			lokasiTujuan = new LatLng(bundle.getDouble(MainActivity.KEY_LAT_TUJUAN),
					bundle.getDouble(MainActivity.KEY_LNG_TUJUAN));
			lokasiAwal = new LatLng(bundle.getDouble(MainActivity.KEY_LAT_ASAL),
					bundle.getDouble(MainActivity.KEY_LNG_ASAL));
		}

		setTeksView();

	}

	private void setTeksView()
	{
		tvNama.setText(nama);
		tvAlamat.setText(alamat);
	}

	private void initialize()
	{
		tvAlamat = (TextView) findViewById(R.id.alamatTempatMakan);
		tvNama = (TextView) findViewById(R.id.namaTempatMakan);
		btnGetDirection = (Button) findViewById(R.id.btnDirection);
		btnGetDirection.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Bundle bundle = new Bundle();
		bundle.putDouble(MainActivity.KEY_LAT_ASAL, lokasiAwal.latitude);
		bundle.putDouble(MainActivity.KEY_LNG_ASAL, lokasiAwal.longitude);
		bundle.putDouble(MainActivity.KEY_LAT_TUJUAN, lokasiTujuan.latitude);
		bundle.putDouble(MainActivity.KEY_LNG_TUJUAN, lokasiTujuan.longitude);
		bundle.putString(MainActivity.KEY_NAMA, nama);

		Intent intent = new Intent(this, DirectionActivity.class);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
