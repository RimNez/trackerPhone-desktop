package trackerClient;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import models.*;
import servers.*;

public class Client {

	private static PhoneRemote lookUpSmartphone() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
			config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
			config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
			final Context context = new InitialContext(config);
			return (PhoneRemote) context.lookup("ejb:/trackerServer/PhoneService!servers.PhoneRemote");
			
	}
		
	private static UserRemote lookUpUser() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (UserRemote) context.lookup("ejb:/trackerServer/UserService!servers.UserRemote");
				
	}

	public static void main(String[] args) throws NamingException {
		UserRemote remote2 = lookUpUser();
		PhoneRemote remote = lookUpSmartphone(); 
		User u1 = new User("Nezhari","Rim","rim@rim.com",new Date(), "795422");
		Phone p1 = new Phone("samsung","00048955");
		remote2.create(u1);	
		remote.create(p1, 1);
			
	}

}
