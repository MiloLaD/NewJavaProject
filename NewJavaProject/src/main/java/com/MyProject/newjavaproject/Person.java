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

 
    public Person(int idperson, String lastname, String firstname, String nickname, String phoneNumber,
                  String address, String emailAddress, Date birthDate) {
        this.idperson = idperson;
        this.lastname = lastname;
        this.firstname = firstname;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
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

    public void setPhoneNumber(String newNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setNickname(String newNickname) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setFirstname(String newFirstname) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setLastname(String newName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

	public String getProfilePicture() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProfilePicture(String string) {
		// TODO Auto-generated method stub
		
	}

   
}
