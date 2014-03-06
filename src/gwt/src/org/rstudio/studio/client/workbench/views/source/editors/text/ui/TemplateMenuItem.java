/*
 * TemplateMenuItem.java
 *
 * Copyright (C) 2009-14 by RStudio, Inc.
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
package org.rstudio.studio.client.workbench.views.source.editors.text.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

public class TemplateMenuItem extends Composite
{
   public TemplateMenuItem(String templateName)
   {
      super();
      wrapper_ = new FlowPanel();
      wrapper_.add(new InlineLabel(templateName));
      initWidget(wrapper_);
   }
   
   public void addIcon(ImageResource icon)
   {
      Image iconImage = new Image(icon);
      wrapper_.insert(iconImage, 0);
      Style imageStyle = iconImage.getElement().getStyle();
      imageStyle.setVerticalAlign(VerticalAlign.MIDDLE);
      imageStyle.setMarginRight(3, Unit.PX);
   }
   
   private FlowPanel wrapper_;
}
