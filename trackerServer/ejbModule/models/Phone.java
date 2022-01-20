package models;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Smartphone
 *
 */
@Entity

public class Phone implements Serializable {

	   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String imei;
	private String marque;
	@ManyToOne @JoinColumn(name="idUser", nullable=false)
    private User user;
	private static final long serialVersionUID = 1L;

	public Phone() {
		super();
	}   
	
	public Phone(String imei, String marque) {
		super();
		this.imei = imei;
		this.marque = marque;
	}
	
	public Phone(int idPhone,String imei, String marque) {
		super();
		this.id= idPhone;
		this.imei = imei;
		this.marque = marque;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}   
	public String getMarque() {
		return this.marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}
	
   
}
