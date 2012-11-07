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
import java.util.Arrays;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.exoplatform.ecm.model.api.ObjectData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:08 PM  
 */
public class ObjectDataImpl<T> implements ObjectData {

  protected static final Log LOG = ExoLogger.getLogger(ObjectData.class); 
  protected String workspace;
  protected String path;
  protected String UUID;
  protected boolean isSystem = false;
  
  public ObjectDataImpl(String workspace, String path) {
    this.workspace = workspace;
    this.path = path;
  }
  
  public ObjectDataImpl(String workspace, String path, boolean isSystem) {
    this.workspace = workspace;
    this.path = path;
    this.isSystem = isSystem;
  }  
  
  public ObjectData getInstance() {
    return this;
  }
  
  public Node getJCRNode() throws PathNotFoundException, RepositoryException {
    return (Node)getSession().getItem(path);
  }
  
  /**
   * Get object name  
   * @return
   */
  public String getName() {
    return path.substring(path.lastIndexOf("/") + 1, path.length());
  }

  /**
   * Get Object path  
   * @return
   */
  public String getPath() {
    try {
      return getJCRNode().getPath();
    } catch (RepositoryException e) {
      LOG.error(e);
    }
    return null;
  }

  /**
   * Get Object children  
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public List<ObjectData> getChildren() throws PathNotFoundException, RepositoryException {
    List<ObjectData> listT = new ArrayList<ObjectData>();
    NodeIterator nodeIter = getJCRNode().getNodes();
    while(nodeIter.hasNext()) {
      Node node = nodeIter.nextNode();
      listT.add(new ObjectDataImpl<ObjectData>(workspace, node.getPath()));
    }
    return listT;
  }

  /**
   * Get Object Property  
   * @param pName Property name
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public Property getProperty(String pName) throws PathNotFoundException, RepositoryException {
    return getJCRNode().getProperty(pName);
  }

  /**
   * Get Object properties  
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public List<Property> getProperties() throws PathNotFoundException, RepositoryException {
    List<Property> listPro = new ArrayList<Property>();
    PropertyIterator proIter;
    proIter = getJCRNode().getProperties();
    while(proIter.hasNext()) {
      listPro.add(proIter.nextProperty());
    }
    return listPro;
  }
  
  /**
   * Get Object created date
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws ValueFormatException 
   */
  public String getCreatedDate() throws ValueFormatException, PathNotFoundException, RepositoryException {
    return getJCRNode().getProperty("exo:dateCreated").getString();
    
  }
  
  /**
   * Get Object last modified date
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws ValueFormatException 
   */
  public String getLastModifiedDate() throws ValueFormatException, PathNotFoundException, RepositoryException {
    return getJCRNode().getProperty("exo:lastModifiedDate").getString();
  }
  
  /**
   * Get Object Creator
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public String getCreator() throws PathNotFoundException, RepositoryException {
    return getJCRNode().getProperty("exo:owner").toString();
  }
  
  /**
   * Get Object Parent
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws AccessDeniedException 
   * @throws ItemNotFoundException 
   */
  public ObjectData getParent() throws ItemNotFoundException, AccessDeniedException, PathNotFoundException, RepositoryException {
    return new ObjectDataImpl<ObjectData>(workspace, getJCRNode().getParent().getPath());
  }
  
  /**
   * Get Object Primary Type
   * @return
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public String getPrimaryType() throws PathNotFoundException, RepositoryException {
    return getJCRNode().getPrimaryNodeType().getName();
  }
  
  /**
   * Get Object mixin types
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   */
  public List<String> getMixinTypes() throws PathNotFoundException, RepositoryException {
    return new ArrayList<String>(Arrays.asList(((NodeImpl)getJCRNode()).getMixinTypeNames()));
  }
  
  /**
   * Get workspace name where stored current object.
   * @return Name of Workspace
   */
  public String getWorkspace() {
    return workspace;
  }
  
  /**
   * Get Object UUID
   * @return UUID of current Object
   * @throws RepositoryException 
   * @throws PathNotFoundException 
   * @throws UnsupportedRepositoryOperationException 
   */
  public String getUUID() throws UnsupportedRepositoryOperationException, PathNotFoundException, RepositoryException {
    return getJCRNode().getUUID();
  }
  
  /**
   * 
   * @return
   * @throws RepositoryException 
   * @throws NoSuchWorkspaceException 
   * @throws LoginException 
   */
  public Session getSession() throws LoginException, NoSuchWorkspaceException, RepositoryException {
    if(isSystem) {
      return WCMCoreUtils.getSystemSessionProvider().getSession(workspace, WCMCoreUtils.getRepository());
    }
    return WCMCoreUtils.getUserSessionProvider().getSession(workspace, WCMCoreUtils.getRepository());
  }
  
  /**
   * @throws RepositoryException 
   * @throws NoSuchWorkspaceException 
   * @throws LoginException 
   * @throws NoSuchNodeTypeException 
   * @throws LockException 
   * @throws VersionException 
   * @throws InvalidItemStateException 
   * @throws ConstraintViolationException 
   * @throws ItemExistsException 
   * @throws AccessDeniedException 
   * 
   */
  public void save() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, 
                            VersionException, LockException, NoSuchNodeTypeException, LoginException, NoSuchWorkspaceException, 
                            RepositoryException {
    getSession().save();
  }

  @Override
  public void addMixin(String mixin) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, 
                                            LockException, PathNotFoundException, RepositoryException {
    getJCRNode().addMixin(mixin);
  }

  @Override
  public boolean canAddMixin(String mixin) throws NoSuchNodeTypeException, PathNotFoundException, RepositoryException {
    return getJCRNode().canAddMixin(mixin);
  }
}
