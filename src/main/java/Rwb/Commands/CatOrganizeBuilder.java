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
package Rwb.Commands;

import java.util.List;

public class CatOrganizeBuilder {

    List<String> src; //= (List<String>) params.get("source");
    String dst; //= (String) params.get("dest");
    Boolean addtodest; //= (Boolean) params.get("addtodest");

    public CatOrganizeBuilder() {
    }

    public CatOrganizeBuilder setSourceParam(List<String> source) {
        this.src = source;
        
        return this;
    }

    public CatOrganizeBuilder setDestParam(String dest) {
        this.dst=dest;

        return this;
    }

    public CatOrganizeBuilder setAddToDestParam(Boolean addtodest) {
        this.addtodest=addtodest;
        
        return this;
    }

    public CatOrganize createCatOrganize() {
        return new CatOrganize(src,dst, addtodest);
    }

}
