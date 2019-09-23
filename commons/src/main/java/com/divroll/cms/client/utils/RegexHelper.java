/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2018 to present, Divroll, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.divroll.cms.client.utils;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;

public class RegexHelper {

    public static ArrayList<String> getMatches(String input, String pattern) {
        ArrayList<String> matches = new ArrayList<String>();
        RegExp regExp = RegExp.compile(pattern, "g");
        for (MatchResult matcher = regExp.exec(input); matcher != null; matcher = regExp.exec(input)) {
            for(int i=0;i<matcher.getGroupCount();i++) {
                if(i != 0) {
                    matches.add(matcher.getGroup(i));
                }
            }
        }
        return matches;
    }

    public static boolean isNumeric(String string){
        RegExp regExp = RegExp.compile("^[0-9]*$");
        MatchResult matcher = regExp.exec(string);
        boolean matchFound = matcher != null;
        return matchFound;
    }

    public static boolean isAlphaNumeric(String string) {
        RegExp regExp = RegExp.compile("^[a-zA-Z0-9]*$");
        MatchResult matcher = regExp.exec(string);
        boolean matchFound = matcher != null;
        return matchFound;
    }

    public static boolean isAlpha(String string) {
        RegExp regExp = RegExp.compile("^[a-zA-Z]*$");
        MatchResult matcher = regExp.exec(string);
        boolean matchFound = matcher != null;
        return matchFound;
    }

}
