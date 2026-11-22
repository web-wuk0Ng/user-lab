package com.example.userlab;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class UserService {
private final UserMapper mapper;
public UserService(UserMapper mapper) { this.mapper = mapper; }
public List<User> list() { return mapper.findAll(); }
public User get(Long id) {
User user = mapper.findById(id);
if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
return user;
}
public User create(User user) { user.setId(null); mapper.insert(user); return user; }
public User update(Long id, User user) {
get(id); user.setId(id); mapper.update(user); return get(id);
}
public void delete(Long id) {
if (mapper.delete(id) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
}
}
