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

package Rwb.Categories;

import Rwb.Page;
import Rwb.Wiki;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Niki Hansche
 */
public class CategoriesTest {
    
    public CategoriesTest() {
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
     * Test of removeAllCategoryLinks method, of class Categories.
     */
    @Test
    public void testRemoveAllCategoryLinks() {
        System.out.println("removeAllCategoryLinks");
        String page = "Bla\n[[:Kategorie:Jo]]\n[[Kategorie:Raus1]]\n[[Kategorie:Raus2]][[Kategorie:Raus1]]\n[[Kategorie:Raus3]]\n\n";
        Page expResult = new Page("Bla\n[[:Kategorie:Jo]]\n\n\n");
        Page result = Categories.removeAllCategoryLinks(page, new Rwb.Wiki("",""));
        assertEquals(expResult.getText().trim(), result.getText().trim());
    }

    /**
     * Test of getAllCategories method, of class Categories.
     */
    @Test
    public void testGetAllCategories() {
        System.out.println("getAllCategories");
        String page = "Bla\n[[:Kategorie:Jo]]\n[[Kategorie:Raus1]]\n[[Kategorie:Raus2]][[Kategorie:Raus1]]\n[[Kategorie:Raus3]]\n\n";
        Map<String, String> expResult = null;
        Map<String, String> result = Categories.getAllCategories(page);
        System.out.println(result);
        assertEquals(
                "{[[Kategorie:Raus3]]\n" +
                "=Raus3, [[Kategorie:Raus1]]\n" +
                "=Raus1, [[Kategorie:Raus2]]=Raus2}"
                , result.toString());
    }
    
}
