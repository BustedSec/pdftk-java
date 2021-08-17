/*
 * $Id: Document.java 4106 2009-11-27 12:59:39Z blowagie $
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

// pdftk-java iText base version 4.2.0
// pdftk-java modified yes (removed HeaderFooter [because of license issues?], removed ChapterAutoNumber [to ease migration])

package com.gitlab.pdftk_java.com.lowagie.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.gitlab.pdftk_java.com.lowagie.text.error_messages.MessageLocalization;

/**
 * A generic Document class.
 * <P>
 * All kinds of Text-elements can be added to a <CODE>HTMLDocument</CODE>.
 * The <CODE>Document</CODE> signals all the listeners when an element has
 * been added.
 * <P>
 * Remark:
 * <OL>
 *     <LI>Once a document is created you can add some meta information.
 *     <LI>You can also set the headers/footers.
 *     <LI>You have to open the document before you can write content.
 * <LI>You can only write content (no more meta-formation!) once a document is
 * opened.
 * <LI>When you change the header/footer on a certain page, this will be
 * effective starting on the next page.
 * <LI>After closing the document, every listener (as well as its <CODE>
 * OutputStream</CODE>) is closed too.
 * </OL>
 * Example: <BLOCKQUOTE>
 *
 * <PRE>// creation of the document with a certain size and certain margins
 * <STRONG>Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * </STRONG> try { 
 *   // creation of the different writers 
 *   HtmlWriter.getInstance(<STRONG>document </STRONG>, System.out);
 *   PdfWriter.getInstance(<STRONG>document </STRONG>, new FileOutputStream("text.pdf"));
 *   // we add some meta information to the document
 *   <STRONG>document.addAuthor("Bruno Lowagie"); </STRONG>
 *   <STRONG>document.addSubject("This is the result of a Test."); </STRONG>
 *   // we open the document for writing
 *   <STRONG>document.open(); </STRONG>
 *   <STRONG>document.add(new Paragraph("Hello world"));</STRONG>
 *  } catch(DocumentException de) {
 *   System.err.println(de.getMessage());
 *  }
 *  <STRONG>document.close();</STRONG>
 * </PRE>
 * 
 * </BLOCKQUOTE>
 */

public class Document implements DocListener {
    
    // membervariables
    /**
     * This constant may only be changed by Paulo Soares and/or Bruno Lowagie.
     * @since	2.1.6
     */
	private static final String ITEXT = "iText";
    /**
     * This constant may only be changed by Paulo Soares and/or Bruno Lowagie.
     * @since	2.1.6
     */
	private static final String RELEASE = "4.2.0";
	/** This constant may only be changed by Paulo Soares and/or Bruno Lowagie. */
	private static final String ITEXT_VERSION = "itext-paulo-155 (itextpdf.sf.net - lowagie.com)";
    
	/**
	 * Allows the pdf documents to be produced without compression for debugging
	 * purposes.
	 */
    public static boolean compress = true; 
    
	/**
	 * When true the file access is not done through a memory mapped file. Use it if the file
     * is too big to be mapped in your address space.
	 */
    public static boolean plainRandomAccess = false; 
 
    /** Scales the WMF font size. The default value is 0.86. */
    public static float wmfFontCorrection = 0.86f;
    
	/** The DocListener. */
    private ArrayList listeners = new ArrayList();
    
	/** Is the document open or not? */
    protected boolean open;
    
	/** Has the document already been closed? */
    protected boolean close;
    
    // membervariables concerning the layout
    
	/** The size of the page. */
    protected Rectangle pageSize;
    
	/** margin in x direction starting from the left */
    protected float marginLeft = 0;
    
	/** margin in x direction starting from the right */
    protected float marginRight = 0;
    
	/** margin in y direction starting from the top */
    protected float marginTop = 0;
    
	/** margin in y direction starting from the bottom */
    protected float marginBottom = 0;
    
    /** mirroring of the left/right margins */
    protected boolean marginMirroring = false;
    
    /**
     * mirroring of the top/bottom margins
     * @since	2.1.6
     */
    protected boolean marginMirroringTopBottom = false;
    
	/** Content of JavaScript onLoad function */
    protected String javaScript_onLoad = null;

	/** Content of JavaScript onUnLoad function */
    protected String javaScript_onUnLoad = null;

