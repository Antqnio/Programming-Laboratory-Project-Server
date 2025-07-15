/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Antonio
 */
public interface CredenzialiRepository extends CrudRepository<Credenziali, Integer>{
    Credenziali findByUsernameAndPassword(String username, String password); //ricerca basata sul campo username.
    Credenziali findByUsername(String name); //usato per indicare all'utente che è corretto lo username ma non la password.
}
