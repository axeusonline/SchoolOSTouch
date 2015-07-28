package com.ies.schoolos;

import javax.servlet.http.Cookie;

import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SchoolSchema;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.UserSchema;
import com.ies.schoolos.ui.mobile.LoginView;
import com.ies.schoolos.ui.mobile.SchoolOSView;
import com.vaadin.addon.touchkit.extensions.OfflineMode;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("schoolostouch")
public class SchoolosTouchUI extends UI {

	private Container container = new Container();
	private SQLContainer schoolContainer = container.getSchoolContainer();
	private SQLContainer userContainer = container.getUserContainer();
	
	@Override
	protected void init(VaadinRequest request) {
		GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker("UA-63545885-1","schoolosplus.com");
		tracker.extend(this);
		tracker.trackPageview("/samplecode/googleanalytics");
		
		OfflineMode offlineMode = new OfflineMode();
		offlineMode.extend(this);
		offlineMode.setPersistentSessionCookie(true);
		offlineMode.setOfflineModeTimeout(15);
		
		getUrlParameter();
		autoLogin();
	}

	/*ค้นหาหน้าของโรงเรียนด้วย url เพื่อใช้ในการสมัครเรียนโดยไม่ต้อง Login */
	private void getUrlParameter(){				
		String path = Page.getCurrent().getLocation().getPath();
		path = path.substring(path.lastIndexOf("/")+1);
		if(!path.equals("")){
			schoolContainer.addContainerFilter(new Equal(SchoolSchema.SHORT_URL,path));
			if(schoolContainer.size() > 0){
				Item item = schoolContainer.getItem(schoolContainer.getIdByIndex(0));
				SessionSchema.setSchoolId(Integer.parseInt(item.getItemProperty(SchoolSchema.SCHOOL_ID).getValue().toString()));
				SessionSchema.setEmail(item.getItemProperty(SchoolSchema.CONTACT_EMAIL).getValue().toString());
			}
			//ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้
			schoolContainer.removeAllContainerFilters();
		}else if(SessionSchema.getUserID() == null){
			SessionSchema.setSchoolId(null);
			SessionSchema.setEmail(null);
		}
	}
	
	/*Login อัตโนมัติจาก Cookie */
	private void autoLogin(){	
		Cookie email = getCookieByName(SessionSchema.EMAIL);
		Cookie password = getCookieByName(SessionSchema.PASSWORD);

		if(email == null && password == null){
			if(SessionSchema.getUserID() != null){
				setContent(new SchoolOSView());				
			}else{
				setContent(new LoginView());
			}
		}else{
			userContainer.addContainerFilter(new And(
					new Equal(UserSchema.EMAIL,email.getValue()),
					new Equal(UserSchema.PASSWORD,password.getValue())));

			if(userContainer.size() != 0){
				Item item = userContainer.getItem(userContainer.getIdByIndex(0));
				Item schoolItem = schoolContainer.getItem(new RowId(item.getItemProperty(UserSchema.SCHOOL_ID).getValue()));
				SessionSchema.setSession(
						Integer.parseInt(item.getItemProperty(UserSchema.SCHOOL_ID).getValue().toString()),
						Integer.parseInt(item.getItemProperty(UserSchema.USER_ID).getValue().toString()),
						Integer.parseInt(item.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString()),
						Integer.parseInt(item.getItemProperty(UserSchema.REF_USER_ID).getValue().toString()),
						schoolItem.getItemProperty(SchoolSchema.NAME).getValue(),
						item.getItemProperty(UserSchema.FIRSTNAME).getValue(),
						item.getItemProperty(UserSchema.LASTNAME).getValue(),
						schoolItem.getItemProperty(SchoolSchema.CONTACT_EMAIL).getValue());
				setContent(new SchoolOSView());
			}else{
				setContent(new LoginView());
			}
			schoolContainer.removeAllContainerFilters();
		}
	}
	
	private Cookie getCookieByName(String name){
		Cookie cookie = null;
		if(VaadinService.getCurrentRequest().getCookies() != null){
			for(Cookie object:VaadinService.getCurrentRequest().getCookies()){
				 if(object.getName().equals(name)) {
					 cookie = object;  
			    }
			}
		}
		return cookie;
	}
}