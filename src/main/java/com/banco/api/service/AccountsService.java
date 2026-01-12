package com.banco.api.service;

import java.util.List;

import com.banco.api.entity.AccountsEntity;
import com.banco.api.entity.ClientsEntity;
import com.banco.api.util.Log;

import lombok.extern.slf4j.Slf4j;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

//@Slf4j
@Stateless
public class AccountsService {
	
	@PersistenceContext(unitName = "BancoPU")
	private EntityManager em;

    public AccountsEntity crearCuenta(AccountsEntity cuenta) {
        try {
//            em.getTransaction().begin();
            em.persist(cuenta);
//            em.getTransaction().commit();            
            Log.info(LoadFilesService.class,"Cuenta creada: {}", cuenta.getNumeroCuenta());
            return cuenta;
        } catch (Exception e) {
            em.getTransaction().rollback();            
            Log.info(LoadFilesService.class,"Error creando cuenta", e);
            throw e;
        }
    }

    public boolean existeNumeroCuenta(String numero) {
        try {
            return em.createQuery(
                    "SELECT COUNT(c) FROM AccountsEntity c WHERE c.numeroCuenta = :numero", Long.class)
                .setParameter("numero", numero)
                .getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public String generarNumeroCuenta() {
        try {
            Long maxId = em.createQuery(
                    "SELECT COALESCE(MAX(c.idCuenta), 0) FROM AccountsEntity c", Long.class)
                .getSingleResult();
            String numeroCuenta = "ACC-" + String.format("%010d", maxId + 1);            
            Log.info(LoadFilesService.class,"Número de cuenta generado: {}", numeroCuenta);
            
            return numeroCuenta;
        } catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
    }
	
    
    public AccountsEntity create(AccountsEntity cuenta) {   
        em.persist(cuenta);
        return cuenta;
    }
    
    public AccountsEntity find(Long id) {  
        return em.find(AccountsEntity.class, id);
    }
   
    public List<AccountsEntity> findAll() {
        return em.createQuery("SELECT c FROM AccountsEntity c", AccountsEntity.class).getResultList();
    }
    

    public AccountsEntity update(AccountsEntity cuenta) {
        return em.merge(cuenta);
    }

    public void delete(Long id) {
    	AccountsEntity c = em.find(AccountsEntity.class, id);
        if (c != null) {
            em.remove(c);
        }
    }

}
 