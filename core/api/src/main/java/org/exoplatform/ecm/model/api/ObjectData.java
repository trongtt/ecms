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

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:08 PM  
 */
public interface ObjectData {

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
  PropertyData getProperty(String pName);

  /**
   * Get Object properties  
   * @return
   */
  List<PropertyData> getProperties();
  
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
}
