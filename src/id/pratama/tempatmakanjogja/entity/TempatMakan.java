/*
 * Pratama Nur Wijaya (c) 2013 
 * 
 * Project       : Tempat Makan Jogja
 * Filename      : TempatMakan.java
 * Creation Date : Apr 7, 2013 time : 1:54:50 PM
 *
 */

package id.pratama.tempatmakanjogja.entity;

public class TempatMakan
{
	private int		id;
	private String	nama;
	private String	alamat;
	private double	lat;
	private double	lng;

	public TempatMakan()
	{
		// do nothing
	}

	public TempatMakan(int id, String nama, String alamat, double lat, double lng)
	{
		super();
		this.id = id;
		this.nama = nama;
		this.alamat = alamat;
		this.lat = lat;
		this.lng = lng;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNama()
	{
		return nama;
	}

	public void setNama(String nama)
	{
		this.nama = nama;
	}

	public String getAlamat()
	{
		return alamat;
	}

	public void setAlamat(String alamat)
	{
		this.alamat = alamat;
	}

	public double getLat()
	{
		return lat;
	}

	public void setLat(double lat)
	{
		this.lat = lat;
	}

	public double getLng()
	{
		return lng;
	}

	public void setLng(double lng)
	{
		this.lng = lng;
	}

}
