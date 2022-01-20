package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import models.User;
import servers.*;
@Stateless
public class UserService implements UserLocal,UserRemote{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean create(User user) {
		em.persist(user);
		return true;
	}

	@Override
	public boolean update(User user) {
		em.merge(user);
		return true;
	}

	@Override
	public boolean delete(User user) {
		em.remove(em.contains(user)?user:em.merge(user));
		return true;
	}

	@Override
	public User findById(int id) {
		return em.find(User.class, id);
	}

	@Override
	public List<User> findAll() {
		Query query = em.createQuery("from User");
		return query.getResultList();
	}

}
