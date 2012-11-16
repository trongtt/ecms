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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.exoplatform.ecm.model.api.File;
import org.exoplatform.ecm.model.api.ObjectType;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 12, 2012
 * 1:58:54 PM  
 */
public class WCMFile extends AbstractFile<WCMFile> implements File {

  public WCMFile(String workspace, String path) {
    super(workspace, path);
  }
  
  public WCMFile(String workspace, String path, boolean isSystem) {
    super(workspace, path, isSystem);
  }

  @Override
  public List<WCMFile> getChildren() throws PathNotFoundException, RepositoryException {
    List<WCMFile> listFile = new ArrayList<WCMFile>();
    NodeIterator nodeIter = getJCRNode().getNodes();
    while(nodeIter.hasNext()) {
      Node node = nodeIter.nextNode();
      listFile.add(new WCMFile(workspace, node.getPath(), isSystem));
    }
    return listFile;
  }

  @Override
  public WCMFile getParent() throws ItemNotFoundException, AccessDeniedException,
      PathNotFoundException, RepositoryException {
    return new WCMFile(workspace, getJCRNode().getParent().getPath(), isSystem);
  }

  @Override
  public String getObjectType() {
    return ObjectType.WCM_FILE;
  }  
}
