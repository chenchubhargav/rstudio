/*
 * ObjectExplorerInspectionResult.java
 *
 * Copyright (C) 2009-17 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.workbench.views.source.editors.explorer.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

// NOTE: synchronize the structure of this object with the
// R object returned by 'explorer.createInspectionResult'
// defined in 'SessionObjectExplorer.R'
public class ObjectExplorerInspectionResult extends JavaScriptObject
{
   protected ObjectExplorerInspectionResult()
   {
   }
   
   public static final native ObjectExplorerInspectionResult create(
         String name,
         String type,
         String clazz,
         String desc,
         int size,
         int length,
         int depth,
         boolean recursive,
         JsArray<ObjectExplorerInspectionResult> children)
   /*-{
      return {
         "name"      : name,
         "type"      : type,
         "class"     : [clazz],
         "desc"      : desc,
         "size"      : size,
         "length"    : length,
         "depth"     : depth,
         "recursive" : recursive,
         "children"  : children
      };
   }-*/;
   
   public final native String        getObjectName()        /*-{ return this["name"];      }-*/;
   public final native String        getObjectType()        /*-{ return this["type"];      }-*/;
   public final native JsArrayString getObjectClass()       /*-{ return this["class"];     }-*/;
   public final native String        getObjectDescription() /*-{ return this["desc"];      }-*/;
   public final native int           getObjectSize()        /*-{ return this["size"];      }-*/;
   public final native int           getObjectLength()      /*-{ return this["length"];    }-*/;
   public final native int           getObjectDepth()       /*-{ return this["depth"];     }-*/;
   public final native boolean       isRecursive()          /*-{ return this["recursive"]; }-*/;
   
   public final native JsArray<ObjectExplorerInspectionResult> getChildren()
   /*-{
      return this["children"];
   }-*/;
}
