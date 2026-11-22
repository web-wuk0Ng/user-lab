package com.example.userlab;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
class UserServiceTest {
@Test void createUsesGeneratedId() {
UserMapper mapper = mock(UserMapper.class);
doAnswer(inv -> { User u=inv.getArgument(0); u.setId(7L); return 1; }).when(mapper).insert(any());
User u = new User(); u.setId(99L); u.setName("Alice"); u.setEmail("alice@example.com");
assertEquals(7L, new UserService(mapper).create(u).getId());
}
@Test void missingUserReturns404() {
UserMapper mapper = mock(UserMapper.class);
ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> new UserService(mapper).get(9L));
assertEquals(404, e.getStatusCode().value());
}
}
