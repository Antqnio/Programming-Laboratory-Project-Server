/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

/**
 *
 * @author Antonio
 * @param <T>
 * Usata per riusare il codice della findByName. Non funziona con la classe Character,
 * unica a non estendere GenericRepository.
 */

public interface GenericRepository<T> {
    T[] findByNameContaining(String keyword);
}
