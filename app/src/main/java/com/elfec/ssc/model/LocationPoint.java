package com.elfec.ssc.model;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.ssc.model.enums.LocationPointType;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Guarda la información de los puntos de pago para mostrarlos en el mapa
 *
 * @author Diego
 */
@Table(name = "LocationPoints")
public class LocationPoint extends Model {

    @Column(name = "InstitutionName", notNull = true)
    private String institutionName;

    @Column(name = "Address", notNull = true)
    private String address;

    @Column(name = "Phone", notNull = true)
    private String phone;

    @Column(name = "StartAttention")
    private String startAttention;

    @Column(name = "EndAttention")
    private String endAttention;

    @Column(name = "Latitude", notNull = true)
    private double latitude;

    @Column(name = "Longitude", notNull = true)
    private double longitude;

    /**
     * Los diferentes tipos de puntos de ubicación según {@link LocationPointType} representados
     * en enteros
     */
    @Column(name = "Type", notNull = true, index = true)
    private short type;

    @Column(name = "Status", notNull = true)
    private short status;

    @SerializedName("created_at")
    @Column(name = "InsertDate", notNull = true)
    private DateTime insertDate;

    @SerializedName("updated_at")
    @Column(name = "UpdateDate")
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

    public static List<LocationPoint> getPointsByType(LocationPointType type) {
        return new Select().from(LocationPoint.class).where("type=?", type.toShort()).execute();
    }

    /**
     * Calcula la distancia en metros desde una ubicación de gps y este punto de ubicación
     *
     * @param location
     * @return
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
     * @param location
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

    /**
     * Compara todos los puntos
     *
     * @param point
     * @return
     */
    public static boolean existPoint(LocationPoint point) {
        return new Select().from(LocationPoint.class)
                .where("institutionName=? and address=? and phone=? and "
                                + "startAttention=? and endAttention=? and "
                                + "latitude=? And longitude=? and type=? ",
                        point.institutionName, point.address, (point.phone == null ? "NULL" : point.phone),
                        (point.startAttention == null ? "NULL" : point.startAttention),
                        (point.endAttention == null ? "NULL" : point.endAttention),
                        point.latitude, point.longitude, point.type).executeSingle() != null;
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
