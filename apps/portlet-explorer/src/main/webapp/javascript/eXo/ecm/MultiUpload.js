function MultiUpload() {
  this.ws = "collaboration";
  this.drive = "collaboration";
  this.driveTitle = "collaboration";
  this.drivePath = "/";
  this.path = "/";
  this.dropNodePath = "/";
  this.portalName = eXo.env.portal.portalName;
  this.context = eXo.env.portal.context;
  this.userId = eXo.env.portal.userName;
  this.userLanguage = eXo.env.portal.language;
  this.restContext = eXo.ecm.WCMUtils.getRestContext();
  this.uploadingFileCount = 0;
  this.invalidFiles = 0;
//--------------Constraints--------------------------//  
  this.maxFileSize = 10;//MB
  this.maxUploadCount = 2;
  this.document = document;
//--------------Collection data type------------------//  
  this.pathMap = {};
  this.cancelRequestMap = {};
  this.sizeMap = {};
  this.uploadingFileIds = {};
  this.percentMap = {};
  this.fileType = {};
  this.connectionFailed = {};
  this.existingBehavior = {};
  this.uploadingFileQueue = [];
};

PROCESS = "Process";
WAIT = "Wait";

UPLOADED = "MultiUploadFilesUploaded";
UPLOADING = "MultiUploadFilesUploading";
CANCELED = "MultiUploadFilesCanceled";
AWAITING = "MultiUploadFilesAwaiting";
WAITING_TXT = "Waiting";
IN = "in";
OR = "or";
INUSE = "AlreadyInUse";
KEEP = "Keep";
REPLACE = "Replace";
CANCEL_TXT = "Canceled";
CANCEL = "Cancel";
ABORT_ALL = "AbortAllConfirmation";
ERROR = "Error";


SUPPORT_FILE_API = window.FileReader;

MAX_CONNECTION = 10;

MultiUpload.prototype.getMsg = function(msg) {
	return eXo.ecm.WCMUtils.getBundle("UIMultiUpload.label." + msg, eXo.env.portal.language);
};

MultiUpload.prototype.loadMsg = function() {
	IN = eXo.ecm.MultiUpload.getMsg("in");
	MAX_SIZE_ALERT = eXo.ecm.MultiUpload.getMsg("MaxFileSizeAlert");
	WAITING_TXT = eXo.ecm.MultiUpload.getMsg("Waiting");
	ERROR = eXo.ecm.MultiUpload.getMsg("Error");
	OR = eXo.ecm.MultiUpload.getMsg("or");
	INUSE = eXo.ecm.MultiUpload.getMsg("AlreadyInUse");
	KEEP = eXo.ecm.MultiUpload.getMsg("Keep");
	REPLACE = eXo.ecm.MultiUpload.getMsg("Replace");
	CANCEL_TXT = eXo.ecm.MultiUpload.getMsg("Canceled");
	CANCEL = eXo.ecm.MultiUpload.getMsg("Cancel");
	ABORT_ALL = eXo.ecm.MultiUpload.getMsg("AbortAllConfirmation");
};
//---------------All setter methods---------------------//
MultiUpload.prototype.setMaxFileSize = function(value) {
  eXo.ecm.MultiUpload.maxFileSize = value;
};

MultiUpload.prototype.setDropFileMessage = function(value) {
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile").innerHTML = value;
};

MultiUpload.prototype.setMaxUploadCount = function(value) {
  eXo.ecm.MultiUpload.maxUploadCount = value;
};
//------------end of setter list-------------------------//
//-------------------------------------------------------//
//------------Event handler related methods--------------//
MultiUpload.prototype.enableMultiUploadBox = function(event) {
  evt = event || window.event;	
  multiUploadDiv = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  if (multiUploadDiv && evt.dataTransfer && evt.dataTransfer.types.length > 0) {
    if (!(evt.dataTransfer.types[0].indexOf("text")==0)) {
    	eXo.ecm.MultiUpload.openMultiUploadBox();
    }
  }
};

MultiUpload.prototype.openMultiUploadBox = function() {
    var multiUploadDiv = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
    
	multiUploadDiv.className="UIMultiUpload";
	//focus UIDocumentInfo
	div=eXo.ecm.MultiUpload.document.getElementById("UIDocumentInfo");
	try {
		div.addEventListener("dragover", eXo.ecm.MultiUpload.focus, false);
		div.addEventListener("dragleave", eXo.ecm.MultiUpload.outFocus, false);
		div.addEventListener("mouseout", eXo.ecm.MultiUpload.outFocus, false);
		div.addEventListener("drop", eXo.ecm.MultiUpload.stopEvent, false);
	} catch (e) {
		//avoid error with IE7
	}
};

MultiUpload.prototype.disableMultiUploadBox = function(event) {
  var multiUploadDiv = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  var evt =  event || window.event;
  if (evt.clientX > 30 && evt.clientX < document.documentElement.clientWidth - 30 &&
	  evt.clientY > 30 && evt.clientY < document.documentElement.clientHeight - 30) {
	  return;
  }
  if (multiUploadDiv) {
  	if (noFileDropped()) {
  		multiUploadDiv.className="UIMultiUpload NoShow";
  	}
  }
};

MultiUpload.prototype.closeMultiUploadBox = function(event) {
  var multiUploadDiv = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  multiUploadDiv.className="UIMultiUpload NoShow";
}

MultiUpload.prototype.outFocus = function() {
  var div = eXo.ecm.MultiUpload.document.getElementById("UIDocumentInfo");
  div.style.background="";
};

MultiUpload.prototype.focus = function(event) {
  evt = event || window.event;
  evt.stopPropagation();
  evt.preventDefault();
	
  var div = eXo.ecm.MultiUpload.document.getElementById("UIDocumentInfo");
  div.style.background="#F5F5FF";
  //enable drop zone;
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile").style.display="block";
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFileBackground").style.display="block";
};

MultiUpload.prototype.stopEvent = function(event) { 
  evt = event || window.event;
  evt.stopPropagation();
  evt.preventDefault();
};

