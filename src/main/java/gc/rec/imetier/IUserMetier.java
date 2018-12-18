package gc.rec.imetier;

import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.User;
 
 
public interface IUserMetier 
{ 
	public List<User> getUsers(); 
	public Page<User> getUsers(int page, int size); 
	public Page<User> getUsersByMotCle(String mc,int page, int size); 
	public User getUser( String username );   
	public User saveUser(User u);
	public boolean deleteUser(String username);
}
