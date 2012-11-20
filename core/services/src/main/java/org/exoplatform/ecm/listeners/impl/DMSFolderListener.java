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
import org.exoplatform.ecm.model.api.Folder;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 14, 2012
 * 2:16:42 PM  
 */
public class DMSFolderListener extends Listener<Folder, Integer> implements EventListener<Folder, Integer> {

    @Override
    public void create(Event<Folder, Integer> event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Event<Folder, Integer> event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(Event<Folder, Integer> event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEvent(Event<Folder, Integer> event) throws Exception {

    }

}