MultiUpload.prototype.registerEvents = function(divId) {
  var div = eXo.ecm.MultiUpload.document.getElementById(divId);
  if (!div) return;
  div.addEventListener("drop", eXo.ecm.MultiUpload.drop, false);
  div.addEventListener("drop", eXo.ecm.MultiUpload.stopEvent, false);
  div.addEventListener("dragenter", eXo.ecm.MultiUpload.enableMultiUploadBox, false);
//  div.addEventListener("dragover", eXo.ecm.MultiUpload.enableMultiUploadBox, false);  
  div.addEventListener("dragenter", eXo.ecm.MultiUpload.focus, false);
  div.addEventListener("dragleave", eXo.ecm.MultiUpload.outFocus, false);
  div.addEventListener("dragend", eXo.ecm.MultiUpload.outFocus, false);
  div.addEventListener("mouseout", eXo.ecm.MultiUpload.outFocus, false);
  
  div = eXo.ecm.MultiUpload.document.getElementById("UIJCRExplorerPortlet");
  eXo.ecm.MultiUpload.document.addEventListener("dragleave", eXo.ecm.MultiUpload.disableMultiUploadBox, false);
};

MultiUpload.prototype.unregisterEvents = function() {
  var dropbox = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  if (dropbox) {  
	  dropbox.removeEventListener("dragenter", eXo.ecm.MultiUpload.dragEnterHandler, false);
	  dropbox.removeEventListener("dragover", eXo.ecm.MultiUpload.dragEnterHandler, false);
	  dropbox.removeEventListener("dragleave", eXo.ecm.MultiUpload.dragExitHandler, false);
	  dropbox.removeEventListener("dragend", eXo.ecm.MultiUpload.dragExitHandler, false);
	  dropbox.removeEventListener("mouseout", eXo.ecm.MultiUpload.dragExitHandler, false);
	  dropbox.removeEventListener("mouseup", eXo.ecm.MultiUpload.dragExitHandler, false);
	  dropbox.removeEventListener("drop", eXo.ecm.MultiUpload.drop, false);
  }
  var div = eXo.ecm.MultiUpload.document.getElementById("UIDocumentInfo");
  if (!div) return;
  div.removeEventListener("drop", eXo.ecm.MultiUpload.drop, false);
  div.removeEventListener("drop", eXo.ecm.MultiUpload.stopEvent, false);

  var div = eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile");
  if (div) {
	  div.style.display="none";
  }
  div = eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFileBackground");
  if (div) {
	  div.style.display="none";
  }
};


MultiUpload.prototype.setLocation = function(workspace, driveName, driveTitle, nodePath, drivePath) {
  if (nodePath.indexOf(drivePath) == 0) {
	  nodePath = nodePath.substring(drivePath.length);
  }
  eXo.ecm.MultiUpload.ws = workspace;
  eXo.ecm.MultiUpload.drive = driveName;
  eXo.ecm.MultiUpload.path = nodePath;
  eXo.ecm.MultiUpload.dropNodePath = nodePath;
  eXo.ecm.MultiUpload.drivePath = drivePath;
  eXo.ecm.MultiUpload.driveTitle = driveTitle;
};

MultiUpload.prototype.initDropBox = function(divid) {
  var dropbox = eXo.ecm.MultiUpload.document.getElementById(divid);
  dropbox.addEventListener("dragenter", eXo.ecm.MultiUpload.dragEnterHandler, false);
  dropbox.addEventListener("dragover", eXo.ecm.MultiUpload.dragEnterHandler, false);
  dropbox.addEventListener("dragleave", eXo.ecm.MultiUpload.dragExitHandler, false);
//  dropbox.addEventListener("dragend", eXo.ecm.MultiUpload.dragExitHandler, false);
  dropbox.addEventListener("mouseout", eXo.ecm.MultiUpload.dragExitHandler, false);
//  dropbox.addEventListener("mouseup", eXo.ecm.MultiUpload.dragExitHandler, false);
  dropbox.addEventListener("drop", eXo.ecm.MultiUpload.drop, false);
  
  var abortAll = eXo.ecm.MultiUpload.document.getElementById("MultiUploadAbortAll");
  abortAll.addEventListener("click", eXo.ecm.MultiUpload.abortAll, false);
};

MultiUpload.prototype.dragEnterHandler = function(event) {
  evt = event || window.event;
  evt.stopPropagation();
  evt.preventDefault();
  dropbox= eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");        
  dropbox.style.background="pink";
  //enable drop zone;
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile").style.display="block";
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFileBackground").style.display="block";
};    

MultiUpload.prototype.dragExitHandler = function (event) {
  evt = event || window.event;
  if (evt) {
      evt.stopPropagation();
      evt.preventDefault();
  }
  dropbox = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  dropbox.style.background="#DDDDDF";//lightgrey
  //disable drop zone;
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile").style.display="none";
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFileBackground").style.display="none";
}; 
//--------------------end of Event handlers-------------------//
//------------------------------------------------------------//
//--------------------Util methods----------------------------//
function noFileDropped() {
	return (parseInt(eXo.ecm.MultiUpload.document.getElementById(UPLOADED).innerHTML) == 0) &&
	(parseInt(eXo.ecm.MultiUpload.document.getElementById(UPLOADING).innerHTML) == 0) &&
	(parseInt(eXo.ecm.MultiUpload.document.getElementById(CANCELED).innerHTML) == 0) &&
	(parseInt(eXo.ecm.MultiUpload.document.getElementById(AWAITING).innerHTML) == 0);
}


function getFileSizeFormat(byteSize) {
	byteSize = Math.round(byteSize);
	megabyte = Math.floor(byteSize/1048576);
	kilobyte = Math.floor((byteSize % 1048576) / 1024);
	byteValue = byteSize % 1024;
	if (megabyte > 0) {
		return megabyte + '.' + kilobyte + " Mb";
	} else if(kilobyte > 0) {
		return kilobyte + '.' + byteValue + " Kb";
	} else {
		return byteValue + " b";
	}
};

function cleanName(oldName) {
	
	if (!oldName || oldName==null ||oldName==undefined) return oldName;
	specialChar = "[]/'\":";
	var ret = "";
	for (var i = 0; i < oldName.length; i++) {
		if (specialChar.indexOf(oldName[i]) > -1) {
			ret += "-";
		} else {
			ret += oldName[i];
		}
	}
	return encodeURIComponent(ret);
};

