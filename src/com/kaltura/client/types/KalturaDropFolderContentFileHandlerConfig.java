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
package com.kaltura.client.types;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaDropFolderContentFileHandlerMatchPolicy;
import com.kaltura.client.utils.ParseUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Tue, 23 Jun 15 08:20:46 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

@SuppressWarnings("serial")
public class KalturaDropFolderContentFileHandlerConfig extends KalturaDropFolderFileHandlerConfig {
    public KalturaDropFolderContentFileHandlerMatchPolicy contentMatchPolicy;
	/**  Regular expression that defines valid file names to be handled.   The following
	  might be extracted from the file name and used if defined:   -
	  (?P<referenceId>\w+) - will be used as the drop folder file's parsed slug.   -
	  (?P<flavorName>\w+)  - will be used as the drop folder file's parsed flavor.     */
    public String slugRegex;

    public KalturaDropFolderContentFileHandlerConfig() {
    }

    public KalturaDropFolderContentFileHandlerConfig(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("contentMatchPolicy")) {
                this.contentMatchPolicy = KalturaDropFolderContentFileHandlerMatchPolicy.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("slugRegex")) {
                this.slugRegex = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDropFolderContentFileHandlerConfig");
        kparams.add("contentMatchPolicy", this.contentMatchPolicy);
        kparams.add("slugRegex", this.slugRegex);
        return kparams;
    }

}

