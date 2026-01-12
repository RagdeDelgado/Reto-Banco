package com.banco.api.service;

import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.util.List;

import com.banco.api.entity.ClientsEntity;

//@Slf4j

public class ClientsService {
	
	private EntityManagerFactory emf;

    public ClientsService() {
        emf = Persistence.createEntityManagerFactory("banco_pu");
        //log.info("ClienteService initialized");
    }

    public ClientsEntity crearCliente(ClientsEntity cliente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            //log.info("Cliente creado: {}", cliente.getCodigoCliente());
            return cliente;
        } catch (Exception e) {
            em.getTransaction().rollback();
            //log.error("Error creando cliente", e);
            throw e;
        } finally {
            em.close();
        }
    }

//    public ClientsEntity obtenerClientePorIdentificacion(String tipoId, String numeroId) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            return em.createQuery(
//                    "SELECT c FROM Cliente c WHERE c.tipoIdentificacion = :tipo " +
//                    "AND c.numeroIdentificacion = :numero", ClientsEntity.class)
//                .setParameter("tipo", Cliente.TipoIdentificacion.valueOf(tipoId))
//                .setParameter("numero", numeroId)
//                .getSingleResult();
//        } catch (NoResultException e) {
//            return null;
//        } finally {
//            em.close();
//        }
//    }

    public boolean existeCorreo(String correo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(c) FROM Cliente c WHERE c.correo = :correo", Long.class)
                .setParameter("correo", correo)
                .getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public boolean existeNumerIdentificacion(String numero) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(c) FROM Cliente c WHERE c.numeroIdentificacion = :numero", 
                    Long.class)
                .setParameter("numero", numero)
                .getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
    
    
    
    
    public ClientsEntity create(ClientsEntity cliente) {
    	EntityManager em = emf.createEntityManager();    
        em.persist(cliente);
        return cliente;
    }

    public ClientsEntity find(Long id) {
    	EntityManager em = emf.createEntityManager();    
        return em.find(ClientsEntity.class, id);
    }
   
    public List<ClientsEntity> findAll() {
    	EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT c FROM Cliente c", ClientsEntity.class).getResultList();
    }
    

    public ClientsEntity update(ClientsEntity cliente) {
    	EntityManager em = emf.createEntityManager();
        return em.merge(cliente);
    }

    public void delete(Long id) {    	
    	EntityManager em = emf.createEntityManager();ClientsEntity c = em.find(ClientsEntity.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}