MultiUpload.prototype.changeStatusValue = function(id, delta) {
  var box = eXo.ecm.MultiUpload.document.getElementById(id);
  var oldValue = parseInt(box.innerHTML)
  if (id == UPLOADING) {
	  if (oldValue == 0 && delta == -1) {
		  eXo.ecm.MultiUpload.changeStatusValue(AWAITING, delta);
	  } else {
		  box.innerHTML = oldValue + delta;
	  }
	  
  } else {
	  box.innerHTML = oldValue + delta;
  }
  if (box.innerHTML == "0") {
	  eXo.ecm.MultiUpload.document.getElementById(id + "-text").style.display="none";
	  box.style.display="none";
  } else {
	  eXo.ecm.MultiUpload.document.getElementById(id + "-text").style.display="";
	  box.style.display="";
  }
  //fix the ',' problem, remove the last ','
  var ids = [AWAITING, CANCELED, UPLOADING, UPLOADED];
  var st = 0;
  for (var i in ids) {
	  var div = eXo.ecm.MultiUpload.document.getElementById(ids[i] + "-text");
	  if (div) {
		  if (div.innerHTML.indexOf(',')==-1) {
			  div.innerHTML = div.innerHTML + ',';
		  } 
		  if ((st == 0) && div.style.display!='none') {
			  st = 1;
			  div.innerHTML = div.innerHTML.substring(0, div.innerHTML.length - 1);
		  }
	  }
  }
  //enable and disable the 'multiUploadClose' and 'multiUploadAbortAll' buttons
  var processingFiles = eXo.ecm.MultiUpload.processFiles();
  var close = eXo.ecm.MultiUpload.document.getElementById("MultiUploadClose");
  var abortAll = eXo.ecm.MultiUpload.document.getElementById("MultiUploadAbortAll");
  
  if (processingFiles == 0) {
	  close.addEventListener("click", eXo.ecm.MultiUpload.closeMultiUploadBox, false);
	  close.className = "MultiUploadClose FR DeleteIcon";
	  abortAll.style.display = "none";  
  } else {
	  close.removeEventListener("click", eXo.ecm.MultiUpload.closeMultiUploadBox, false);
	  close.className = "MultiUploadClose FR DeleteIconInactive";
	  abortAll.style.display = "";
  }

  eXo.ecm.MultiUpload.document.getElementById("MultiUploadClose").className =
	  (processingFiles == 0) ? "MultiUploadClose FR DeleteIcon" :
		  									  "MultiUploadClose FR DeleteIconInactive";
};

MultiUpload.prototype.processFiles = function() {
	return parseInt(eXo.ecm.MultiUpload.document.getElementById(UPLOADING).innerHTML) +
			parseInt(eXo.ecm.MultiUpload.document.getElementById(AWAITING).innerHTML);
};
//--------------------end of Util methods---------------------//
//------------------------------------------------------------//
//--------------------UI Related methods----------------------//
MultiUpload.prototype.enableDragItemArea = function(event, box) {
  box.style.background="darkgray";
  event.preventDefault();
};

MultiUpload.prototype.disableDragItemArea = function(box) {
  box.style.background="";
};

MultiUpload.prototype.doDropItemArea = function(event, box, path) {
  box.style.background="";
  if (path.indexOf(eXo.ecm.MultiUpload.drivePath) == 0) {
	  path = path.substring(eXo.ecm.MultiUpload.drivePath.length);
  }
  eXo.ecm.MultiUpload.dropNodePath = path;
  eXo.ecm.MultiUpload.drop(event);
};

MultiUpload.prototype.displayCorrectUploadButton = function() {
	var deletedDiv = document.getElementById(SUPPORT_FILE_API ? "UploadButtonDivIE" : "UploadButtonDiv");
	if (deletedDiv) {
		deletedDiv.parentNode.removeChild(deletedDiv);
	}
};


MultiUpload.prototype.insertFileName = function(listDivID, file, fileID, position) {
  //add file and fileID to map
  eXo.ecm.MultiUpload.uploadingFileIds[fileID] = file;
  //get the list div
  //add a <br/>
  var br = gj("<br/>");
  //add LoadContent div
  var loadContentDiv = gj("#LoadContent" + fileID)[0];
  if (loadContentDiv) {
	  loadContentDiv.innerHTML = "";
  } else {
	  loadContentDiv = eXo.ecm.MultiUpload.document.createElement("div");
	  loadContentDiv.className = "LoadContent";
	  loadContentDiv.id = "LoadContent" + fileID;
	  if (listDivID == PROCESS) {
		  //divList.append(br);
		  if (position && position == "TOP") {
			  var divList = gj("#MultiUploadFileListProgress", eXo.ecm.MultiUpload.document);
			  divList.prepend(loadContentDiv);
			  divList[0].scrollTop = 0;
		  } else {
//			  var seperator = gj("#MultiUploadListSeperator", eXo.ecm.MultiUpload.document)[0];
//			  gj(loadContentDiv).insertAfter(seperator);
			  var divList = gj("#MultiUploadFileListProgress", eXo.ecm.MultiUpload.document);
			  divList.append(loadContentDiv);
		  }
	  } else {//listdDivID == WAITING
		  //divList.append(br);
		  var divList = gj("#MultiUploadFileListProgress", eXo.ecm.MultiUpload.document);
		  divList.append(loadContentDiv);
	  }
  }
  //add file icon div
  var iconDiv = eXo.ecm.MultiUpload.document.createElement("div");
  iconDiv.className = "FL Icon16x16 default16x16Icon nt_file16x16Icon default16x16Icon " + file.type.replace(/\./g,'_').replace('/','_').replace('\\','_') + "16x16Icon";
  iconDiv.id = "icon" + fileID;
  iconDiv.innerHTML = "<span>&nbsp;</span>";
  iconDiv.style.width="50px";
  eXo.ecm.MultiUpload.fileType[fileID]=file.type;
  loadContentDiv.appendChild(iconDiv);
  //add file name div
  var info = eXo.ecm.MultiUpload.document.createElement("div");
  info.id = "file" + fileID;
  info.innerHTML = decodeURIComponent(cleanName(file.name));
  info.className = "FileName FL";
  if (eXo.ecm.MultiUpload.pathMap[fileID] != undefined) {
	  info.title = IN + " " + eXo.ecm.MultiUpload.driveTitle + (eXo.ecm.MultiUpload.pathMap[fileID].indexOf("/") == 0 ? "" : "/") + 
	  			   eXo.ecm.MultiUpload.pathMap[fileID];
  }
  loadContentDiv.appendChild(info);
  //return ret
  return loadContentDiv;
};

MultiUpload.prototype.insertFileNameWithMessage = function(listDivID, file, fileID, msg, position) {
	var loadContentDiv = eXo.ecm.MultiUpload.insertFileName(listDivID, file, fileID, position);
	var msgDiv = eXo.ecm.MultiUpload.document.createElement("div");
	msgDiv.className = "FR WarningIcon";
	msgDiv.innerHTML = msg;
	msgDiv.id = "msg" + fileID;
	loadContentDiv.appendChild(msgDiv);
};

