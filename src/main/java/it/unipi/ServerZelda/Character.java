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
import static it.unipi.ServerZelda.ServerZeldaApplication.dbName;




/**
 *
 * @author Antonio
 * JavaBean della classe Character + attributi per JPA.
 */
@Entity
@Table(name="characters", schema=dbName)
public class Character implements Serializable{
    @Id
    @Column(name="name")
    private String name;
    @Column(name="description", length = 1000)
    private String description;
    @Column(name="gender")
    private String gender;
    @Column(name="race")
    private String race;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }


    

    public Character(String name, String description, String gender, String race) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.race = race;
    }

    public Character() {
        
    }
}
