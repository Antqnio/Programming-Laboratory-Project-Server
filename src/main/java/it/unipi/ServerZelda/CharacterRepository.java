/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import org.springframework.data.repository.CrudRepository;

/**
 * È l'unica classe di entità presenti nel database che non estende GeneralRepository perché dava dei problemi.
 * @author Antonio
 */
public interface CharacterRepository extends CrudRepository<Character, Integer> {
    Character[] findByNameContaining(String keyword);
}
