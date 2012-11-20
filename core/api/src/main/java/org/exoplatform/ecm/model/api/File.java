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

import java.io.InputStream;
import java.util.Date;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 2, 2012
 * 2:14:08 PM  
 */
public interface File extends BaseObject {

    /**
     * 
     * @return
     */
    public InputStream getData();

    /**
     * 
     * @return
     */
    public String getMimeType();
    
    /**
     * 
     */
    public Date getLastModified();
    
    /**
     * 
     * @return
     */
    public String getEncoding();
    
    /**
     * 
     * @return
     */
    public String getDCCreator();
    
    /**
     * 
     * @return
     */
    public String getDCDescription();
    
    /**
     * 
     * @return
     */
    public String getDCContributor();
    
    /**
     * 
     * @return
     */
    public String getDCPublisher();
    
    /**
     * 
     * @return
     */
    public String getDCSubject();
    
    /**
     * 
     * @return
     */
    public Date getDCDate();
    
    
}