MultiUpload.prototype.insertFileNameWithProgressBar = function(listDivID, file, fileID, req) {
	var loadContentDiv = eXo.ecm.MultiUpload.insertFileName(listDivID, file, fileID);
	//outer progress
    var outerProgress = eXo.ecm.MultiUpload.document.createElement("div");
    outerProgress.className = "LoaddingPercent FR";
    //progress
    var progress = eXo.ecm.MultiUpload.document.createElement("div");
    progress.id = fileID;
    progress.className = "Percent";
    progress.innerHTML = "0%";
    //span
    var spanProgress = eXo.ecm.MultiUpload.document.createElement("span");
    spanProgress.className="Loadding";
    spanProgress.style.width="0%";
    //empty div
    var span = eXo.ecm.MultiUpload.document.createElement("div");
    span.innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;";
    span.className="FR";
    //cancel
    var cancel = eXo.ecm.MultiUpload.document.createElement("div");
    cancel.id = "cancel" + fileID;
    cancel.className = "FR DeleteIcon";
    cancel.innerHTML = "&nbsp;";
    if (req) {
	    cancel.onclick=function abortUpload(myreq, evt) {
	      return function(evt) {
	        myreq.abort();
	      }
	    }(req);
    } else {
    	cancel.onclick=eXo.ecm.MultiUpload.handleReaderAbort(fileID);
    }
    //-------------------------------------
    outerProgress.appendChild(spanProgress);    
    outerProgress.appendChild(progress);
    //---
    loadContentDiv.appendChild(cancel);
    loadContentDiv.appendChild(span);
    loadContentDiv.appendChild(outerProgress);
    //return ret
    return loadContentDiv;
};

MultiUpload.prototype.removeFileName = function(id) {
	var loadContentDiv = eXo.ecm.MultiUpload.document.getElementById("LoadContent" +id);
	if (loadContentDiv) {
		loadContentDiv.parentNode.removeChild(loadContentDiv);
	}
};
//--------------------End of UI related methods---------------//
//------------------------------------------------------------//
//--------------------file processing methods-----------------//
MultiUpload.prototype.drop = function(event) {
  //disable drop zone;
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFile").style.display="none";
  eXo.ecm.MultiUpload.document.getElementById("MultiUploadDragFileBackground").style.display="none";
  //enable multiupload box
  var multiUploadDiv = eXo.ecm.MultiUpload.document.getElementById("UIMultiUpload");
  multiUploadDiv.className="UIMultiUpload";
  //process drop
  evt = event || window.event;
  evt.stopPropagation();
  evt.preventDefault();
  var files = evt.dataTransfer.files;
  var count = files.length;
  if (count > 0) {
    eXo.ecm.MultiUpload.handleFiles(files, evt.dataTransfer.items);
  }
  eXo.ecm.MultiUpload.dropNodePath = eXo.ecm.MultiUpload.path;
};

//Starting points to handle files
MultiUpload.prototype.handleFiles = function(files, items) {
	eXo.ecm.MultiUpload.document.getElementById("MultiUploadHelp").style.display="";
	if (eXo.ecm.MultiUpload.processFiles() == 0) {
		var close = eXo.ecm.MultiUpload.document.getElementById("MultiUploadClose");
		close.addEventListener("click", eXo.ecm.MultiUpload.closeMultiUploadBox, false);
		close.className = "MultiUploadClose FR DeleteIcon";
	}
	
	var ids = new Array();
	var count = 0;
	for (var i = 0; i < files.length; i++) {
	  var file = files[i];
	  if (file.size > eXo.ecm.MultiUpload.maxFileSize * 1024 * 1024) {
		var msg  = MAX_SIZE_ALERT + " " +eXo.ecm.MultiUpload.maxFileSize + "Mb!";
		var id = Math.random().toString().substring(2);
		eXo.ecm.MultiUpload.insertFileNameWithMessage(PROCESS, file, id, msg, "TOP");
		eXo.ecm.MultiUpload.invalidFiles++;
		delete eXo.ecm.MultiUpload.uploadingFileIds[id];
	  } else {
		var isDirectory = false;
		if (items && items[i]) {
			try {
				isDirectory = items[i].webkitGetAsEntry().isDirectory;
			} catch (e) {
				isDirectory = false;
			}
		}
		if (!isDirectory && ( (file.size != 0 && file.size != 4096) || (file.type && file.type != "" && file.type != null) )) {
			ids[count] = Math.random().toString().substring(2);
		    eXo.ecm.MultiUpload.pathMap[ids[count]] = eXo.ecm.MultiUpload.dropNodePath;
		    eXo.ecm.MultiUpload.connectionFailed[ids[count]] = 0;
			eXo.ecm.MultiUpload.insertFileNameWithMessage(WAIT, file, ids[count], WAITING_TXT);
			eXo.ecm.MultiUpload.changeStatusValue(AWAITING, 1);
			count++;
		}
	  }
	}
	//of for
	for (var i = 0; i < ids.length; i++) {
		eXo.ecm.MultiUpload.processUploadRequest3(ids[i]);
	}
	//close MultiUpload component if there is no file
	if (count == 0 && noFileDropped() && eXo.ecm.MultiUpload.invalidFiles == 0) {
		eXo.ecm.MultiUpload.closeMultiUploadBox();
	}
};
//step1: check file existence on server side
MultiUpload.prototype.processUploadRequest3 = function(id) {
	//send request to check file existence
    var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/checkExistence?" +
    "repositoryName=repository&workspaceName=" + eXo.ecm.MultiUpload.ws +
    "&driverName=" + eXo.ecm.MultiUpload.drive +
    "&currentFolder=" + eXo.ecm.MultiUpload.pathMap[id] +
    "&currentPortal="+ eXo.ecm.MultiUpload.portalName +
    "&userId=" + eXo.ecm.MultiUpload.userId +
    "&fileName=" + cleanName(eXo.ecm.MultiUpload.uploadingFileIds[id].name) + 
    "&language=" + eXo.ecm.MultiUpload.userLanguage;
    
	gj.ajax({url: uri, 
	     success: function(result, status, xhr) {
			if (!result) {
				setTimeout(function(){eXo.ecm.MultiUpload.processUploadRequest3(id);}, 1000);
			}
		    var existed = result.getElementsByTagName("Existed");
		    if (existed && existed.length > 0) {
		    	//file already existed, inform for user
		    	eXo.ecm.MultiUpload.processUploadRequest4(id);
		    } else {
		    	//next step
		    	eXo.ecm.MultiUpload.existingBehavior[id] = "keep";
		    	eXo.ecm.MultiUpload.processUploadRequest1(id);
		    }
        },
        ajaxError: function() {
			 if (eXo.ecm.MultiUpload.connectionFailed[id]++ > MAX_CONNECTION) {
				 var e = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
				 e(window.event);
			 } else {
			    	setTimeout(function(){eXo.ecm.MultiUpload.processUploadRequest3(id);}, 1000);	        		 
			 }
        }
	});
};

