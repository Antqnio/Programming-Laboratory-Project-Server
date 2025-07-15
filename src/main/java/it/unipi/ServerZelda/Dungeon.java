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
 * JavaBean della classe Dungeon + attributi per JPA.
 */
@Entity
@Table(name="dungeons", schema=dbName)
public class Dungeon implements Serializable{
    @Id
    @Column(name="name")
    private String name;
    @Column(name="description", length = 1000)
    private String description;


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

    

    public Dungeon(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public Dungeon() {
        
    }
}
