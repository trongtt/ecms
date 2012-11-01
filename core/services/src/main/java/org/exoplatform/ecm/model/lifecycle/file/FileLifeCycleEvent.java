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
package org.exoplatform.ecm.model.lifecycle.file;

import org.exoplatform.ecm.model.api.FileData;
import org.exoplatform.ecm.model.lifecycle.ContentLifeCycleEvent;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 31, 2012
 * 2:03:39 PM  
 */
public class FileLifeCycleEvent extends ContentLifeCycleEvent<FileData> {

  public enum Type {FILE_CREATE, FILE_UPDATE, FILE_DELETE};
  
  private Type type;
  
  public FileLifeCycleEvent(Type type, FileData fileObj) {
    super(fileObj);
    this.type = type;
  }
  
}
