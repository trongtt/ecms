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
package org.exoplatform.ecm.model.lifecycle;

import java.util.HashSet;
import java.util.Set;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;


/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 31, 2012
 * 2:12:23 PM  
 */
public abstract class AbstractContentLifeCycle <T extends ContentLifeCycleListener<E>, E extends ContentLifeCycleEvent<?>>  {

  private static final Log LOG = ExoLogger.getLogger(AbstractContentLifeCycle.class);

  protected Set<T> listeners = new HashSet<T>();
  
  protected AbstractContentLifeCycle() {
    
  }
  
  public void addListener(T listener) {
    listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removeListener(T listener) {
    listeners.remove(listener);
  }

  /**
   * Broadcasts an event to the registered listeners. The event is broadcasted
   * asynchronously but sequentially.
   *
   * @see #dispatchEvent(ContentLifeCycleListener, ContentLifeCycleEvent)
   * @param event
   */
  protected void broadcast(final E event) {
    for (T listener : listeners) {
      try {
        dispatchEvent(listener, event);
      }
      catch (Exception e) {
        LOG.error(e);
      }
    }
  }
  
  protected abstract void dispatchEvent(final T listener, final E event);
}