	/** Style class in HTML body tag */
    protected String htmlStyleClass = null;

    // headers, footers
    
	/** Current pagenumber */
    protected int pageN = 0;
    
	/** This is the textual part of a Page; it can contain a header */
    // protected HeaderFooter header = null; ssteward: dropped in 1.44
    
	/** This is the textual part of the footer */
    // protected HeaderFooter footer = null; ssteward: dropped in 1.44
    
    // constructor
    
	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 */
    
    public Document() {
        this(PageSize.A4);
    }
    
	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 *
	 * @param pageSize
	 *            the pageSize
 */
    
    public Document(Rectangle pageSize) {
        this(pageSize, 36, 36, 36, 36);
    }
    
	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 *
	 * @param pageSize
	 *            the pageSize
	 * @param marginLeft
	 *            the margin on the left
	 * @param marginRight
	 *            the margin on the right
	 * @param marginTop
	 *            the margin on the top
	 * @param marginBottom
	 *            the margin on the bottom
 */
    
	public Document(Rectangle pageSize, float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }
    
    // listener methods
    
	/**
 * Adds a <CODE>DocListener</CODE> to the <CODE>Document</CODE>.
 *
	 * @param listener
	 *            the new DocListener.
 */
    
    public void addDocListener(DocListener listener) {
        listeners.add(listener);
    }
    
	/**
 * Removes a <CODE>DocListener</CODE> from the <CODE>Document</CODE>.
 *
	 * @param listener
	 *            the DocListener that has to be removed.
 */
    
    public void removeDocListener(DocListener listener) {
        listeners.remove(listener);
    }
    
    // methods implementing the DocListener interface
    
	/**
	 * Adds an <CODE>Element</CODE> to the <CODE>Document</CODE>.
 *
	 * @param element
	 *            the <CODE>Element</CODE> to add
	 * @return <CODE>true</CODE> if the element was added, <CODE>false
	 *         </CODE> if not
	 * @throws DocumentException
	 *             when a document isn't open yet, or has been closed
 */
    