//inform user that file already existed on server, 
//ask user to 'replace' or 'keep both'
MultiUpload.prototype.processUploadRequest4 = function(id) {
  //remove UI of file in waiting list
  eXo.ecm.MultiUpload.removeFileName(id);
	//inform message
  var file = eXo.ecm.MultiUpload.uploadingFileIds[id];
  var loadContentDiv = eXo.ecm.MultiUpload.insertFileName(PROCESS, file, id, "TOP");
	
  var span = eXo.ecm.MultiUpload.document.createElement("div");
  span.innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;";
  span.className="FR";
  span.id="span" + id;
  //span1
  var span1 = eXo.ecm.MultiUpload.document.createElement("div");
  span1.innerHTML=",&nbsp;";
  span1.className="FR";
  span1.id="span1" + id;
  //span2
  var span2 = eXo.ecm.MultiUpload.document.createElement("div");
  span2.innerHTML="&nbsp;" + OR + "&nbsp;";
  span2.className="FR";
  span2.id="span2" + id;
  //keepBoth
  var keepBoth = eXo.ecm.MultiUpload.document.createElement("div");
  keepBoth.id="keepBoth" + id;
  //replaceDiv
  var replaceDiv = eXo.ecm.MultiUpload.document.createElement("div");
  replaceDiv.id="replaceDiv" + id;
  //cancel
  var cancel = eXo.ecm.MultiUpload.document.createElement("div");
  cancel.id = "cancel" + id;

	//add msg 'already in use'
	var msgDiv = eXo.ecm.MultiUpload.document.createElement("div");
	msgDiv.className = "FR WarningIcon";
	msgDiv.innerHTML = INUSE;
	msgDiv.id = "msg" + id;
	
	loadContentDiv.appendChild(cancel);
	loadContentDiv.appendChild(span2);
	loadContentDiv.appendChild(replaceDiv);
	loadContentDiv.appendChild(span1);
	loadContentDiv.appendChild(keepBoth);
	loadContentDiv.appendChild(span);
	loadContentDiv.appendChild(msgDiv);
	
  //keepBoth
  keepBoth.className = "FR Action"
  keepBoth.innerHTML = "<u>" + KEEP + "</u>";
  keepBoth.onclick=function abortUpload(myFileDiv, fileId, evt) {
    return function(evt) {
  	myFileDiv.parentNode.removeChild(myFileDiv);
  	//remove UI of file in waiting list	
  	eXo.ecm.MultiUpload.removeFileName(fileId);
  	//upload file, keep both old and new file
  	  eXo.ecm.MultiUpload.existingBehavior[fileId] = "keep";
      eXo.ecm.MultiUpload.processUploadRequest1(fileId);
    }
  }(loadContentDiv, id);
  //replace
  replaceDiv.className = "FR Action";
  replaceDiv.innerHTML = "<u>" + REPLACE + "</u>";
  replaceDiv.onclick=function abortUpload(myFileDiv, myfile, evt) {
    return function(evt) {
  	myFileDiv.parentNode.removeChild(myFileDiv);
  	//remove UI of file in waiting list	
  	eXo.ecm.MultiUpload.removeFileName(myfile);
  	//upload file, replace old file by new file
  	  eXo.ecm.MultiUpload.existingBehavior[myfile] = "replace";
      eXo.ecm.MultiUpload.processUploadRequest1(myfile);
    }
  }(loadContentDiv, id);
  //cancel
  cancel.className = "FR Action";
  cancel.innerHTML = "<u>" + CANCEL + "</u>";
  cancel.onclick = eXo.ecm.MultiUpload.handleReaderAbort(id, CANCEL_TXT);
};

//first check with maximal number of uploading file
//if uploading file count >= max -> push to queue
//else jump to step 2
MultiUpload.prototype.processUploadRequest1 = function(id) {
  if (eXo.ecm.MultiUpload.uploadingFileCount < eXo.ecm.MultiUpload.maxUploadCount) {
	  eXo.ecm.MultiUpload.uploadingFileCount ++;
	 //process upload file
	 eXo.ecm.MultiUpload.processUploadRequest2(id) 
  } else {
	  eXo.ecm.MultiUpload.uploadingFileQueue.push(id);
  }
};
//step2: ask if upload is available on server side
MultiUpload.prototype.processUploadRequest2 = function(id) {
	//send request to ask if upload file is available
	var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/checkUploadAvailable";
	//gui request len server de check neu upload available
	gj.ajax({url: uri, 
		     success: function(result, status, xhr) {
				if (!result) {
					setTimeout(function(){eXo.ecm.MultiUpload.processUploadRequest2(id);}, 1000);
				}
			    var info = result.getElementsByTagName("uploadAvailable");
			    if (info && info.length > 0) {
			    	//upload available -> check file existence
			    	eXo.ecm.MultiUpload.processUploadRequest5(id);
			    } else {
			    	//set time out 5s to check upload available again
			    	setTimeout(function(){eXo.ecm.MultiUpload.processUploadRequest2(id);}, 5000);
			    }
	         },
	         ajaxError: function() {
	        	 if (eXo.ecm.MultiUpload.connectionFailed[id]++ > MAX_CONNECTION) {
	        		 var e = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
	        		 e(window.event);
	        	 } else {
			    	setTimeout(function(){eXo.ecm.MultiUpload.processUploadRequest2(id);}, 1000);	        		 
	        	 }
	         }
	});
};

