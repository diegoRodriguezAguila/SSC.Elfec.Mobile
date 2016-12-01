package com.elfec.ssc.model;

import android.location.Location;

import com.elfec.ssc.model.enums.LocationPointType;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Guarda la información de los puntos de pago para mostrarlos en el mapa
 *
 * @author Diego
 */
public class LocationPoint{
    private String institutionName;
    private String address;
    private String phone;
    private String startAttention;
    private String endAttention;
    private double latitude;
    private double longitude;
    /**
     * Los diferentes tipos de puntos de ubicación según {@link LocationPointType} representados
     * en enteros
     */
    private short type;
    private short status;
    @SerializedName("created_at")
    private DateTime insertDate;
    @SerializedName("updated_at")
    private DateTime updateDate;


    public LocationPoint() {
        super();
        status = 1;
    }

    /**
     * Inicializa un punto de pago con los parámetros especificados y con status = 1
     * @param institutionName institutionName
     * @param address address
     * @param phone phone
     * @param startAttention startAttention
     * @param endAttention endAttention
     * @param latitude latitude
     * @param longitude longitude
     * @param type type
     */
    public LocationPoint(String institutionName, String address, String phone, String startAttention,
                         String endAttention, double latitude, double longitude, short type) {
        super();
        this.institutionName = institutionName;
        this.address = address;
        this.phone = phone;
        this.startAttention = startAttention;
        this.endAttention = endAttention;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.status = (short) 1;
    }

    /**
     * Calcula la distancia en metros desde una ubicación de gps y este punto de ubicación
     *
     * @param location location
     * @return distance
     */
    public double distanceFrom(Location location) {
        Location thisLocation = new Location(address);
        thisLocation.setLatitude(latitude);
        thisLocation.setLongitude(longitude);
        return location.distanceTo(thisLocation);
    }

    /**
     * Compara todos los atributos de locationpoint
     *
     * @param location location
     * @return si son iguales da true
     */
    public boolean compare(LocationPoint location) {
        return institutionName.equals(location.institutionName) &&
                address.equals(location.address) &&
                phone.equals(location.phone) &&
                startAttention.equals(location.startAttention) &&
                endAttention.equals(location.endAttention) &&
                latitude == location.latitude &&
                longitude == location.longitude &&
                type == location.type;

    }

    //region Getters y Setters
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartAttention() {
        return startAttention;
    }

    public void setStartAttention(String startAttention) {
        this.startAttention = startAttention;
    }

    public String getEndAttention() {
        return endAttention;
    }

    public void setEndAttention(String endAttention) {
        this.endAttention = endAttention;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationPointType getType() {
        return LocationPointType.get(type);
    }

    public void setType(LocationPointType type) {
        this.type = type.toShort();
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public DateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(DateTime insertDate) {
        this.insertDate = insertDate;
    }

    public DateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
    }

    //endregion

}
