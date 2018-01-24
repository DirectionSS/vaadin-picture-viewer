package pictureviewer.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import pictureviewer.PictureViewer;

@Theme("demo")
@Title("MyComponent Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(
        value = "/*",
        asyncSupported = true)
    @VaadinServletConfiguration(
        productionMode = false,
        ui = DemoUI.class,
        widgetset = "pictureviewer.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(final VaadinRequest request) {
        
        


        // Initialize our new UI component
        final PictureViewer pictureViewer = new PictureViewer(new ThemeResource("images/noddy.jpg"));

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(pictureViewer);
        layout.setComponentAlignment(pictureViewer, Alignment.MIDDLE_CENTER);
        setContent(layout);

    }

}
