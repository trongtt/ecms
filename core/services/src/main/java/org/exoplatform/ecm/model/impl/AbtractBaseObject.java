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
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.ecm.model.api.BaseObject;
import org.exoplatform.services.cms.folksonomy.NewFolksonomyService;
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
public abstract class AbtractBaseObject implements BaseObject {

    protected static final Log LOG = ExoLogger.getLogger(BaseObject.class); 
    protected String workspace;
    protected String path;
    protected String UUID;
    protected boolean isSystem = false;

    public AbtractBaseObject(String workspace, String path) {
        this.workspace = workspace;
        this.path = path;
    }

    public AbtractBaseObject(String workspace, String path, boolean isSystem) {
        this.workspace = workspace;
        this.path = path;
        this.isSystem = isSystem;
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
        return path;
    }

    /**
     * Get Object created date
     * @return
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     * @throws ValueFormatException 
     */
    public String getCreatedDate() {
        try {
            return getJCRNode().getProperty("exo:dateCreated").getString();
        } catch (ValueFormatException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        } catch (RepositoryException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        }
        return null;
    }

    /**
     * Get Object last modified date
     * @return
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     * @throws ValueFormatException 
     */
    public String getLastModifiedDate() {
        try {
            return getJCRNode().getProperty("exo:lastModifiedDate").getString();
        } catch (ValueFormatException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return null;
    }

    /**
     * Get Object Creator
     * @return
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     */
    public String getCreator() {
        try {
            return getJCRNode().getProperty("exo:owner").toString();
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return null;
    }

    /**
     * Get Object Primary Type
     * @return
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     */
    public String getPrimaryType() {
        try {
            return getJCRNode().getPrimaryNodeType().getName();
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return null;
    }

    /**
     * Get Object mixin types
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     */
    public List<String> getMixinTypes() {
        try {
            return new ArrayList<String>(Arrays.asList(((NodeImpl)getJCRNode()).getMixinTypeNames()));
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return new ArrayList<String>();
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
    public String getUUID() {
        try {
            return getJCRNode().getUUID();
        } catch (UnsupportedRepositoryOperationException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return null;
    }

    /**
     * 
     * @return
     * @throws RepositoryException 
     * @throws NoSuchWorkspaceException 
     * @throws LoginException 
     */
    protected Session getSession() throws LoginException, NoSuchWorkspaceException, RepositoryException {
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
    public void save() {
        try {
            getSession().save();
        } catch (AccessDeniedException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (ItemExistsException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (ConstraintViolationException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (InvalidItemStateException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (VersionException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (LockException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (NoSuchNodeTypeException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (LoginException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (NoSuchWorkspaceException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
    }

    @Override
    public void addMixin(String mixin) {
        try {
            getJCRNode().addMixin(mixin);
        } catch (NoSuchNodeTypeException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (VersionException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (ConstraintViolationException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (LockException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
    }

    @Override
    public boolean canAddMixin(String mixin) {
        try {
            return getJCRNode().canAddMixin(mixin);
        } catch (NoSuchNodeTypeException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return false;
    }

    @Override
    public String getTitle() {
        try {
            return getJCRNode().getProperty("exo:title").getString();
        } catch (ValueFormatException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        } catch (RepositoryException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        }
        return null;
    }

    @Override
    public String getLastModifier() {
        try {
            return getJCRNode().getProperty("exo:lastModifier").getString();
        } catch (ValueFormatException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        } catch (RepositoryException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        }
        return null;
    }

    @Override
    public String getOwner() {
        try {
            return getJCRNode().getProperty("exo:owner").getString();
        } catch (ValueFormatException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (PathNotFoundException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        } catch (RepositoryException e) {
            if(LOG.isDebugEnabled()) {
                LOG.error(e);
            }
        }
        return null;
    }
    
    public List<String> getTags() {
        return new ArrayList<String>();
    }
    
    public String getRating() {
        return StringUtils.EMPTY;
    }

    /**
     * 
     * @return
     */
    public abstract String getObjectType();  

    /**
     * 
     * @return
     */
    protected Node getJCRNode() {
        try {
            return (Node)getSession().getItem(path);
        } catch (PathNotFoundException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (LoginException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (NoSuchWorkspaceException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        } catch (RepositoryException e) {
           if(LOG.isDebugEnabled()) {
               LOG.error(e);
           }
        }
        return null;
    }
}
