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

import javax.jcr.Property;
import javax.jcr.Session;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 2, 2012
 * 2:13:57 PM  
 */
public interface ObjectData {

  ObjectData getInstance();
  
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
   * Get Object children  
   * @return
   */
  List<?> getChildren();

  /**
   * Get Object Property  
   * @param pName Property name
   * @return
   */
  Property getProperty(String pName);

  /**
   * Get Object properties  
   * @return
   */
  List<Property> getProperties();
  
  /**
   * Get Object created date
   * @return
   */
  String getCreatedDate();
  
  /**
   * Get Object last modified date
   * @return
   */
  String getLastModifiedDate();
  
  /**
   * Get Object Creator
   * @return
   */
  String getCreator();
  /**
   * Get Object Parent
   */
  ObjectData getParent();
  
  /**
   * Get Object Primary Type
   * @return
   */
  String getPrimaryType();
  
  /**
   * Get Object mixin types
   */
  List<String> getMixinTypes();
  
  /**
   * Get workspace name where stored current object.
   * @return Name of Workspace
   */
  String getWorkspace();
  
  /**
   * Get Object UUID
   * @return UUID of current Object
   */
  String getUUID();
  
  /**
   * 
   * @return
   */
  Session getSession();
  
  /**
   * 
   */
  void save();
}
