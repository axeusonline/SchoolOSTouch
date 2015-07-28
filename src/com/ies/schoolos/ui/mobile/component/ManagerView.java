package com.ies.schoolos.ui.mobile.component;

import javax.servlet.http.Cookie;

import org.vaadin.dialogs.ConfirmDialog;

import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.ui.mobile.LoginView;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ManagerView extends NavigationManager {
	private static final long serialVersionUID = 1L;

	public NavigationView navView;
	public Button menuButton;

	/* Toolbar */
	public Button searchButton;
	public Button addButton;
	public Button timetableButton;
	
	public Popover popover;
	public TextField searchField;
	public Button search;
	
	public ManagerView() {
		navView = new NavigationView("SchoolOS ");
		navView.setStyleName("main-layout");
		setCurrentComponent(navView);
		
		buildMainLayout();
		initToolbar();
		
		Responsive.makeResponsive(navView);
	}
		
	private void buildMainLayout(){
		menuButton = new Button(FontAwesome.BARS);
        menuButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
        	private static final long serialVersionUID = 1L;
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
            {
                final Popover popup = new Popover();
                popup.setWidth("100%");
                
                VerticalComponentGroup buttonGroup = new VerticalComponentGroup();
                popup.setContent(buttonGroup);
                popup.showRelativeTo(menuButton);
                
                /*Button personnelButton = new Button(Feature.getNameTh(Feature.PERSONNEL), FontAwesome.USERS);
                personnelButton.setWidth("100%");
                personnelButton.setStyleName("menu-button");
                personnelButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                      
                    }
                });
                buttonGroup.addComponent(personnelButton);
        		
                Button academicButton = new Button(Feature.getNameTh(Feature.ACADEMIC), FontAwesome.BOOK);
                academicButton.setWidth("100%");
                academicButton.setStyleName("menu-button");
                academicButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                      
                    }
                });
                buttonGroup.addComponent(academicButton);
                
                Button registrationButton = new Button(Feature.getNameTh(Feature.REGISTRATION), FontAwesome.PENCIL_SQUARE_O);
                registrationButton.setWidth("100%");
                registrationButton.setStyleName("menu-button");
                registrationButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                      
                    }
                });
                buttonGroup.addComponent(registrationButton);
                
                Button studentAffairs = new Button(Feature.getNameTh(Feature.STUDENT_AFFAIRS), FontAwesome.LEGAL);
                studentAffairs.setWidth("100%");
                studentAffairs.setStyleName("menu-button");
                studentAffairs.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                      
                    }
                });
                buttonGroup.addComponent(studentAffairs);
                
                Button admin = new Button(Feature.getNameTh(Feature.ADMIN), FontAwesome.DESKTOP);
                admin.setWidth("100%");
                admin.setStyleName("menu-button");
                admin.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                      
                    }
                });
                buttonGroup.addComponent(admin);*/
                
                Button admin = new Button("แบบสอบถาม", FontAwesome.BOOK);
                admin.setWidth("100%");
                admin.setStyleName("menu-button");
                admin.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                    	popup.close();
                    	Window window = new Window();
                    	window.setPosition(0, 0);
                    	window.setSizeFull();
                    	UI.getCurrent().addWindow(window);
                      
                    	BrowserFrame browser = new BrowserFrame("แบบสอบถาม", new ExternalResource("http://goo.gl/forms/WDegvlCIlR"));
                    	browser.setSizeFull();
                    	window.setContent(browser);
                    }
                });
                buttonGroup.addComponent(admin);
                
                Button signoutButton = new Button("ออกจากระบบ", FontAwesome.SIGN_OUT);
                signoutButton.setWidth("100%");
                signoutButton.setStyleName("menu-button");
                signoutButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                    private static final long serialVersionUID = 1L;
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
                    {
                    	 ConfirmDialog.show(UI.getCurrent(), "ออกจากระบบ", "คุณค้องการออกจากระบบใช่หรือไม่?", "ใช่", "ไม่ใช่", new org.vaadin.dialogs.ConfirmDialog.Listener() {

                             private static final long serialVersionUID = 1L;
                             public void onClose(ConfirmDialog dialog)
                             {
                                 if(dialog.isConfirmed())
                                 {
                                     logout();
                                     popup.close();
                                 }
                             }
                         });
                    }
                });
                buttonGroup.addComponent(signoutButton);                
            }
        });
        navView.setRightComponent(menuButton);
	}

	/* สร้าง Toolbar */
	private void initToolbar(){
		Toolbar toolbar = new Toolbar();
		navView.setToolbar(toolbar);   
         
        searchButton = new Button(FontAwesome.SEARCH);
        searchButton.setSizeFull();
        searchButton.setStyleName("toolbar-button");
        toolbar.addComponent(searchButton);    
        
        addButton = new Button(FontAwesome.PLUS);
        addButton.setSizeFull();
        addButton.setStyleName("toolbar-button-focus");
        toolbar.addComponent(addButton);
        
        timetableButton = new Button(FontAwesome.CALENDAR);
        timetableButton.setSizeFull();
        timetableButton.setStyleName("toolbar-button");
        timetableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				addButton.setStyleName("");
		        searchButton.setStyleName("");
		        timetableButton.setStyleName("toolbar-button-focus");
			}
		});
        toolbar.addComponent(timetableButton); 
        
        setToolbarListenner();
	}
	
	public void setToolbarListenner(){}
	
    /* ออกจากระบบ */
	private void logout(){
        Cookie emailCookie = new Cookie(SessionSchema.EMAIL, "");
        emailCookie.setMaxAge(0);
        emailCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(emailCookie);
        
        Cookie passwordCookie = new Cookie(SessionSchema.PASSWORD, "");
        passwordCookie.setMaxAge(0);
        passwordCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(passwordCookie);
        resetSession();
        
        UI ui = UI.getCurrent();
        ui.setContent(new LoginView());
    }

	/* ล้าง Session */
    private void resetSession()
    {
        UI.getCurrent().getSession().setAttribute(SessionSchema.SCHOOL_ID, null);
        UI.getCurrent().getSession().setAttribute(SessionSchema.SCHOOL_NAME, null);
        UI.getCurrent().getSession().setAttribute(SessionSchema.USER_ID, null);
        UI.getCurrent().getSession().setAttribute(SessionSchema.REF_ID, null);
        UI.getCurrent().getSession().setAttribute(SessionSchema.FIRSTNAME, null);
        UI.getCurrent().getSession().setAttribute(SessionSchema.EMAIL, null);
    }
}
