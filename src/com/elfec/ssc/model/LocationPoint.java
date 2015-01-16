package com.elfec.ssc.model;

import java.util.List;

import org.joda.time.DateTime;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.ssc.model.enums.LocationPointType;

/**
 * Guarda la información de los puntos de pago para mostrarlos en el mapa
 * @author Diego
 */
@Table(name = "LocationPoints")
public class LocationPoint extends Model {
	
	@Column(name = "InstitutionName", notNull = true)
	private String InstitutionName;
	
	@Column(name = "Address", notNull = true)
	private String Address;
	
	@Column(name = "Phone", notNull = true)
	private String Phone;
	
	@Column(name = "StartAttention")
	private String StartAttention;
	
	@Column(name = "EndAttention")
	private String EndAttention;
	
	@Column(name = "Latitude", notNull = true)
	private double Latitude;
	
	@Column(name = "Longitude", notNull = true)
	private double Longitude;
	
	/**
	 * Los diferentes tipos de puntos de ubicación según {@link LocationPointType} representados en enteros
	 */
	@Column(name = "Type", notNull = true)
	private short Type;
	
	@Column(name = "Status", notNull = true)
	private short Status;

	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;

	@Column(name = "UpdateDate")
	private DateTime UpdateDate;

	
	public LocationPoint() {
		super();
	}
	
	/**
	 * Inicializa un punto de pago con los parámetros especificados y con Status = 1
	 * @param address
	 * @param phone
	 * @param startAttention
	 * @param endAttention
	 * @param latitude
	 * @param longitude
	 */
	public LocationPoint(String institutionName, String address, String phone, String startAttention,
			String endAttention, double latitude, double longitude,short type) {
		super();
		this.InstitutionName = institutionName;
		this.Address = address;
		this.Phone = phone;
		this.StartAttention = startAttention;
		this.EndAttention = endAttention;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.Type=type;
		this.Status = (short)1;
	}
	
	public static List<LocationPoint> getPointsByType(LocationPointType type)
	{
		return new Select().from(LocationPoint.class).where("Type=?",type.toShort()).execute();
	}
	
	/**
	 * Calcula la distancia en metros desde una ubicación de gps y este punto de ubicación
	 * @param location
	 * @return
	 */
	public double distanceFrom(Location location)
	{
		Location thisLocation = new Location(Address);
		thisLocation.setLatitude(Latitude);
		thisLocation.setLongitude(Longitude);
		return location.distanceTo(thisLocation);
	}
	
	//#region Getters y Setters
	public String getInstitutionName() {
		return InstitutionName;
	}

	public void setInstitutionName(String institutionName) {
		InstitutionName = institutionName;
	}

	
	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getStartAttention() {
		return StartAttention;
	}

	public void setStartAttention(String startAttention) {
		StartAttention = startAttention;
	}

	public String getEndAttention() {
		return EndAttention;
	}

	public void setEndAttention(String endAttention) {
		EndAttention = endAttention;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	
	public LocationPointType getType() {
		return LocationPointType.get(Type);
	}

	public void setType(LocationPointType type) {
		Type = type.toShort();
	}

	public short getStatus() {
		return Status;
	}

	public void setStatus(short status) {
		Status = status;
	}

	public DateTime getInsertDate() {
		return InsertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		InsertDate = insertDate;
	}

	public DateTime getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(DateTime updateDate) {
		UpdateDate = updateDate;
	}
	
	//#endregion
	
}
