// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Kaltura Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2015  Kaltura Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBulkUploadResultObjectType implements KalturaEnumAsString {
    ENTRY ("1"),
    CATEGORY ("2"),
    USER ("3"),
    CATEGORY_USER ("4"),
    CATEGORY_ENTRY ("5");

    public String hashCode;

    KalturaBulkUploadResultObjectType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaBulkUploadResultObjectType get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return CATEGORY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return USER;
        }
        else 
        if (hashCode.equals("4"))
        {
           return CATEGORY_USER;
        }
        else 
        if (hashCode.equals("5"))
        {
           return CATEGORY_ENTRY;
        }
        else 
        {
           return ENTRY;
        }
    }
}
