/* H.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Dec 18, 2007 12:10:40 PM     2007, Created by Dennis.Chen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under Lesser GPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zss.ui.au.in;

import static org.zkoss.zss.ui.au.in.Commands.parseKeys;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.HeaderEvent;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zss.ui.impl.Utils;

/**
 * A Command (client to server) for handling user(client) start editing a cell
 * @author Dennis.Chen
 *
 */
public class HeaderMouseCommand implements Command {
	
	public void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final Map data = (Map) request.getData();
		if (data == null || data.size() != 9)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {Objects.toString(data), this});

		String sheetId = (String) data.get("sheetId");
		Worksheet sheet = ((Spreadsheet)comp).getSelectedSheet();
		if(!Utils.getSheetUuid(sheet).equals(sheetId)) {
			return;
		}
		String type = (String) data.get("type");//x offset against spreadsheet
		int shx = (Integer) data.get("shx");//x offset against spreadsheet
		int shy = (Integer) data.get("shy");
		int key = parseKeys((String) data.get("key"));
		int row = (Integer) data.get("row");
		int col = (Integer) data.get("col");
		int mx = (Integer) data.get("mx");//x offset against body
		int my = (Integer) data.get("my");
		
		
		if ("lc".equals(type)) {
			type = org.zkoss.zss.ui.event.Events.ON_HEADER_CLICK;
		} else if ("rc".equals(type)) {
			type = org.zkoss.zss.ui.event.Events.ON_HEADER_RIGHT_CLICK;
		} else if ("dbc".equals(type)) {
			type = org.zkoss.zss.ui.event.Events.ON_HEADER_DOUBLE_CLICK;
		} else {
			throw new UiException("unknow type : " + type);
		}
		
		int htype = (row == -1) ? HeaderEvent.TOP_HEADER : HeaderEvent.LEFT_HEADER;
		int index = (row == -1) ? col : row;
				
		Events.postEvent(new HeaderMouseEvent(type, comp, shx, shy, key, sheet, htype, index, mx, my));
	}
}