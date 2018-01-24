package pictureviewer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * PictureViewerWidget is the client side widget of an add-on for Vaadin that allows magnifying of an image on Mouse Wheel Roll Event
 *
 * @author Kartik Suba
 *
 */
public class PictureViewerWidget extends FocusPanel {

    /** Style name */
    public static final String CLASSNAME = "imagemagnifieronwheel";

    /** Image to be Magnified */
    Image image = null;

    /** Cursor_Icon to be used for Magnified Image Traversal */
    Image cursor_image = null;

    /** Container for the image */
    ScrollPanel scrollContainer = null;

    /**
     * 
     */
    public PictureViewerWidget() {

        super();

        /* Create image container */
        this.scrollContainer = new ScrollPanel();
        this.scrollContainer.setStyleName(PictureViewerWidget.CLASSNAME);

        /* Create image to be magnified */
        this.image = new Image("");

        /* Create cursor_icon to be used for Magnified Image Traversal */
        this.cursor_image = new Image(GWT.getModuleBaseURL()
            .replace(GWT.getModuleBaseURL()
                .substring(GWT.getModuleBaseURL()
                    .indexOf("/VAADIN")), "/VAADIN/themes/imagemagnifieronmousewheeladdon/" + "img/my-cursor.png"));

        /* Setting of Styles */
        this.cursor_image.setStyleName("v-image-cursor");
        this.image.setStyleName("v-image-magnifier");

        /* Add widgets */
        this.scrollContainer.add(this.image);
        this.add(this.scrollContainer);
    }

    /**
     * Returns (an unmodifiable) copy of the ScrollPanel currently used as an image container for this Component.
     * 
     * @return ScrollPanel
     */
    public ScrollPanel getScrollContainer() {
        return this.scrollContainer;
    }

    /**
     * Sets the ScrollPanel to be used as an image container for this Component.
     * 
     * @param scrollContainer
     */
    public void setScrollContainer(final ScrollPanel scrollContainer) {
        this.scrollContainer = scrollContainer;
    }

    /**
     * Returns (an unmodifiable) copy of the image currently added to this Component.
     *
     * @return Image instance
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Sets the given image to be displayed in this PictureViewer.
     * 
     * @param image Image
     */
    public void setImage(final Image image) {
        this.image = image;
    }

    /**
     * Returns (an unmodifiable) copy of the image currently set as a Cursor_Icon to this Component.
     *
     * @return Image
     */
    public Image getCursor_image() {
        return this.cursor_image;
    }

    /**
     * Sets the given image to be displayed as a Cursor_Icon for Magnified Image Traversal in this PictureViewer.
     * 
     * @param cursor_image Image for Cursor_Icon
     */
    public void setCursor_image(final Image cursor_image) {
        this.cursor_image = cursor_image;
    }

}