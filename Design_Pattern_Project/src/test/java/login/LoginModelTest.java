/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package login;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginModelTest {

    private LoginModel loginModel;
    private Path classpathRoot;

    @BeforeAll
    void setupResources() throws Exception {
        loginModel = new LoginModel();

        // 1) 클래스패스 루트 디렉토리 경로 가져오기
        URL resource = getClass().getClassLoader().getResource("");
        if (resource == null) {
            throw new IllegalStateException("클래스패스 루트를 찾을 수 없습니다");
        }
        classpathRoot = Paths.get(resource.toURI());

        // 2) ADMIN_LOGIN.txt 생성
        String adminData = "admin1,secret,Admin Name,Dept\n";
        Files.writeString(
            classpathRoot.resolve("ADMIN_LOGIN.txt"),
            adminData,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );

        // 3) USER_LOGIN.txt 생성
        String userData = "user1,passwd,User Name,Dept\n";
        Files.writeString(
            classpathRoot.resolve("USER_LOGIN.txt"),
            userData,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    @Test
    void testValidAdminCredentials() {
        assertTrue(
            loginModel.validateCredentials("admin1", "secret", "admin"),
            "올바른 admin 자격증명은 true"
        );
    }

    @Test
    void testInvalidAdminPassword() {
        assertFalse(
            loginModel.validateCredentials("admin1", "wrong", "admin"),
            "잘못된 admin 비밀번호는 false"
        );
    }

    @Test
    void testValidUserCredentials() {
        assertTrue(
            loginModel.validateCredentials("user1", "passwd", "user"),
            "올바른 user 자격증명은 true"
        );
    }

    @Test
    void testInvalidUserId() {
        assertFalse(
            loginModel.validateCredentials("wrongUser", "passwd", "user"),
            "존재하지 않는 userId는 false"
        );
    }

    @Test
    void testRoleFallbackToUserFile() {
        // role 이 "guest" 등일 때도 USER_LOGIN.txt 를 읽기 때문에 올바른 user1/passwd 도 true
        assertTrue(
            loginModel.validateCredentials("user1", "passwd", "guest"),
            "알 수 없는 role 도 USER_LOGIN.txt 로 처리"
        );
    }

    @Test
    void testFileNotFoundReturnsFalse() {
        // 존재하지 않는 파일명(예: role="none") 은 false
        assertFalse(
            loginModel.validateCredentials("any", "any", "none"),
            "파일 못 찾으면 false"
        );
    }
}