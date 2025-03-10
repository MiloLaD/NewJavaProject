package com.MyProject.newjavaproject;  

import java.util.Date;

public class Person {
    private int idperson;
    private String lastname;
    private String firstname;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private Date birthDate;
    private String profilePicture;

 
    public Person(int idperson, String lastname, String firstname, String nickname, String phoneNumber,
                  String address, String emailAddress, Date birthDate,String profilePicture) {
        this.idperson = idperson;
        this.lastname = lastname;
        this.firstname = firstname;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
    }
    public Person(int idperson, String lastname, String firstname, String nickname, String phoneNumber,
            String address, String emailAddress, Date birthDate) {
    	this(idperson, lastname, firstname, nickname, phoneNumber, address, emailAddress, birthDate, null);
    }

    // Getters et Setters
    public int getIdperson() {
        return idperson;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Le numéro doit contenir 10 chiffres.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;  
    }

    public void setFirstname(String firstname) {
        if (firstname == null || firstname.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        if (lastname == null || lastname.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.lastname = lastname;
    }

 
    public String getProfilePicture() {
        return profilePicture;
    }

    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
	public void setEmailAddress(String newEmail) {
		this.emailAddress = newEmail;
		
	}
	public void setAddress(String newAddress) {
		this.address = newAddress;
		
	}
	public void setBirthDate(Date from) {
		this.birthDate = from;	
	}

   
}
