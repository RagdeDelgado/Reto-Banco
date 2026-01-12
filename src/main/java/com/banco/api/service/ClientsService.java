package com.banco.api.service;

import lombok.extern.slf4j.Slf4j;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import com.banco.api.entity.ClientsEntity;

//@Slf4j

@Stateless
public class ClientsService {
	
	
	@PersistenceContext(unitName = "BancoPU")
	private EntityManager em;

    public ClientsEntity crearCliente(ClientsEntity cliente) {
        try {
//            em.getTransaction().begin();
            em.persist(cliente);
//            em.getTransaction().commit();
            //log.info("Cliente creado: {}", cliente.getCodigoCliente());
            return cliente;
        } catch (Exception e) {
            em.getTransaction().rollback();
            //log.error("Error creando cliente", e);
            throw e;
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
//        try {
        	
        	Long count = em.createQuery(
                    "SELECT COUNT(c) FROM ClientsEntity c WHERE c.correo = :correo",
                    Long.class)
                .setParameter("correo", correo)
                .getSingleResult();

            return count  > 0 ? true : false ;
        	
        	
//            return this.em.createQuery(
//                    "SELECT COUNT(c) FROM ClientsEntity c WHERE c.correo = :correo", Long.class)
//                .setParameter("correo", correo)
//                .getSingleResult() > 0;
//        } finally {
//            em.close();
//        }
    }

//    public boolean existeNumerIdentificacion(String numero) {
//        try {
//            return this.em.createQuery(
//                    "SELECT COUNT(c) FROM ClientsEntity c WHERE c.numeroIdentificacion = :numero", 
//                    Long.class)
//                .setParameter("numero", numero)
//                .getSingleResult() > 0;
//        } finally {
//            em.close();
//        }
//    }
    
    public boolean existeNumeroIdentificacion(String numero) {

        Long count = em.createQuery(
                "SELECT COUNT(c) FROM ClientsEntity c WHERE c.numeroIdentificacion = :numero",
                Long.class)
            .setParameter("numero", numero)
            .getSingleResult();

        return count != null && count > 0;
    }
    
    
    
    public ClientsEntity create(ClientsEntity cliente) {
    	this.em.persist(cliente);
        return cliente;
    }

    public ClientsEntity find(Long id) {
    	return this.em.find(ClientsEntity.class, id);
    }
   
    public List<ClientsEntity> findAll() {
        return this.em.createQuery("SELECT c FROM ClientsEntity c ", ClientsEntity.class).getResultList();
    }
    

    public ClientsEntity update(ClientsEntity cliente) {
    	return this.em.merge(cliente);
    }

    public void delete(Long id) {    	
    	ClientsEntity c = this.em.find(ClientsEntity.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}
