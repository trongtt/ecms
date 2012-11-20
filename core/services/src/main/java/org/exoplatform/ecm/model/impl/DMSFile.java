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

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.exoplatform.ecm.model.api.File;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 12, 2012
 * 1:58:44 PM  
 */
public class DMSFile extends AbstractFile<DMSFile> implements File {

    public static final String DMS_FILE = "DMSFile";
    
    public DMSFile(String workspace, String path) {
        super(workspace, path);
    }

    public DMSFile(String workspace, String path, boolean isSystem) {
        super(workspace, path, isSystem);
    }

    @Override
    public DMSFile getParent() throws ItemNotFoundException, AccessDeniedException,
    PathNotFoundException, RepositoryException {
        return new DMSFile(workspace, getJCRNode().getParent().getPath(), isSystem);
    }

    @Override
    public String getObjectType() {
        return DMS_FILE;
    }


}
