package servers;

import java.util.List;

import javax.ejb.Local;

import models.User;

@Local
public interface UserLocal {
	boolean create(User user);
	boolean update(User user); 
	boolean delete(User user);
	User findById(int id); 
	List<User> findAll(); 

}
