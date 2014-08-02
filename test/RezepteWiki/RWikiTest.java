/*
 * Copyright (C) 2014 Niki Hansche
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package RezepteWiki;

import Rwb.Categories.Categories;
import Rwb.Wiki;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niki Hansche
 */
public class RWikiTest {
    
    public RWikiTest() {
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

    /**
     * Test of getAllCategories method, of class Wiki.
     */
    @Test
    public void testGetAllCategories() throws Exception {
        System.out.println("getAllCategories");
        String page = "[[Kategorie:A-Kat]]\n<nowiki>[[Kategorie:NO-Kat]] [[Kategorie:NO2-Kat|Dings]]</nowiki>[[Kategorie:B-Kat]] [[Kategorie:C-Kat|CCAT]]";
        Map<String, String> expResult = new HashMap<>();
        expResult.put("[[Kategorie:A-Kat]]\n", "A-Kat");
        expResult.put("[[Kategorie:NO-Kat]]", "NO-Kat");
        expResult.put("[[Kategorie:NO2-Kat|Dings]]", "NO2-Kat");
        expResult.put("[[Kategorie:B-Kat]]", "B-Kat");
        expResult.put("[[Kategorie:C-Kat|CCAT]]", "C-Kat");
        Map<String, String> result = Categories.getAllCategories(page);
        assertEquals(expResult.get("[[Kategorie:A-Kat]]\n"), result.get("[[Kategorie:A-Kat]]\n"));
        assertEquals(expResult.get("[[Kategorie:NO-Kat]]"), result.get("[[Kategorie:NO-Kat]]"));
        assertEquals(expResult.get("[[Kategorie:NO2-Kat|Dings]]"), result.get("[[Kategorie:NO2-Kat|Dings]]"));
        assertEquals(expResult.get("[[Kategorie:B-Kat]]"), result.get("[[Kategorie:B-Kat]]"));
        assertEquals(expResult.get("[[Kategorie:C-Kat|CCAT]]"), result.get("[[Kategorie:C-Kat|CCAT]]"));
    }

    /**
     * Test of removeAllCategoryLinks method, of class Wiki.
     */
    @Test
    public void testRemoveAllCategoryLinks() throws Exception {
        System.out.println("removeAllCategoryLinks");
        String page = "[[Kategorie:A-Kat]]\n<!--e [[Kategorie:NOCC-Kat]] a--><nowiki>[[Kategorie:NO-Kat]] [[Kategorie:NO2-Kat|Dings]]</nowiki>[[Kategorie:B-Kat]] [[Kategorie:C-Kat|CCAT]]";
        String expResult = "<!--e [[Kategorie:NOCC-Kat]] a--><nowiki>[[Kategorie:NO-Kat]] [[Kategorie:NO2-Kat|Dings]]</nowiki> ";
        String result = Categories.removeAllCategoryLinks(page, new Wiki("http","","")).getText();
        assertEquals(expResult, result);
    }

    @Test
    public void testPrintDiff() throws Exception {
        System.out.println("printDiff");
        
        Wiki.printDiff("Title", page, nPage, System.out);
    }

    @Test
    public void testacceptDiff1() throws Exception {
        System.out.println("acceptDiff1");

        Wiki.acceptDiff("Title", page, nPage);
    }

    
    /**
     * Test of saveThis method, of class Wiki.
     */
    @Test
    public void testSaveThis() {
/*        
        System.out.println("saveThis");
        String FileName = "";
        Wiki instance = new Wiki();
        instance.saveThis(FileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
*/        
    }
        String page = "{{Rezept|\n" +
" | Menge         = 2–3 Personen\n" +
" | Zeit          = 20 Minuten\n" +
" | Schwierigkeit = leicht\n" +
" | Alkohol       = nein\n" +
" | Vegetarisch   = ja\n" +
" | Bild          = Kein_Bild.png\n" +
"|}}\n" +
"\n" +
"'''Kräuterseitling und Pinienkern''' ist ein einfaches chinesisches Tempelgericht.\n" +
" \n" +
"== Zutaten ==\n" +
"* 250 g [[Zutat:Kräuterseitling|Kräuterseitlinge]]\n" +
"* 3 EL [[Zutat:Pinienkern|Pinienkerne]]\n" +
"* 6 EL klein gewürfelte rote und grüne [[Zutat:Paprika|Paprika]]\n" +
"* 2 TL [[Zutat:Maisstärke|Maisstärke]] in 3 TL [[Zutat:Wasser|Wasser]] angerührt\n" +
"* [[Zutat:Salz|Salz]]\n" +
"\n" +
"== Kochgeschirr ==\n" +
"* 1 [[Zubereitung:Messer|Messer]]\n" +
"* 1 [[Zubereitung:Wok|Wok]]\n" +
"\n" +
"== Zubereitung ==\n" +
"* Die Saitlinge kurz [[Zubereitung:kochen|kochen]], bis sie anfangen gar zu werden.\n" +
"* In feine Streifen, dann in Rauten [[Zubereitung:schneiden|schneiden]].\n" +
"* Öl im Wok erhitzen, die Pinienkerne ganz kurz [[Zubereitung:anbraten|anbraten]].\n" +
"* Sofort die Paprika und die Pilze dazugeben.\n" +
"* Mit Salz [[Zubereitung:abschmecken|abschmecken]] und mit der angerührten Stärke leicht [[Zubereitung:binden|binden]].\n" +
"\n" +
"== Beilagen ==\n" +
"* [[:Kategorie:Salat|Salat]]\n" +
"\n" +
"== Varianten ==\n" +
"* …\n" +
"\n" +
"[[Kategorie:Vegetarische Hauptspeise]]\n" +
"[[Kategorie:Vegane Hauptspeisen]]\n" +
"[[Kategorie:Chinesische Küche]]\n" +
"[[Kategorie:Schnelle Gerichte]]";
        String nPage = "{{Rezept|\n" +
" | Menge         = 2–3 Personen\n" +
" | Zeit          = 20 Minuten\n" +
" | Schwierigkeit = leicht\n" +
" | Alkohol       = nein\n" +
" | Vegetarisch   = ja\n" +
" | Bild          = Kein_Bild.png\n" +
"|}}\n" +
"\n" +
"'''Kräuterseitling und Pinienkern''' ist ein einfaches chinesisches Tempelgericht.\n" +
" \n" +
"== Zutaten ==\n" +
"* 250 g [[Zutat:Kräuterseitling|Kräuterseitlinge]]\n" +
"* 3 EL [[Zutat:Pinienkern|Pinienkerne]]\n" +
"* 6 EL klein gewürfelte rote und grüne [[Zutat:Paprika|Paprika]]\n" +
"* 2 TL [[Zutat:Maisstärke|Maisstärke]] in 3 TL [[Zutat:Wasser|Wasser]] angerührt\n" +
"* [[Zutat:Salz|Salz]]\n" +
"\n" +
"== Kochgeschirr ==\n" +
"* 1 [[Zubereitung:Messer|Messer]]\n" +
"* 1 [[Zubereitung:Wok|Wok]]\n" +
"\n" +
"== Zubereitung ==\n" +
"* Die Saitlinge kurz [[Zubereitung:kochen|kochen]], bis sie anfangen gar zu werden.\n" +
"* In feine Streifen, dann in Rauten [[Zubereitung:schneiden|schneiden]].\n" +
"* Öl im Wok erhitzen, die Pinienkerne ganz kurz [[Zubereitung:anbraten|anbraten]].\n" +
"* Sofort die Paprika und die Pilze dazugeben.\n" +
"* Mit Salz [[Zubereitung:abschmecken|abschmecken]] und mit der angerührten Stärke leicht [[Zubereitung:binden|binden]].\n" +
"\n" +
"== Beilagen ==\n" +
"* [[:Kategorie:Salat|Salat]]\n" +
"\n" +
"== Varianten ==\n" +
"* …\n" +
"\n" +
"[[Kategorie:Vegetarische Hauptspeise]]\n" +
"[[Kategorie:Vegane Beilagen]]\n" +
"[[Kategorie:Chinesische Küche]]\n" +
"[[Kategorie:Schnelle Gerichte]]";
    
}
