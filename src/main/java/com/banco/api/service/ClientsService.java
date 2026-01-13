package com.banco.api.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import com.banco.api.entity.ClientsEntity;
import com.banco.api.util.Log;

@Stateless
public class ClientsService {
	
	
	@PersistenceContext(unitName = "BancoPU")
	private EntityManager em;

    public ClientsEntity crearCliente(ClientsEntity cliente) {
        try {
            em.persist(cliente);
            Log.info(LoadFilesService.class,"Cliente creado: {}", cliente.getCodigoCliente());
            return cliente;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Log.error(LoadFilesService.class,"Error creando cliente", e);
            throw e;
        } 
    }

    public boolean existeCorreo(String correo) {       	
        	Long count = em.createQuery(
                    "SELECT COUNT(c) FROM ClientsEntity c WHERE c.correo = :correo",
                    Long.class)
                .setParameter("correo", correo)
                .getSingleResult();

            return count  > 0 ? true : false ;        	        	
    }

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
