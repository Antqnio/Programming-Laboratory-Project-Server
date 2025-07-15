/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

/**
 *
 * @author Antonio
 * Usata per inviare stringhe tramite oggetti codificati (in JSON) tra client e server.
 * Stessa classe sia in ServerZelda che in Applicazione.
 */
public class SearchClass {
    public String keyword;

    public SearchClass(String keyword) {
        this.keyword = keyword;
    }
    
    public SearchClass() {
        
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    
}
