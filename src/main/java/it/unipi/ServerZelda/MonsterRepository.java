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
public interface MonsterRepository extends CrudRepository<Monster, Integer>, GenericRepository<Monster> {
    
}
