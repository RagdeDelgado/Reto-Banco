package com.banco.api.service;

import java.util.List;

import com.banco.api.entity.AccountsEntity;
import com.banco.api.entity.ClientsEntity;
import com.banco.api.util.Log;

import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//@Slf4j
public class AccountsService {
	
	private EntityManagerFactory emf;

    public AccountsService() {
        emf = Persistence.createEntityManagerFactory("banco_pu");        
        Log.info(LoadFilesService.class,"CuentaService initialized");
    }

    public AccountsEntity crearCuenta(AccountsEntity cuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cuenta);
            em.getTransaction().commit();            
            Log.info(LoadFilesService.class,"Cuenta creada: {}", cuenta.getNumeroCuenta());
            return cuenta;
        } catch (Exception e) {
            em.getTransaction().rollback();            
            Log.info(LoadFilesService.class,"Error creando cuenta", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean existeNumeroCuenta(String numero) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(c) FROM Cuenta c WHERE c.numeroCuenta = :numero", Long.class)
                .setParameter("numero", numero)
                .getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public String generarNumeroCuenta() {
        EntityManager em = emf.createEntityManager();
        try {
            Long maxId = em.createQuery(
                    "SELECT COALESCE(MAX(c.idCuenta), 0) FROM Cuenta c", Long.class)
                .getSingleResult();
            String numeroCuenta = "ACC-" + String.format("%010d", maxId + 1);            
            Log.info(LoadFilesService.class,"Número de cuenta generado: {}", numeroCuenta);
            
            return numeroCuenta;
        } finally {
            em.close();
        }
    }
	
    
    public AccountsEntity create(AccountsEntity cuenta) {
    	EntityManager em = emf.createEntityManager();    
        em.persist(cuenta);
        return cuenta;
    }
    
    public AccountsEntity find(Long id) {
    	EntityManager em = emf.createEntityManager();    
        return em.find(AccountsEntity.class, id);
    }
   
    public List<AccountsEntity> findAll() {
    	EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT c FROM Cuenta c", AccountsEntity.class).getResultList();
    }
    

    public AccountsEntity update(AccountsEntity cuenta) {
    	EntityManager em = emf.createEntityManager();
        return em.merge(cuenta);
    }

    public void delete(Long id) {    	
    	EntityManager em = emf.createEntityManager();AccountsEntity c = em.find(AccountsEntity.class, id);
        if (c != null) {
            em.remove(c);
        }
    }

}
 