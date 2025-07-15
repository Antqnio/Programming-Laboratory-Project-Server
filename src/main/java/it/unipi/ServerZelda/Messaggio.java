/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import java.io.Serializable;

/**
 *
 * @author Antonio
 * Stessa classe in sia in Applicazione che in ServerZelda.
 * Oltre a mandare un messaggio (se stampabile nell'interfaccia grafica, contenuto
 * sarà della lingua scelta dal client), manda anche uno status code, utile al
 * client per comunicare l'esito delle operazioni all'utente.
 */
public class Messaggio implements Serializable{
    public String contenuto;
    public int statusCode;
    public Messaggio(String contenuto, int statusCode) {
        this.contenuto = contenuto;
        this.statusCode = statusCode;
    }
}
