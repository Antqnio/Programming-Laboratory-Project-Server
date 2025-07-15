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
public interface GameRepository extends CrudRepository<Game, Integer>, GenericRepository<Game> {
    
}