//upload file
MultiUpload.prototype.processUploadRequest5 = function(id) {
	var file = eXo.ecm.MultiUpload.uploadingFileIds[id];
	if (SUPPORT_FILE_API) {
	    //Add XMLHttpRequest to upload file
	    var req = new XMLHttpRequest();
	    var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/upload?uploadId=" + id;
	    var loadContentDiv = eXo.ecm.MultiUpload.insertFileNameWithProgressBar(PROCESS, file, id, req);
		//set value
	    eXo.ecm.MultiUpload.sizeMap[id] = file.size;
		//set handler methods for request
	    req.onload = eXo.ecm.MultiUpload.handleReaderLoad(id);
	    req.upload.onloadstart = eXo.ecm.MultiUpload.handleReaderStart(id);
	    req.upload.onabort = eXo.ecm.MultiUpload.handleReaderAbort(id, CANCEL_TXT);
	    req.upload.onerror = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
	    //begin the send operation
	    var fd = new FormData();
	    req.open("POST", uri, true);
	    fd.append(id, file);
	    req.send(fd);
	} else {
		eXo.ecm.MultiUpload.insertFileNameWithProgressBar(PROCESS, file, id, null);
		eXo.ecm.MultiUpload.formUpload.submit();
		eXo.ecm.MultiUpload.handleReaderStart(id);
	}
};
//when reader start
MultiUpload.prototype.handleReaderStart = function(id) {
	//update the number of uploading files
	eXo.ecm.MultiUpload.changeStatusValue(UPLOADING, 1);
	eXo.ecm.MultiUpload.changeStatusValue(AWAITING, -1);
	//create an interval call for ask progress of uploading file.
	eXo.ecm.MultiUpload.percentMap[id] = 0;
	eXo.ecm.MultiUpload.handleReaderProgress(id);
};
//when reader in progress
MultiUpload.prototype.handleReaderProgress= function(id) {
    var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/control?" +
    "repositoryName=repository&workspaceName=" + eXo.ecm.MultiUpload.ws +
    "&driverName=" + eXo.ecm.MultiUpload.drive +
    "&currentFolder=" + eXo.ecm.MultiUpload.pathMap[id] +
    "&currentPortal="+ eXo.ecm.MultiUpload.portalName +
    "&userId=" + eXo.ecm.MultiUpload.userId +
    "&action=progress&uploadId=" + id;
    gj.ajax({url: uri, 
	     success: function(ret) {
    		if (!ret) {
				setTimeout(function(){eXo.ecm.MultiUpload.handleReaderProgress(id)}, 1000);
    		}
			var nodeList = ret.getElementsByTagName("UploadProgress");
			if(!nodeList || nodeList.length == 0) {
               setTimeout(function(){eXo.ecm.MultiUpload.handleReaderProgress(id)}, 1000);
               return;
            }
			var oProgress;
			if(nodeList.length > 0) oProgress = nodeList[0];
			var nPercent = oProgress.getAttribute("percent");
			var uploadedSize = oProgress.getAttribute("uploadedSize");
			var totalSize = oProgress.getAttribute("totalSize");
			var fileName = oProgress.getAttribute("fileName");
			var filetype = oProgress.getAttribute("fileType");
			if (nPercent && nPercent != "0") {
				if (filetype && filetype!= "null") eXo.ecm.MultiUpload.fileType[id]=filetype;
				if (totalSize && totalSize!= "null") eXo.ecm.MultiUpload.sizeMap[id] = totalSize;
				if (eXo.ecm.MultiUpload.percentMap[id] == nPercent) {
				 if (eXo.ecm.MultiUpload.connectionFailed[id]++ > MAX_CONNECTION) {
					 var e = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
					 e(window.event);
					 return;
				 }
				} else {
					eXo.ecm.MultiUpload.percentMap[id] = nPercent;
					eXo.ecm.MultiUpload.connectionFailed[id] = 0;
				}
			}
			progMeter = gj("#" + id, eXo.ecm.MultiUpload.document)[0];
			if (progMeter) {
				progMeter.innerHTML = nPercent + "%";
				gj(progMeter).prev("span").width(nPercent + "%");
			}
			if (!nPercent || nPercent < 100) {
				if (nPercent == 0) {
					eXo.ecm.MultiUpload.connectionFailed[id]++;
				}
				if (eXo.ecm.MultiUpload.connectionFailed[id] <= MAX_CONNECTION) {
					setTimeout(function(){eXo.ecm.MultiUpload.handleReaderProgress(id)}, 1000);
				}
			} else {
				if (!SUPPORT_FILE_API) {
					var e = eXo.ecm.MultiUpload.handleReaderLoad(id, "keep");
					e(window.event);
				}
			}	    
		},
        ajaxError: function() {
			 if (eXo.ecm.MultiUpload.connectionFailed[id]++ > MAX_CONNECTION) {
				 var e = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
				 e(window.event);
			 } else {
			    	setTimeout(function(){eXo.ecm.MultiUpload.handleReaderProgress(id);}, 1000);	        		 
			 }
		}
    });
};

