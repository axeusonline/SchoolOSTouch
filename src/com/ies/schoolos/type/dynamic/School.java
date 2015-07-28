package com.ies.schoolos.type.dynamic;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SchoolSchema;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class School extends IndexedContainer{

	private static final long serialVersionUID = 1L;

	private Container container = new Container();
	
	public School(int provinceId) {
		initContainer(provinceId);
	}
 
	@SuppressWarnings("unchecked")
	private void initContainer(int provinceId){			
		SQLContainer scontainer = container.getSchoolContainer();
		scontainer.addContainerFilter(new Equal(SchoolSchema.PROVINCE_ID, provinceId));
		addContainerProperty("name", String.class,null);
		for (int i = 0; i < scontainer.size(); i++) {
			Object itemId = scontainer.getIdByIndex(i);
			Item item = addItem(Integer.parseInt(itemId.toString()));
			item.getItemProperty("name").setValue(scontainer.getItem(itemId).getItemProperty(SchoolSchema.NAME).getValue());
		}
		

		//ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้
		scontainer.removeAllContainerFilters();
	}
}
