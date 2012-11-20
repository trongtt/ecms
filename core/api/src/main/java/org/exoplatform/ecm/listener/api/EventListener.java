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
package org.exoplatform.ecm.listener.api;

import org.exoplatform.services.listener.Event;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 13, 2012
 * 3:00:06 PM  
 */
public interface EventListener<S, D> {

  /**
   * 
   * @param event
   */
  public void create(Event<S, D> event);
  
  /**
   * 
   * @param event
   */
  public void update(Event<S, D> event);
  
  /**
   * 
   * @param event
   */
  public void remove(Event<S, D> event);
  
}
