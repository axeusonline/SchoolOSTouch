package com.ies.schoolos;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.annotations.VaadinServletConfiguration;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = SchoolosTouchUI.class, widgetset = "com.ies.schoolos.widgetset.SchoolostouchWidgetset")
public class Servlet extends TouchKitServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        TouchKitSettings s = getTouchKitSettings();

        String contextPath = getServletConfig().getServletContext().getContextPath();
        s.getApplicationIcons().addApplicationIcon(contextPath + "/VAADIN/themes/schoolosmobile/icon.png");
        s.getApplicationCacheSettings().setCacheManifestEnabled(true);

    }
}
