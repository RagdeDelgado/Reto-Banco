package com.banco.api.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import com.banco.api.entity.ClientsEntity;

@Stateless
public class ClienteService {
	
	@PersistenceContext(unitName = "BancoPU")
    private EntityManager em;

    public ClientsEntity create(ClientsEntity cliente) {
        em.persist(cliente);
        return cliente;
    }

    public ClientsEntity find(Long id) {
        return em.find(ClientsEntity.class, id);
    }

    public List<ClientsEntity> findAll() {
        return em.createQuery("SELECT c FROM Cliente c", ClientsEntity.class).getResultList();
    }

    public ClientsEntity update(ClientsEntity cliente) {
        return em.merge(cliente);
    }

    public void delete(Long id) {
    	ClientsEntity c = em.find(ClientsEntity.class, id);
        if (c != null) {
            em.remove(c);
        }
    }

}
