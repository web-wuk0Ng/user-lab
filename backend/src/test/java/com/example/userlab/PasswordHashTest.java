package com.example.userlab;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
class PasswordHashTest {
@Test void makeHashForLab() {
String p = System.getenv("LAB_PASSWORD");
Assumptions.assumeTrue(p != null && !p.isBlank());
System.out.println("LAB_HASH=" + new BCryptPasswordEncoder().encode(p));
}
}
