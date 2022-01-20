package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import models.Phone;
import models.User;
import servers.*;
@Stateless
public class PhoneService implements PhoneLocal,PhoneRemote{

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean create(Phone phone, int idUser) {
		User user = em.find(User.class, idUser);
		if (user == null) return false;
		else {
			phone.setUser(user);
			em.persist(phone);
			return true;
		}
	}

	@Override
	public boolean update(Phone phone, int idUser) {
		User user = em.find(User.class, idUser);
		if (user == null) return false;
		else {
			phone.setUser(user);
			em.merge(phone);
			return true;
		}
	}

	@Override
	public boolean delete(Phone phone) {
		em.remove(em.contains(phone)?phone:em.merge(phone));
		return true;
	}

	@Override
	public Phone findById(int id) {
		return em.find(Phone.class, id);
	}

	@Override
	public List<Phone> findAll(User user) {
		//Query query = em.createNativeQuery("Select * from Phone p where idUser=?",Phone.class);
		Query query = em.createQuery("from Phone p where p.user=:user");
		query.setParameter("user", user);
		return query.getResultList();
	}
}
