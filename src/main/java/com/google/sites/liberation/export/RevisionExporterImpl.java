/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sites.liberation.export;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.sites.liberation.util.EntryType.getType;

import com.google.gdata.data.sites.BasePageEntry;
import com.google.inject.Inject;
import com.google.sites.liberation.renderers.RevisionRenderer;
import com.google.sites.liberation.renderers.TitleRenderer;
import com.google.sites.liberation.util.XmlElement;

import java.io.IOException;

/**
 * Exports a single revision of a page.
 * 
 * @author bsimon@google.com (Benjamin Simon)
 */
final class RevisionExporterImpl implements RevisionExporter {
  
  private RevisionRenderer revisionRenderer;
  private TitleRenderer titleRenderer;
  
  @Inject
  RevisionExporterImpl(RevisionRenderer revisionRenderer,
      TitleRenderer titleRenderer) {
    this.revisionRenderer = checkNotNull(revisionRenderer);
    this.titleRenderer = checkNotNull(titleRenderer);
  }
  
  @Override
  public void exportRevision(BasePageEntry<?> revision, Appendable out) 
        throws IOException {
    XmlElement html = new XmlElement("html");
    XmlElement head = new XmlElement("head");
    XmlElement title = new XmlElement("title");
    title.addText(revision.getTitle().getPlainText() + " (Version " 
        + revision.getRevision().getValue() + ")");
    html.addElement(head.addElement(title));
    XmlElement body = new XmlElement("body");
    XmlElement mainDiv = new XmlElement("div");
    mainDiv.setAttribute("class", "hentry " + getType(revision).toString());
    mainDiv.setAttribute("id", revision.getId());
    mainDiv.addElement(titleRenderer.renderTitle(revision));
    mainDiv.addElement(revisionRenderer.renderRevision(revision));
    html.addElement(body.addElement(mainDiv));
    html.appendTo(out);
  }
}