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
package org.exoplatform.ecm.listeners.impl;

import org.exoplatform.ecm.listener.api.EventListener;
import org.exoplatform.ecm.model.api.EventType;
import org.exoplatform.ecm.model.impl.WCMFile;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 12, 2012
 * 5:44:45 PM  
 */
public class WCMFileListener extends Listener<WCMFile, Integer> implements EventListener<WCMFile>{

  @Override
  public void create(WCMFile file) {
    System.out.println("\n\nCreating wcm file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void update(WCMFile file) {
    System.out.println("\n\nUpdating wcm file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void remove(WCMFile file) {
    System.out.println("\n\nRemoving wcm file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void onEvent(Event<WCMFile, Integer> event) throws Exception {
    WCMFile wcmFile = event.getSource();
    int eventType = event.getData();
    switch(eventType) {
    case EventType.CREATED :
      create(wcmFile);
      break;
    case EventType.REMOVE :
      remove(wcmFile);
      break;
    case EventType.UPDATE :
      update(wcmFile);
      break;
    default :
      break;
    }    
  }

}
