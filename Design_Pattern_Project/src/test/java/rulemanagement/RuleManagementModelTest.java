/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package rulemanagement;

/**
 *
 * @author adsd3
 */
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RuleManagementModelTest {

    @TempDir
    Path tempDir;

    private Path rulesFile;
    private RuleManagementModel model;

    @BeforeEach
    void setUp() throws IOException {
        // 테스트용 파일 경로 준비 (아직 파일은 존재하지 않음)
        rulesFile = tempDir.resolve("rules.txt");
        // 모델 생성 시 loadRules()에서 파일이 없으면 자동 생성됨
        model = new RuleManagementModel(rulesFile.toString());
    }

    @Test
    void testInitialLoad_emptyFile() {
        // 빈 파일에서 로드했으므로 리스트가 비어 있어야 함
        List<String> initial = model.getRules();
        assertNotNull(initial, "getRules()는 null이 아닌 리스트를 반환해야 한다");
        assertTrue(initial.isEmpty(), "빈 파일에서 로드된 규칙이 없어야 한다");
    }

    @Test
    void testAddRule_persistsToFile() throws IOException {
        // 규칙 추가
        model.addRule("alpha");
        model.addRule("beta");

        // 모델 내부 리스트 확인
        List<String> inMemory = model.getRules();
        assertEquals(2, inMemory.size(), "2개의 규칙이 추가되어야 한다");
        assertTrue(inMemory.containsAll(List.of("alpha", "beta")));

        // 실제 파일 내용 확인
        List<String> fileLines = Files.readAllLines(rulesFile);
        assertEquals(inMemory, fileLines,
            "파일에 저장된 내용과 모델 리스트가 일치해야 한다");
    }

    @Test
    void testDeleteRules_persistsToFile() throws IOException {
        // 미리 몇 개 규칙을 파일에 직접 작성
        Files.write(rulesFile, List.of("one", "two", "three"),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        // 모델을 재생성하여 loadRules() 실행
        model = new RuleManagementModel(rulesFile.toString());
        assertEquals(3, model.getRules().size(), "3개의 규칙이 로드되어야 한다");

        // 두 개 삭제
        model.deleteRules(List.of("two", "three"));

        // 모델에서 삭제 확인
        List<String> remaining = model.getRules();
        assertEquals(1, remaining.size());
        assertTrue(remaining.contains("one"));

        // 파일에도 반영되었는지 확인
        List<String> fileAfter = Files.readAllLines(rulesFile);
        assertEquals(remaining, fileAfter,
            "삭제 후 모델 리스트와 파일 내용이 일치해야 한다");
    }

    @Test
    void testLoadRules_ignoresEmptyAndWhitespaceLines() throws IOException {
        // 파일에 빈 줄, 공백 줄, 실제 규칙 혼합 작성
        List<String> mixed = List.of(
            "",
            "   ",
            "foo",
            "  bar  ",
            ""
        );
        Files.write(rulesFile, mixed,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        // 모델 재생성하여 loadRules() 실행
        model = new RuleManagementModel(rulesFile.toString());

        // 공백 줄은 무시되고 trim된 값만 남아야 함
        List<String> loaded = model.getRules();
        assertEquals(2, loaded.size(), "공백 줄을 제외하고 2개의 규칙만 남아야 한다");
        assertEquals("foo", loaded.get(0));
        assertEquals("bar", loaded.get(1));
    }
}