MultiUpload.prototype.handleReaderLoad = function(progressID, evt) {
    // use lengthComputable, loaded, and total on ProgressEvent
    return function(evt) {
      //send save request
      //alert(eXo.ecm.MultiUpload.path + "----" + eXo.ecm.MultiUpload.pathMap[progressID]);
      if (eXo.ecm.MultiUpload.cancelRequestMap[progressID]) {
    	  return;
      }
      var file = eXo.ecm.MultiUpload.uploadingFileIds[progressID];
      var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/control?" +
      "repositoryName=repository&workspaceName=" + eXo.ecm.MultiUpload.ws +
      "&driverName=" + eXo.ecm.MultiUpload.drive +
      "&currentFolder=" + eXo.ecm.MultiUpload.pathMap[progressID] +
      "&currentPortal="+ eXo.ecm.MultiUpload.portalName +
      "&userId=" + eXo.ecm.MultiUpload.userId +
      "&action=save&uploadId=" + progressID +
      "&fileName=" + cleanName(file.name) + 
      "&language=" + eXo.ecm.MultiUpload.userLanguage +
      "&existenceAction=" + eXo.ecm.MultiUpload.existingBehavior[progressID];
      gj.ajax({url: uri, 
 	     success: function(ret, status, xhr) {
    	  //mark OK
    	  if (eXo.ecm.MultiUpload.connectionFailed[progressID] > MAX_CONNECTION) {
    		var e = eXo.ecm.MultiUpload.handleReaderAbort(progressID, ERROR);
    		e(window.event);
    	  	return;
    	  }
    	  var progMeter = gj("#" + progressID, eXo.ecm.MultiUpload.document)[0];
    	  var cancelButton = gj("#cancel" + progressID, eXo.ecm.MultiUpload.document)[0];
    	  if (progMeter && cancelButton) {
	    	  progMeter.parentNode.parentNode.removeChild(progMeter.parentNode);
	    	  cancelButton.innerHTML= getFileSizeFormat(eXo.ecm.MultiUpload.sizeMap[progressID]);
	    	  cancelButton.className = "FileSize FR";
    	  } else {
			var e = eXo.ecm.MultiUpload.handleReaderLoad(progressID);
			setTimeout(function(){e(window.event);}, 1000);
   		  	return;
    	  }
    	  //set mimetype
    	  if (ret) {
    		  var result = ret.getElementsByTagName("Result");
    		  if (result && result.length > 0) {
    			  var filetype = result[0].getAttribute("mimetype");
		    	  var icon = gj("#icon" + progressID, eXo.ecm.MultiUpload.document)[0];
		    	  if (filetype && icon && (filetype.indexOf("image") == -1)) {
					var className = icon.className;
					var typeFormatted = filetype.replace(/\./g, "_").replace("/", "_").replace("\\","_");
					if (className.indexOf(typeFormatted) == -1) {
						icon.className = className + " " + typeFormatted + "16x16Icon";
					}
		      	  }
    		  }
    	  }
    	  delete eXo.ecm.MultiUpload.uploadingFileIds[progressID];      
    	  //change uploading files value
    	  eXo.ecm.MultiUpload.changeStatusValue(UPLOADING, -1);
    	  //change uploaded files value
    	  eXo.ecm.MultiUpload.changeStatusValue(UPLOADED, 1);
    	  //load image thumbnail
    	  var nodePath = (eXo.ecm.MultiUpload.drivePath.length <= 1 ? "":"/" + eXo.ecm.MultiUpload.drivePath) + 
    	  (eXo.ecm.MultiUpload.pathMap[progressID].length <= 1 ? "" : "/" + eXo.ecm.MultiUpload.pathMap[progressID]) +
    	  "/" + cleanName(file.name);
		  var icon = gj("#icon" + progressID, eXo.ecm.MultiUpload.document)[0];
    	  if (icon && eXo.ecm.MultiUpload.fileType[progressID].indexOf("image") != -1) {
    		  icon.className="FL";
    		  icon.innerHTML="<img style='margin-top: -8px; height: 25px; width: 25px;' src='" +
    		  eXo.ecm.MultiUpload.restContext + "/thumbnailImage/small/repository/" +
    		  eXo.ecm.MultiUpload.ws + nodePath + "'>";
    	  }
    	  //add link to open file
    	  var fileDiv = gj("#file" + progressID, eXo.ecm.MultiUpload.document)[0];
    	  fileDiv.innerHTML = "<a href='" + eXo.env.server.portalBaseURL + "?path=" + eXo.ecm.MultiUpload.drive + nodePath + 
    	  					  "'>" + fileDiv.innerHTML + "</a>";
    	  //refresh UIJCRExplorer
    	  if (eXo.ecm.MultiUpload.processFiles() == 0) {
    		  var uiExplorer = eXo.ecm.MultiUpload.document.getElementById("MultiUploadRefreshExplorer");
    		  eXo.ecm.MultiUpload.document.window.eval(decodeURIComponent(uiExplorer.innerHTML));
    	  }
    	  //process next upload request
    	  eXo.ecm.MultiUpload.processNextUploadRequestInQueue();
      	 },
         ajaxError: function() {
			 if (eXo.ecm.MultiUpload.connectionFailed[id]++ > MAX_CONNECTION) {
				 var e = eXo.ecm.MultiUpload.handleReaderAbort(id, ERROR);
				 e(window.event);
			 } else {
			    	setTimeout(function(){eXo.ecm.MultiUpload.handleReaderLoad(id);}, 1000);	        		 
			 }
      	 }
      });
    }
};

MultiUpload.prototype.processNextUploadRequestInQueue = function() {
  if (eXo.ecm.MultiUpload.uploadingFileQueue.length == 0) {
	eXo.ecm.MultiUpload.uploadingFileCount --;
  } else {
	eXo.ecm.MultiUpload.processUploadRequest2(eXo.ecm.MultiUpload.uploadingFileQueue.shift());
  }
};

MultiUpload.prototype.handleReaderAbort = function(progressID, message, evt) {
  return function(evt) {
    var progMeter = gj("#" + progressID, eXo.ecm.MultiUpload.document)[0];
    var cancelButton = gj("#cancel" + progressID, eXo.ecm.MultiUpload.document)[0];
    if (progMeter && cancelButton) {
		progMeter.parentNode.parentNode.removeChild(progMeter.parentNode);
    } else {
    	gj("#cancel" + progressID, eXo.ecm.MultiUpload.document).remove();
    	msg = gj("#msg" + progressID, eXo.ecm.MultiUpload.document)[0];
    	//add cancel button
    	cancelButton = eXo.ecm.MultiUpload.document.createElement("div");
    	cancelButton.id = "cancel" + progressID;
    	msg.parentNode.appendChild(cancelButton);
    	//remove redundant elements
    	gj("#keepBoth" + progressID, eXo.ecm.MultiUpload.document).remove();
    	gj("#replaceDiv" + progressID, eXo.ecm.MultiUpload.document).remove();
    	gj("#span" + progressID, eXo.ecm.MultiUpload.document).remove();
    	gj("#span1" + progressID, eXo.ecm.MultiUpload.document).remove();
    	gj("#span2" + progressID, eXo.ecm.MultiUpload.document).remove();
    	gj("#msg" + progressID, eXo.ecm.MultiUpload.document).remove();
    }
    gj("#LoadContent" + progressID, eXo.ecm.MultiUpload.document).insertAfter("#MultiUploadListSeperator");
    cancelButton.className = "FileSize FR";
    cancelButton.innerHTML = message;
	cancelButton.style.color="darkgray";
	var filename = eXo.ecm.MultiUpload.document.getElementById("file" + progressID);
	filename.style.color="darkgray";
	delete eXo.ecm.MultiUpload.uploadingFileIds[progressID];
	//change uploading files value
	eXo.ecm.MultiUpload.changeStatusValue(UPLOADING, -1);
	//change canceled files value
	eXo.ecm.MultiUpload.changeStatusValue(CANCELED, 1);
	//send abort request to server
	var uri = eXo.ecm.MultiUpload.restContext + "/wcmDriver/uploadFile/control?" +
	   "repositoryName=repository&workspaceName=" + eXo.ecm.MultiUpload.ws +
	   "&driverName=" + eXo.ecm.MultiUpload.drive +
	   "&currentFolder=" + eXo.ecm.MultiUpload.pathMap[progressID] +
	   "&currentPortal="+ eXo.ecm.MultiUpload.portalName +
	   "&userId=" + eXo.ecm.MultiUpload.userId +
	   "&action=abort&uploadId=" + progressID;
	gj.ajax({url: uri});
	eXo.ecm.MultiUpload.processNextUploadRequestInQueue();
  }
};