    public boolean add(Element element) throws DocumentException {
        if (close) {
			throw new DocumentException(MessageLocalization.getComposedMessage("the.document.has.been.closed.you.can.t.add.any.elements"));
        }
		if (!open && element.isContent()) {
			throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information"));
        }
        boolean success = false;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            success |= listener.add(element);
        }
		if (element instanceof LargeElement) {
			LargeElement e = (LargeElement)element;
			if (!e.isComplete())
				e.flushContent();
		}
        return success;
    }
    
	/**
 * Opens the document.
 * <P>
	 * Once the document is opened, you can't write any Header- or
	 * Meta-information anymore. You have to open the document before you can
	 * begin to add content to the body of the document.
 */
    
    public void open() {
		if (!close) {
            open = true;
        }
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageSize(pageSize);
			listener.setMargins(marginLeft, marginRight, marginTop,
					marginBottom);
            listener.open();
        }
    }
    
	/**
 * Sets the pagesize.
 *
	 * @param pageSize
	 *            the new pagesize
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageSize(pageSize);
        }
        return true;
    }
    
	/**
 * Sets the margins.
 *
	 * @param marginLeft
	 *            the margin on the left
	 * @param marginRight
	 *            the margin on the right
	 * @param marginTop
	 *            the margin on the top
	 * @param marginBottom
	 *            the margin on the bottom
 * @return	a <CODE>boolean</CODE>
 */
    
	public boolean setMargins(float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
			listener.setMargins(marginLeft, marginRight, marginTop,
					marginBottom);
        }
        return true;
    }
    
	/**
 * Signals that an new page has to be started.
 *
	 * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE>
	 *         if not.
 */
    
    public boolean newPage() throws DocumentException {
        if (!open || close) {
            return false;
        }
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.newPage();
        }
        return true;
    }
    
	/**
 * Changes the header of this document.
 *
	 * @param header
	 *            the new header
 */
    /* ssteward: dropped in 1.44
    public void setHeader(HeaderFooter header) {
        this.header = header;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setHeader(header);
        }
    }
    */
    
	/**
 * Resets the header of this document.
 */
    /* ssteward: dropped in 1.44
    public void resetHeader() {
        this.header = null;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetHeader();
        }
    }
    */
    
	/**
 * Changes the footer of this document.
 *
	 * @param footer
	 *            the new footer
 */
    /* ssteward: dropped in 1.44
    public void setFooter(HeaderFooter footer) {
        this.footer = footer;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setFooter(footer);
        }
    }
    */
    
	/**
 * Resets the footer of this document.
 */
    /* ssteward: dropped in 1.44
    public void resetFooter() {
        this.footer = null;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetFooter();
        }
    }
    */
    
	/**
 * Sets the page number to 0.
 */
    
    public void resetPageCount() {
        pageN = 0;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.resetPageCount();
        }
    }
    
	/**
 * Sets the page number.
 *
	 * @param pageN
	 *            the new page number
 */
    
    public void setPageCount(int pageN) {
        this.pageN = pageN;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setPageCount(pageN);
        }
    }
    
	/**
 * Returns the current page number.
 *
 * @return the current page number
 */
    
    public int getPageNumber() {
        return this.pageN;
    }
    
	/**
 * Closes the document.
 * <P>
	 * Once all the content has been written in the body, you have to close the
	 * body. After that nothing can be written to the body anymore.
 */
    
    public void close() {
		if (!close) {
            open = false;
            close = true;
        }
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.close();
        }
    }
    
    // methods concerning the header or some meta information
    
	/**
 * Adds a user defined header to the document.
 *
	 * @param name
	 *            the name of the header
	 * @param content
	 *            the content of the header
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addHeader(String name, String content) {
        try {
            return add(new Header(name, content));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the title to a Document.
 *
	 * @param title
	 *            the title
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addTitle(String title) {
        try {
            return add(new Meta(Element.TITLE, title));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the subject to a Document.
 *
	 * @param subject
	 *            the subject
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addSubject(String subject) {
        try {
            return add(new Meta(Element.SUBJECT, subject));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the keywords to a Document.
 *
	 * @param keywords
	 *            adds the keywords to the document
 * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addKeywords(String keywords) {
        try {
            return add(new Meta(Element.KEYWORDS, keywords));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the author to a Document.
 *
	 * @param author
	 *            the name of the author
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addAuthor(String author) {
        try {
            return add(new Meta(Element.AUTHOR, author));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the creator to a Document.
 *
	 * @param creator
	 *            the name of the creator
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addCreator(String creator) {
        try {
            return add(new Meta(Element.CREATOR, creator));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the producer to a Document.
 *
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addProducer() {
        try {
            return add(new Meta(Element.PRODUCER, getVersion()));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
	/**
 * Adds the current date and time to a Document.
 *
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */
    
    public boolean addCreationDate() {
        try {
			/* bugfix by 'taqua' (Thomas) */
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy");
			return add(new Meta(Element.CREATIONDATE, sdf.format(new Date())));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    // methods to get the layout of the document.
    
	/**
 * Returns the left margin.
 *
 * @return	the left margin
 */
    
    public float leftMargin() {
        return marginLeft;
    }
    
	/**
 * Return the right margin.
 *
 * @return	the right margin
 */
    
    public float rightMargin() {
        return marginRight;
    }
    
	/**
 * Returns the top margin.
 *
 * @return	the top margin
 */
    
    public float topMargin() {
        return marginTop;
    }
    
	/**
 * Returns the bottom margin.
 *
 * @return	the bottom margin
 */
    
    public float bottomMargin() {
        return marginBottom;
    }
    
	/**
 * Returns the lower left x-coordinate.
 *
 * @return	the lower left x-coordinate
 */
    
    public float left() {
        return pageSize.getLeft(marginLeft);
    }
    
	/**
 * Returns the upper right x-coordinate.
 *
 * @return	the upper right x-coordinate
 */
    
    public float right() {
        return pageSize.getRight(marginRight);
    }
    
	/**
 * Returns the upper right y-coordinate.
 *
 * @return	the upper right y-coordinate
 */
    
    public float top() {
        return pageSize.getTop(marginTop);
    }
    
	/**
 * Returns the lower left y-coordinate.
 *
 * @return	the lower left y-coordinate
 */
    
    public float bottom() {
        return pageSize.getBottom(marginBottom);
    }
    
	/**
 * Returns the lower left x-coordinate considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the lower left x-coordinate
 */
    
    public float left(float margin) {
        return pageSize.getLeft(marginLeft + margin);
    }
    
	/**
 * Returns the upper right x-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the upper right x-coordinate
 */
    
    public float right(float margin) {
        return pageSize.getRight(marginRight + margin);
    }
    
	/**
 * Returns the upper right y-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the upper right y-coordinate
 */
    
    public float top(float margin) {
        return pageSize.getTop(marginTop + margin);
    }
    
	/**
 * Returns the lower left y-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the lower left y-coordinate
 */
    
    public float bottom(float margin) {
        return pageSize.getBottom(marginBottom + margin);
    }
    
	/**
 * Gets the pagesize.
	 * 
 * @return the page size
 */
    
	public Rectangle getPageSize() {
        return this.pageSize;
    }
    
	/**
	 * Checks if the document is open.
	 * 
     * @return <CODE>true</CODE> if the document is open
     */    
    public boolean isOpen() {
        return open;
    }
    
	/**
	 * Gets the product name.
	 * This method may only be changed by Paulo Soares and/or Bruno Lowagie.
     * @return the product name
     * @since	2.1.6
     */    
    public static final String getProduct() {
        return ITEXT;
    }
    
	/**
	 * Gets the release number.
	 * This method may only be changed by Paulo Soares and/or Bruno Lowagie.
     * @return the product name
     * @since	2.1.6
     */    
    public static final String getRelease() {
        return RELEASE;
    }
    
	/**
	 * Gets the iText version.
	 * This method may only be changed by Paulo Soares and/or Bruno Lowagie.
     * @return iText version
     */    
    public static final String getVersion() {
        return ITEXT_VERSION;
    }

	/**
 * Adds a JavaScript onLoad function to the HTML body tag
 *
	 * @param code
	 *            the JavaScript code to be executed on load of the HTML page
 */
    
    public void setJavaScript_onLoad(String code) {
        this.javaScript_onLoad = code;
    }

	/**
 * Gets the JavaScript onLoad command.
	 * 
 * @return the JavaScript onLoad command
 */

    public String getJavaScript_onLoad() {
        return this.javaScript_onLoad;
    }

	/**
 * Adds a JavaScript onUnLoad function to the HTML body tag
 *
	 * @param code
	 *            the JavaScript code to be executed on unload of the HTML page
 */
    
    public void setJavaScript_onUnLoad(String code) {
        this.javaScript_onUnLoad = code;
    }

	/**
 * Gets the JavaScript onUnLoad command.
	 * 
 * @return the JavaScript onUnLoad command
 */

    public String getJavaScript_onUnLoad() {
        return this.javaScript_onUnLoad;
    }

	/**
 * Adds a style class to the HTML body tag
 *
	 * @param htmlStyleClass
	 *            the style class for the HTML body tag
 */
    
    public void setHtmlStyleClass(String htmlStyleClass) {
        this.htmlStyleClass = htmlStyleClass;
    }

	/**
 * Gets the style class of the HTML body tag
 *
 * @return		the style class of the HTML body tag
 */
    
    public String getHtmlStyleClass() {
        return this.htmlStyleClass;
    }
    
    /**
     * Set the margin mirroring. It will mirror right/left margins for odd/even pages.
     * <p>
     * Note: it will not work with {@link Table}.
	 * 
	 * @param marginMirroring
	 *            <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     */    
    public boolean setMarginMirroring(boolean marginMirroring) {
        this.marginMirroring = marginMirroring;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setMarginMirroring(marginMirroring);
        }
        return true;
    }
    
    /**
     * Set the margin mirroring. It will mirror top/bottom margins for odd/even pages.
     * <p>
     * Note: it will not work with {@link Table}.
	 * 
	 * @param marginMirroringTopBottom
	 *            <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     * @since	2.1.6
     */    
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
        this.marginMirroringTopBottom = marginMirroringTopBottom;
        DocListener listener;
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            listener = (DocListener) iterator.next();
            listener.setMarginMirroringTopBottom(marginMirroringTopBottom);
        }
        return true;
    }
    
    /**
     * Gets the margin mirroring flag.
	 * 
     * @return the margin mirroring flag
     */    
    public boolean isMarginMirroring() {
        return marginMirroring;
    }
}
