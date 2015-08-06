/*
 */
package Rwb.Parser;

import Rwb.Commands.WikiCommand;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author niki
 */
public class CatBotTest {

    public CatBotTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static String mkRes(String input) {
        return String.format("[%s, [Commit pagecache(compressed: false)]]", input);
    }

    private static void mkParserTest(String title, String source, String representation) throws TokenMgrException, ParseException {
        System.out.println(title);
        CatBot b = new CatBot(source);
        List<WikiCommand> wcl = b.Input();
        System.out.println(wcl.toString());

        assertEquals(mkRes(representation), wcl.toString());
    }

    @Test
    public void testSummary() throws ParseException {
        mkParserTest("Test summary", "summary(\"Sum outer\");", "[EditSummary: Sum outer]");
    }

    @Test
    public void testCommit() throws ParseException {
        mkParserTest("Test commit", "commit();", "[Commit pagecache(compressed: false)]");
    }

    @Test
    public void testCommitCompressed() throws ParseException {
        mkParserTest("Test commitcompressed", "commitcompressed();", "[Commit pagecache(compressed: true)]");
    }

    @Test
    public void testSearchNS() throws ParseException {
        mkParserTest("Test searchns", "searchns(\"main\",0){printtitle();}", "[Printtitle: Pages: [[SearchNS: main(0)]]]");
    }

    @Test
    public void testSearchNS2() throws ParseException {
        mkParserTest("Test searchNS", "searchns(\"main+111+115\",0, 111, 115){printtitle();}", "[Printtitle: Pages: [[SearchNS: main+111+115(0, 111, 115)]]]");
    }
     
    @Test
    public void testSearchNS3() throws ParseException {
        mkParserTest("Test searchNS3", "searchns(\"main+-2\",0, -2){printtitle();}", "[Printtitle: Pages: [[SearchNS: main+-2(0, -2)]]]");
    }

    @Test
    public void testOrganize() throws ParseException {
        mkParserTest("Test organize", "organize() {[[Vegetarische Rezepte]], [[Vorspeisen]] --> [[Vegetarische Vorspeise]];[[Vegetarische Rezepte]], [[Hauptspeisen]] <-- [[Vegetarische Hauptspeise]];}", "[CatOrganize: From: [Vegetarische Rezepte, Vorspeisen] To: [Vegetarische Vorspeise]], [CatOrganize: From: [Vegetarische Rezepte, Hauptspeisen] To: [Vegetarische Hauptspeise] Remove]");
    }

    @Test
    public void testRecent() throws ParseException {
        mkParserTest("Test recent", "recent(100){putintocat(\"Recent\");}", "[CatPutRemove: Put into: [Recent]. Pages: [[RecentChanges: 100]]]");
    }

    @Test
    public void testSearch() throws ParseException {
        mkParserTest("test search", "search(\"Canneloni\"){printtitle();}", "[Printtitle: Pages: [[Search: Canneloni]]]");
    }

    @Test
    public void testNestedGenerators() throws ParseException {
        mkParserTest(
                "Test nested generators", "recent(100){pagelist([[P1]]) {putintocat(\"Recent100P1\");}}", "[CatPutRemove: Put into: [Recent100P1]. Pages: [[[P1][RecentChanges: 100]]]]"
        );
    }

    @Test
    public void testPutIntoCat() throws ParseException {
        mkParserTest(
                "Test putintocat", "pagelist([[P1]], [[P2]]) {putintocat(\"Cat1\");}", "[CatPutRemove: Put into: [Cat1]. Pages: [[[P1, P2]]]]"
        );
    }

    @Test
    public void testReplace() throws ParseException {
        mkParserTest(
                "Test replace", "pagelist([[P1]], [[P2]]) {replace(\"Test(\\\\d{2})\", \"Answer$1\");}", "[Replace: 'Test(\\\\d{2})' with 'Answer$1']"
        );
    }

    @Test
    public void testAddExclude() throws ParseException {
        mkParserTest(
                "Test addexclude", "addexclude(\"<gallery>\",\"</gallery>\");", "[Addexclude: <gallery>, </gallery>]"
        );
    }

    @Test
    public void testResetExcludes() throws ParseException {
        mkParserTest(
                "Test resetexcludes", "resetexcludes();", "[ReInitIgnorePositions]"
        );
    }
}
