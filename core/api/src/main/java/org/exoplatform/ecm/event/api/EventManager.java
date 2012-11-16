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
package org.exoplatform.ecm.event.api;

import java.util.List;

import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 8, 2012
 * 1:47:47 PM  
 */
public interface EventManager<S, D> {

  public void addEventListener(Listener<S, D> listener);
  
  public void removeEventListener(Listener<S, D> listener);
  
  public void broadcastEvent(Event<S, D> event) throws Exception;
  
  public List<Listener<S, D>> getEventListeners(String type);
  
}
