/*
 * ObjectExplorerDataGrid.java
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
package org.rstudio.studio.client.workbench.views.source.editors.explorer.view;

import java.util.ArrayList;
import java.util.List;



/*
 * This widget provides a tabular, drill-down view into an R object.
 *
 * ## Columns
 *
 * ### Name
 *
 * The name column contains three elements:
 *
 *    1) An (optional) 'drill-down' icon, that expands the node such that
 *       children of that object are shown and added to the table,
 *
 *    2) An icon, denoting the object's type (list, environment, etc.)
 *
 *    3) The object's name; that is, the binding through which is can be accessed
 *       from the parent object.
 *
 * ### Type
 *
 * A text column, giving a short description of the object's type. Typically, this
 * will be the object's class, alongside the object's length (if relevant).
 *
 * ### Value
 *
 * A short, succinct description of the value of the object within.
 *
 * ### Size
 *
 * The amount of memory occupied by this object.
 *
 * ### Inspect
 *
 * A set of one or more widgets, used to e.g. open data.frames in the Data Viewer,
 * to find functions, and so on.
 */

import org.rstudio.studio.client.workbench.views.source.editors.explorer.model.ObjectExplorerHandle;
import org.rstudio.studio.client.workbench.views.source.editors.explorer.model.ObjectExplorerInspectionResult;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.view.client.ListDataProvider;

public class ObjectExplorerDataGrid extends DataGrid<ObjectExplorerInspectionResult>
{
   private static class NameCell
         extends AbstractCell<ObjectExplorerInspectionResult>
   {
      @Override
      public void render(Context context,
                         ObjectExplorerInspectionResult result,
                         SafeHtmlBuilder builder)
      {
         builder.appendHtmlConstant("<table>");
         builder.appendHtmlConstant("<tr>");
         
         builder.appendHtmlConstant("<td>");
         addIndent(builder, result);
         builder.appendHtmlConstant("</td>");
         
         builder.appendHtmlConstant("<td width=20");
         addExpandIcon(builder, result);
         builder.appendHtmlConstant("</td>");
         
         builder.appendHtmlConstant("<td width=20");
         addIcon(builder, result);
         builder.appendHtmlConstant("</td>");
         
         builder.appendHtmlConstant("<td>");
         addName(builder, result);
         builder.appendHtmlConstant("</td>");
         
         builder.appendHtmlConstant("</tr>");
         builder.appendHtmlConstant("</table>");
      }
      
      private static final void addIndent(SafeHtmlBuilder builder,
                                          ObjectExplorerInspectionResult result)
      {
         int indentPx = result.getObjectDepth() * 4;
         if (indentPx == 0)
            return;
         
         String html = "<div width=" + indentPx + "px></div>";
         builder.appendHtmlConstant(html);
      }
      
      private static final void addExpandIcon(SafeHtmlBuilder builder,
                                              ObjectExplorerInspectionResult result)
      {
         builder.appendHtmlConstant("<div>Expand Icon</div>");
      }
      
      private static final void addIcon(SafeHtmlBuilder builder,
                                        ObjectExplorerInspectionResult result)
      {
         builder.appendHtmlConstant("<div>Icon</div>");
      }
      
      private static final void addName(SafeHtmlBuilder builder,
                                        ObjectExplorerInspectionResult result)
      {
         builder.appendEscaped(result.getObjectName());
      }
   }
   
   public ObjectExplorerDataGrid(ObjectExplorerHandle handle)
   {
      super();
      handle_ = handle;
      
      setSize("100%", "100%");
      
      // add columns
      nameColumn_ = new Column<ObjectExplorerInspectionResult, ObjectExplorerInspectionResult>(new NameCell())
      {
         @Override
         public ObjectExplorerInspectionResult getValue(ObjectExplorerInspectionResult result)
         {
            return result;
         }
      };
      addColumn(nameColumn_, new TextHeader("Name"));
      
      typeColumn_ = new TextColumn<ObjectExplorerInspectionResult>()
      {
         @Override
         public String getValue(ObjectExplorerInspectionResult result)
         {
            return result.getObjectType();
         }
      };
      addColumn(typeColumn_, new TextHeader("Type"));
      setColumnWidth(typeColumn_, "20%");
      
      valueColumn_ = new TextColumn<ObjectExplorerInspectionResult>()
      {
         @Override
         public String getValue(ObjectExplorerInspectionResult result)
         {
            return result.getObjectDescription();
         }
      };
      addColumn(valueColumn_, new TextHeader("Value"));
      setColumnWidth(valueColumn_, "40%");
      
      sizeColumn_ = new TextColumn<ObjectExplorerInspectionResult>()
      {
         @Override
         public String getValue(ObjectExplorerInspectionResult result)
         {
            // TODO
            return String.valueOf(result.getObjectSize());
         }
      };
      addColumn(sizeColumn_, new TextHeader("Size"));
      setColumnWidth(sizeColumn_, "20%");
      
      // set updater
      dataProvider_ = new ListDataProvider<ObjectExplorerInspectionResult>();
      dataProvider_.addDataDisplay(this);
      
      // add some dummy data (for testing)
      List<ObjectExplorerInspectionResult> data = new ArrayList<ObjectExplorerInspectionResult>();
      data.add(ObjectExplorerInspectionResult.create(
            "Hello",
            "World",
            "World",
            "World",
            10,
            10,
            0,
            false,
            null));
      
      data.add(ObjectExplorerInspectionResult.create(
            "Hello",
            "World",
            "World",
            "World",
            10,
            10,
            0,
            false,
            null));
      data.add(ObjectExplorerInspectionResult.create(
            "Hello",
            "World",
            "World",
            "World",
            10,
            10,
            0,
            true,
            null));
      data.add(ObjectExplorerInspectionResult.create(
            "Hello",
            "World",
            "World",
            "World",
            10,
            10,
            0,
            true,
            null));
      
      dataProvider_.setList(data);
      
   }
   
   private final Column<ObjectExplorerInspectionResult, ObjectExplorerInspectionResult> nameColumn_;
   private final TextColumn<ObjectExplorerInspectionResult> typeColumn_;
   private final TextColumn<ObjectExplorerInspectionResult> valueColumn_;
   private final TextColumn<ObjectExplorerInspectionResult> sizeColumn_;
   
   private final ListDataProvider<ObjectExplorerInspectionResult> dataProvider_;
   
   private final ObjectExplorerHandle handle_;
}
