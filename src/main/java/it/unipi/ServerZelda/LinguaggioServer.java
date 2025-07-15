/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 *
 * @author Antonio
 * Serve per inviare i messaggi nella lingua usata dal client
 */
@XStreamAlias("linguaggioServer")
public class LinguaggioServer implements Serializable {
    public String alreadyPopulatedDB;
    public String notExistingDB;
    public String creationSuccess;
    public String genericError;
    public String alreadyExistingUser;
    public String userSignedUp;
    public String userSignedIn;
    public String wrongCredentials;
    public String wrongPassword;
    
}
