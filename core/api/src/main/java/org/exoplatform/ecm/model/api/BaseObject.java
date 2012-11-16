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
package org.exoplatform.ecm.model.api;

import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 2, 2012
 * 2:13:57 PM  
 */
public interface BaseObject {

  
  /**
   * Get object name  
   * @return
   */
  String getName();

  /**
   * Get Object path  
   * @return
   */
  String getPath();

  /**
   * Get Object Property  
   * @param pName Property name
   * @return
   */
  Property getProperty(String pName) throws PathNotFoundException, RepositoryException ;

  /**
   * Get Object properties  
   * @return
   */
  List<Property> getProperties() throws PathNotFoundException, RepositoryException;
  
  /**
   * Get Object created date
   * @return
   */
  String getCreatedDate()  throws ValueFormatException, PathNotFoundException, RepositoryException;
  
  /**
   * Get Object last modified date
   * @return
   */
  String getLastModifiedDate() throws ValueFormatException, PathNotFoundException, RepositoryException;
  
  /**
   * Get Object Creator
   * @return
   */
  String getCreator() throws PathNotFoundException, RepositoryException;
  
  /**
   * Get Object Primary Type
   * @return
   */
  String getPrimaryType() throws PathNotFoundException, RepositoryException;
  
  /**
   * Get Object mixin types
   */
  List<String> getMixinTypes() throws PathNotFoundException, RepositoryException;
  
  /**
   * Get workspace name where stored current object.
   * @return Name of Workspace
   */
  String getWorkspace();
  
  /**
   * Get Object UUID
   * @return UUID of current Object
   */
  String getUUID() throws UnsupportedRepositoryOperationException, PathNotFoundException, RepositoryException;
  
  /**
   * 
   * @return
   */
  Session getSession() throws LoginException, NoSuchWorkspaceException, RepositoryException;
  
  /**
   * 
   * @return
   */
  Node getJCRNode() throws PathNotFoundException, RepositoryException;
  
  /**
   * 
   */
  void save() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, 
                     VersionException, LockException, NoSuchNodeTypeException, LoginException, NoSuchWorkspaceException, 
                     RepositoryException;
  
  /**
   * Add mixin
   * @param mixin
   */
  void addMixin(String mixin) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, 
                                     LockException, PathNotFoundException, RepositoryException;
  
  /**
   * 
   * @param mixin
   * @return
   */
  boolean canAddMixin(String mixin) throws NoSuchNodeTypeException, PathNotFoundException, RepositoryException;
}
