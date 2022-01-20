package servers;

import java.util.List;
import javax.ejb.Local;
import models.Phone;
import models.User;

@Local
public interface PhoneLocal {
	boolean create(Phone phone,int idUser);
	boolean update(Phone phone,int idUser); 
	boolean delete(Phone phone);
	Phone findById(int id); 
	List<Phone> findAll(User user);  

}
