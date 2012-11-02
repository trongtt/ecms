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

import java.util.List;

import javax.jcr.Property;
import javax.jcr.Session;

import org.exoplatform.ecm.api.model.ObjectData;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:08 PM  
 */
public class ObjectDataImpl implements ObjectData {

  protected String name;
  protected String path;
  protected String UUID;
  
  public ObjectDataImpl(String name, String path) {
    this.name = name;
    this.path = path;
  }
  
  public ObjectDataImpl(String UUID) {
    this.UUID = UUID;
  }
  
  public ObjectDataImpl getInstance() {
    return null;
  }
  
  /**
   * Get object name  
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Get Object path  
   * @return
   */
  public String getPath() {
    return path;
  }

  /**
   * Get Object children  
   * @return
   */
  public List<?> getChildren() {
    return null;
  }

  /**
   * Get Object Property  
   * @param pName Property name
   * @return
   */
  public Property getProperty(String pName) {
    return null;
  }

  /**
   * Get Object properties  
   * @return
   */
  public List<Property> getProperties() {
    return null;
  }
  
  /**
   * Get Object created date
   * @return
   */
  public String getCreatedDate() {
    return null;
  }
  
  /**
   * Get Object last modified date
   * @return
   */
  public String getLastModifiedDate() {
    return null;
  }
  
  /**
   * Get Object Creator
   * @return
   */
  public String getCreator() {
    return null;
  }
  
  /**
   * Get Object Parent
   */
  public ObjectData getParent() {
    return null;
  }
  
  /**
   * Get Object Primary Type
   * @return
   */
  public String getPrimaryType() {
    return null;
  }
  
  /**
   * Get Object mixin types
   */
  public List<String> getMixinTypes() {
    return null;
  }
  
  /**
   * Get workspace name where stored current object.
   * @return Name of Workspace
   */
  public String getWorkspace() {
    return null;
  }
  
  /**
   * Get Object UUID
   * @return UUID of current Object
   */
  public String getUUID() {
    return UUID;
  }
  
  /**
   * 
   * @return
   */
  public Session getSession() {
    return null;
  }
  
  /**
   * 
   */
  public void save() {
    
  }
}