MultiUpload.prototype.abortAll = function(event) {
    uploadingBox = eXo.ecm.MultiUpload.document.getElementById("MultiUploadFilesUploading");
    awaitingBox = eXo.ecm.MultiUpload.document.getElementById("MultiUploadFilesAwaiting");
	var number = parseInt(uploadingBox.innerHTML) + parseInt(awaitingBox.innerHTML);
	if (number < 1) {
		return;
	}
	var ret = confirm(ABORT_ALL);
	if (ret == true) {
		for (var i in eXo.ecm.MultiUpload.uploadingFileIds) {
			eXo.ecm.MultiUpload.cancelRequestMap[i] = true;
			e = eXo.ecm.MultiUpload.handleReaderAbort(i, CANCEL_TXT, event);
			e(event);
		}
	}
};
//-------------------------------------------------------------
//-------------------------------------------------------------
//-----------------------handleForIE---------------------------
MultiUpload.prototype.handleFileIE = function(input) {
	eXo.ecm.MultiUpload.ws = document.parentWindow.parent.eXo.ecm.MultiUpload.ws;
	eXo.ecm.MultiUpload.drive = document.parentWindow.parent.eXo.ecm.MultiUpload.drive;
	eXo.ecm.MultiUpload.driveTitle = document.parentWindow.parent.eXo.ecm.MultiUpload.driveTitle;
	eXo.ecm.MultiUpload.drivePath = document.parentWindow.parent.eXo.ecm.MultiUpload.drivePath;
	eXo.ecm.MultiUpload.path = document.parentWindow.parent.eXo.ecm.MultiUpload.path;
	eXo.ecm.MultiUpload.dropNodePath = document.parentWindow.parent.eXo.ecm.MultiUpload.dropNodePath;
	eXo.ecm.MultiUpload.pathMap = document.parentWindow.parent.eXo.ecm.MultiUpload.pathMap;
	eXo.ecm.MultiUpload.cancelRequestMap = document.parentWindow.parent.eXo.ecm.MultiUpload.cancelRequestMap;
	eXo.ecm.MultiUpload.sizeMap = document.parentWindow.parent.eXo.ecm.MultiUpload.sizeMap;
	eXo.ecm.MultiUpload.uploadingFileIds = document.parentWindow.parent.eXo.ecm.MultiUpload.uploadingFileIds;
	eXo.ecm.MultiUpload.percentMap = document.parentWindow.parent.eXo.ecm.MultiUpload.percentMap;
	
	eXo.ecm.MultiUpload.portalName = document.parentWindow.parent.eXo.ecm.MultiUpload.portalName;
	eXo.ecm.MultiUpload.context = document.parentWindow.parent.eXo.ecm.MultiUpload.context;
	eXo.ecm.MultiUpload.restContext = document.parentWindow.parent.eXo.ecm.MultiUpload.restContext;
	
	eXo.ecm.MultiUpload.userId = document.parentWindow.parent.eXo.ecm.MultiUpload.userId;
	eXo.ecm.MultiUpload.userLanguage = document.parentWindow.parent.eXo.ecm.MultiUpload.userLanguage;
	eXo.ecm.MultiUpload.maxFileSize = document.parentWindow.parent.eXo.ecm.MultiUpload.maxFileSize;
	eXo.ecm.MultiUpload.fileType = document.parentWindow.parent.eXo.ecm.MultiUpload.fileType;
	eXo.ecm.MultiUpload.existingBehavior = document.parentWindow.parent.eXo.ecm.MultiUpload.existingBehavior;
	
	eXo.ecm.MultiUpload.document = document.parentWindow.parent.document;
	gj = $;
	
	eXo.ecm.MultiUpload.uploadingFileCount = document.parentWindow.parent.eXo.ecm.MultiUpload.uploadingFileCount;
	eXo.ecm.MultiUpload.maxFileSize = document.parentWindow.parent.eXo.ecm.MultiUpload.maxFileSize;
	eXo.ecm.MultiUpload.maxUploadCount = document.parentWindow.parent.eXo.ecm.MultiUpload.maxUploadCount;
	eXo.ecm.MultiUpload.invalidFiles = document.parentWindow.parent.eXo.ecm.MultiUpload.invalidFiles;
	//--------------Collection data type------------------//  
	eXo.ecm.MultiUpload.connectionFailed = document.parentWindow.parent.eXo.ecm.MultiUpload.connectionFailed;
	eXo.ecm.MultiUpload.uploadingFileQueue = document.parentWindow.parent.eXo.ecm.MultiUpload.uploadingFileQueue;

	
    var progressId = Math.random().toString().substring(2);
    var uri = eXo.ecm.MultiUpload.restContext + 
    "/wcmDriver/uploadFile/upload?uploadId=" + progressId;
    var formUpload = input.parentNode;
	formUpload.action = uri;
    //formUpload.submit();
    eXo.ecm.MultiUpload.formUpload = formUpload;
    var file = {name:cleanName(input.value.substring(input.value.lastIndexOf('\\') + 1)),type: " ", size:0};
    eXo.ecm.MultiUpload.pathMap[progressId] = eXo.ecm.MultiUpload.path;
    eXo.ecm.MultiUpload.connectionFailed[progressId] = 0;
//	eXo.ecm.MultiUpload.insertFileNameWithMessage(WAIT, file, progressId, WAITING_TXT);
	eXo.ecm.MultiUpload.changeStatusValue(AWAITING, 1);
    eXo.ecm.MultiUpload.uploadingFileIds[progressId] = file;
    eXo.ecm.MultiUpload.fileType[progressId]=file.type;
    
	eXo.ecm.MultiUpload.document.getElementById("MultiUploadAbortAll").style.display="";
	eXo.ecm.MultiUpload.document.getElementById("MultiUploadHelp").style.display="";

	eXo.ecm.MultiUpload.processUploadRequest3(progressId);
};

eXo.ecm.MultiUpload = new MultiUpload();
_module.MultiUpload = eXo.ecm.MultiUpload;
//-------------------------------------------------------------------------//

