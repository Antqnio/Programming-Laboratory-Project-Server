/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
//import java.sql.Date; //Questa dà problemi.
import java.util.Date;
import static it.unipi.ServerZelda.ServerZeldaApplication.dbName;





/**
 *
 * @author Antonio
 * JavaBean della classe Game + attributi per JPA.
 */
@Entity
@Table(name="games", schema=dbName)
public class Game implements Serializable{
    @Id
    @Column(name="name")
    private String name;
    @Column(name="description", length = 1000)
    private String description;
    @Column(name="released_date", columnDefinition = "DATE")
    private Date released_date;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getReleaseDate() {
        return released_date;
    }

    public void setReleaseDate(Date releaseDate) {
        this.released_date = releaseDate;
    }

    public Game(String name, String description, Date release_date) {
        this.name = name;
        this.description = description;
        this.released_date = release_date;
    }
    
    public Game() {
        
    }
}
