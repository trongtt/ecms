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

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.exoplatform.ecm.model.api.FileData;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:30 PM  
 */
public class FileDataImpl extends ObjectDataImpl<FileData> implements FileData {

  public FileDataImpl(String workspace, String path) {
    super(workspace, path);
  }
  
  public FileDataImpl(String workspace, String path, boolean isSystem) {
    super(workspace, path, isSystem);
  }  
  /**
   * 
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws ValueFormatException 
   */
  public InputStream getData() throws ValueFormatException, PathNotFoundException, RepositoryException {
    return getResource().getProperty("jcr:data").getStream();
  }
  
  /**
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws ValueFormatException 
   */
  public String getMimeType() throws ValueFormatException, PathNotFoundException, RepositoryException {
    return getResource().getProperty("jcr:mimeType").getString();
  }
  
  /**
   * 
   * @return
   * @throws PathNotFoundException
   * @throws RepositoryException
   */
  private Node getResource() throws PathNotFoundException, RepositoryException {
    return getJCRNode().getNode("jcr:content");
  }
 
}
