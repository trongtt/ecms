/***************************************************************************
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 *
 **************************************************************************/
package org.exoplatform.ecm.model.impl;

import org.exoplatform.ecm.model.api.Folder;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:46 PM  
 */
public abstract class AbstractFolder extends AbtractBaseObject implements Folder {

    public AbstractFolder(String name, String path) {
        super(name, path);
    }

    public AbstractFolder(String name, String path, boolean isSystem) {
        super(name, path, isSystem);
    }  
    /**
     * Get type of folder
     * @return Type of folder such as nt:folder or nt:unstructured
     */
    public String getFolderType() {
        return null;
    }

}
