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
 * Nov 2, 2012
 * 2:13:57 PM  
 */
public interface BaseObject {

    /**
     * Get object name  
     * @return
     */
    public String getName();

    /**
     * Get Object path  
     * @return
     */
    public String getPath();

    /**
     * 
     * @return
     */
    public String getTitle();

    /**
     * Get Object created date
     * @return
     */
    public String getCreatedDate();

    /**
     * Get Object last modified date
     * @return
     */
    public String getLastModifiedDate();

    /**
     * 
     * @return
     */
    public String getLastModifier();

    /**
     * Get Object Creator
     * @return
     */
    public String getOwner();

    /**
     * Get Object Primary Type
     * @return
     */
    public String getPrimaryType();

    /**
     * Get Object mixin types
     */
    public List<String> getMixinTypes();

    /**
     * Get workspace name where stored current object.
     * @return Name of Workspace
     */
    public String getWorkspace();
    
    /**
     * 
     * @return
     */
    public List<String> getTags();
    
    /**
     * 
     * @return
     */
    public String getRating();

    /**
     * Get Object UUID
     * @return UUID of current Object
     */
    public String getUUID();

    /**
     * 
     */
    public void save();

    /**
     * Add mixin
     * @param mixin
     */
    public void addMixin(String mixin);

    /**
     * 
     * @param mixin
     * @return
     */
    public boolean canAddMixin(String mixin);
}
