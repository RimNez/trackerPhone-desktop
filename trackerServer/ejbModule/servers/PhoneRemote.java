package servers;

import java.util.List;
import javax.ejb.Remote;
import models.Phone;
import models.User;

@Remote
public interface PhoneRemote {
	boolean create(Phone phone,int idUser);
	boolean update(Phone phone,int idUser); 
	boolean delete(Phone phone);
	Phone findById(int id); 
	List<Phone> findAll(User user); 

}
