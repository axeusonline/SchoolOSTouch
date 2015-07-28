package com.ies.schoolos;


import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.vaadin.addon.touchkit.gwt.TouchKitBundleLoaderFactory;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.EmailFieldConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.GeolocatorConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.NumberFieldConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.SwitchConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.TabBarConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.VerticalComponentGroupConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationBarConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationButtonConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationManagerConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationViewConnector;
import com.vaadin.addon.touchkit.gwt.client.vcom.popover.PopoverConnector;
import com.vaadin.client.extensions.ResponsiveConnector;
import com.vaadin.client.extensions.javascriptmanager.JavaScriptManagerConnector;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.client.ui.combobox.ComboBoxConnector;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.client.ui.form.FormConnector;
import com.vaadin.client.ui.formlayout.FormLayoutConnector;
import com.vaadin.client.ui.image.ImageConnector;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.client.ui.link.LinkConnector;
import com.vaadin.client.ui.nativeselect.NativeSelectConnector;
import com.vaadin.client.ui.optiongroup.OptionGroupConnector;
import com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.client.ui.table.TableConnector;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.client.ui.upload.UploadConnector;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect.LoadStyle;

public class WidgetLoaderFactory extends TouchKitBundleLoaderFactory {
	
	private final ArrayList<String> usedConnectors;

    public WidgetLoaderFactory() {
    	usedConnectors = new ArrayList<String>();
    	usedConnectors.add(VerticalLayoutConnector.class.getName());
        usedConnectors.add(HorizontalLayoutConnector.class.getName());
        usedConnectors.add(PanelConnector.class.getName());
        usedConnectors.add(TableConnector.class.getName());
        usedConnectors.add(ButtonConnector.class.getName());
        usedConnectors.add(ComboBoxConnector.class.getName());
        usedConnectors.add(CssLayoutConnector.class.getName());
        usedConnectors.add(DatePickerConnector.class.getName());
        usedConnectors.add(EmailFieldConnector.class.getName());
        usedConnectors.add(ImageConnector.class.getName());
        usedConnectors.add(FormConnector.class.getName());
        usedConnectors.add(FormLayoutConnector.class.getName());
        usedConnectors.add(GeolocatorConnector.class.getName());
        usedConnectors.add(LabelConnector.class.getName());
        usedConnectors.add(LinkConnector.class.getName());
        usedConnectors.add(NativeSelectConnector.class.getName());
        usedConnectors.add(NavigationBarConnector.class.getName());
        usedConnectors.add(NavigationButtonConnector.class.getName());
        usedConnectors.add(NavigationManagerConnector.class.getName());
        usedConnectors.add(NavigationViewConnector.class.getName());
        usedConnectors.add(NumberFieldConnector.class.getName());
        usedConnectors.add(OptionGroupConnector.class.getName());
        usedConnectors.add(PopoverConnector.class.getName());
        usedConnectors.add(SwitchConnector.class.getName());
        usedConnectors.add(TabBarConnector.class.getName());
        usedConnectors.add(TableConnector.class.getName());
        usedConnectors.add(TextFieldConnector.class.getName());
        usedConnectors.add(UIConnector.class.getName());
        usedConnectors.add(UploadConnector.class.getName());
        usedConnectors.add(VerticalComponentGroupConnector.class.getName());
        usedConnectors.add(WindowConnector.class.getName());
        usedConnectors.add(ResponsiveConnector.class.getName());
        usedConnectors.add(JavaScriptManagerConnector.class.getName());
        

        //usedConnectors.add(GridLayoutConnector.class.getName());
        //usedConnectors.add(AbsoluteLayoutConnector.class.getName());
        //usedConnectors.add(HorizontalSplitPanelConnector.class.getName());
        //usedConnectors.add(VerticalSplitPanelConnector.class.getName());
        //usedConnectors.add(AccordionConnector.class.getName());
        //usedConnectors.add(ComboBoxConnector.class.getName());
        //usedConnectors.add(TabsheetConnector.class.getName());
        //usedConnectors.add(MenuBarConnector.class.getName());
        //usedConnectors.add(WindowConnector.class.getName());
        // usedConnectors.add(RichTextAreaConnector.class.getName());
        //usedConnectors.add(TwinColSelectConnector.class.getName());
        //usedConnectors.add(CustomLayoutConnector.class.getName());
        //usedConnectors.add(PopupViewConnector.class.getName());
        //usedConnectors.add(CalendarConnector.class.getName());
        //usedConnectors.add(TreeTableConnector.class.getName());
    }
    
    @Override
    protected Collection<JClassType> getConnectorsForWidgetset(TreeLogger logger, TypeOracle typeOracle)
            throws UnableToCompleteException {
        // The usedConnectors list should contain all the
        // connectors that we need in the app, so we
        // can leave all others away.

        // Get all connectors in the unoptimized widget set
        Collection<JClassType> connectorsForWidgetset = super
                .getConnectorsForWidgetset(logger, typeOracle);

        // Filter the connectors using the used list
        ArrayList<JClassType> arrayList = new ArrayList<JClassType>();
        for (JClassType jClassType : connectorsForWidgetset) {
            String qualifiedSourceName =
                jClassType.getQualifiedSourceName();
            if (usedConnectors.contains(qualifiedSourceName)) {
                arrayList.add(jClassType);
            }
        }
        return arrayList;
    }
    
    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
        return LoadStyle.EAGER;
    }
}
