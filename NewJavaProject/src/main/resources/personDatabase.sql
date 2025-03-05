/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  milolad
 * Created: 20 Feb 2025
 */

CREATE TABLE IF NOT EXISTS person (
    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
    lastname VARCHAR(45) NOT NULL,  
    firstname VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    phone_number VARCHAR(15) NULL,
    address VARCHAR(200) NULL,
    email_address VARCHAR(150) NULL,
    birth_date DATE NULL

);
