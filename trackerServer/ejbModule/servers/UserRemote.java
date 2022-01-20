package servers;

import java.util.List;

import javax.ejb.Remote;

import models.User;

@Remote
public interface UserRemote {
	boolean create(User user);
	boolean update(User user); 
	boolean delete(User user);
	User findById(int id); 
	List<User> findAll(); 

}
