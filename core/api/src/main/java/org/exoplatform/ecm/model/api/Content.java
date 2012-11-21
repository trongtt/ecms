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

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 2, 2012
 * 2:14:30 PM  
 */
public interface Content extends BaseObject {

    /**
     * Return the state of content
     * @return <code>true</code> if content is viewable and
     *         <code>otherwise</code>
     */
    public boolean isViewableContent();

    /**
     * Return the content type
     * @return Type of content such as exo:webContent or exo:accessibleMedia...
     */
    public String getContentType();

    /**
     * Return the label of content
     * @return The value of content label.
     */
    public String getContentLabel();

    /**
     * Return the a absolute path which point to content view template if it is available in workspace.
     * @return A absolute path to view template
     */
    public String getViewTemplate();

    /**
     * Return the a absolute path which point to content dialog template if it is available in workspace.
     * @return A absolute path to dialog template
     */
    public String getDialogTemplate();

}
