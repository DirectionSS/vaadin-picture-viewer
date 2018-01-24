package pictureviewer;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails;

import pictureviewer.client.PictureViewerServerRpc;
import pictureviewer.client.PictureViewerState;

/**
 * PictureViewer is an add-on for Vaadin that allows magnifying of an image on Mouse Wheel Roll
 *
 * PictureViewer supports e.g.
 *
 * - Lazy-loading the images with the help of GWT Image Loader
 *
 * - Mouse wheel actions for image zooming
 *
 * - Specifying the limit on the zoom percentage
 *
 * - Black and normal Reindeer theme styles
 *
 * - Mouse roll over actions for capturing the details from the magnified image
 *
 * - Specifying the size of the area confining the image
 *
 * - Providing the images as Vaadin Resources
 *
 * @author Kartik Suba
 */

@SuppressWarnings("serial")
public class PictureViewer extends com.vaadin.ui.AbstractComponent {

    /** Resource (image) set to display in this component */
    Resource image = null;

    /** Resource (image) set to display as a Cursor for Magnified Image Traversal in this component */
    Resource cursor_image = null;

    String transitionState = "";
    boolean goForReverse = false;

    /** Server RPC instance */
    private final PictureViewerServerRpc rpc = new PictureViewerServerRpc() {

        @Override
        public void wheeled(final MouseEventDetails mouseDetails) {

            // Mouse Wheel Event Capturing: Condition determining the upward/downward roll of the wheel
            if (mouseDetails.isAltKey()) {
                if ((getState().currentImageWidth * 2) < getState().maxImageWidth) {
                    getState().currentImageWidth = getState().currentImageWidth * 2;
                }
                if ((getState().currentImageHeight * 2) < getState().maxImageHeight) {
                    getState().currentImageHeight = getState().currentImageHeight * 2;
                }
            }
            else {
                if ((getState().currentImageWidth / 2) > 0) {
                    getState().currentImageWidth = getState().currentImageWidth / 2;
                    getState().currentImageHeight = getState().currentImageHeight / 2;
                }
            }
        }
    };

    /**
     * Default constructor of PictureViewer.
     *
     * To set the images, use the setImage(Resource image) method.
     */
    public PictureViewer() {
        registerRpc(this.rpc);
    }

    /**
     * Convenience constructor that allows you to give the image at init.
     * 
     * @param imageRes Image as ThemeResource
     */
    public PictureViewer(final ThemeResource imageRes) {
        getState().currentImageWidth = getState().defaultImageWidth;
        getState().currentImageHeight = getState().defaultImageHeight;
        setImage(imageRes);
        registerRpc(this.rpc);
    }

    /**
     * Convenience constructor that allows you to give the image at init.
     * 
     * @param imageRes Image as ThemeResource
     */
    public PictureViewer(final ExternalResource imageRes) {
        getState().currentImageWidth = getState().defaultImageWidth;
        getState().currentImageHeight = getState().defaultImageHeight;
        setImage(imageRes);
        registerRpc(this.rpc);
    }

    /**
     * Convenience constructor that allows you to give the image at init and also specify the Cursor_Image used for Image Traversal
     * 
     * @param imageRes
     * @param cursorImageRes
     */
    public PictureViewer(final ThemeResource imageRes, final ThemeResource cursorImageRes) {
        getState().currentImageWidth = getState().defaultImageWidth;
        getState().currentImageHeight = getState().defaultImageHeight;
        setImage(imageRes);
        setCursor_image(cursorImageRes);
        registerRpc(this.rpc);
    }

    /**
     * Convenience constructor that allows you to give the image at init and also specify the area confining the image
     * 
     * @param imageRes
     * @param areaWidth
     * @param areaHeight
     */
    public PictureViewer(final ThemeResource imageRes, final int areaWidth, final int areaHeight) {
        getState().currentImageWidth = getState().defaultImageWidth;
        getState().currentImageHeight = getState().defaultImageHeight;
        setImage(imageRes);
        getState().areaWidth = areaWidth;
        getState().areaHeight = areaHeight;
        registerRpc(this.rpc);
    }

    /**
     * Convenience constructor that allows you to give the image and Cursor_Image at init and also specify the area confining the image
     * 
     * @param imageRes
     * @param cursorImageRes
     * @param areaWidth
     * @param areaHeight
     */
    public PictureViewer(final ThemeResource imageRes, final ThemeResource cursorImageRes, final int areaWidth, final int areaHeight) {
        getState().currentImageWidth = getState().defaultImageWidth;
        getState().currentImageHeight = getState().defaultImageHeight;
        setImage(imageRes);
        setCursor_image(cursorImageRes);
        getState().areaWidth = areaWidth;
        getState().areaHeight = areaHeight;
        registerRpc(this.rpc);
    }

    @Override
    public PictureViewerState getState() {
        return (PictureViewerState) super.getState();
    }

    /**
     * Returns (an unmodifiable) copy of the image currently set as a Cursor_Icon to this Component.
     *
     * @return Resource instance
     */
    public Resource getCursor_image() {
        return this.cursor_image;
    }

    /**
     * Sets the given image to be displayed as a Cursor_Icon for Magnified Image Traversal in this PictureViewer.
     * 
     * @param cursor_image Image Resource for Cursor_Icon
     */
    public void setCursor_image(final Resource cursor_image) {
        setResource("cursor_image", this.image);
        getState().is_cursor_image = true;
        this.cursor_image = cursor_image;
    }

    /**
     * Returns (an unmodifiable) copy of the image currently added to this Component.
     *
     * @return Resource instance
     */
    public Resource getImage() {
        return this.image;
    }

    /**
     * Sets the given image to be displayed in this PictureViewer.
     * 
     * @param image Image Resource
     */
    public void setImage(final Resource image) {
        setResource("image", image);
        this.image = image;
    }

    public void setPercentage(final Double percentage) {
        if (percentage > getState().minPercentage && percentage < getState().maxPercentage) {
            getState().currentPercentage = percentage;
            getState().currentImageWidth = percentage.intValue();
            getState().currentImageHeight = percentage.intValue();
        }
    }

    public Double getPercentage() {
        return getState().currentPercentage;
    }

    public void increasePercentage() {
        if (((getState().currentPercentage + Double.valueOf(getState().scale)) < getState().maxPercentage)) {
            getState().currentPercentage = getState().currentPercentage + Double.valueOf(getState().scale);
            getState().currentImageWidth = Double.valueOf(getState().currentPercentage + getState().scale)
                .intValue();
            getState().currentImageHeight = Double.valueOf(getState().currentPercentage + getState().scale)
                .intValue();
        }
    }

    public void decreasePercentage() {
        if (((getState().currentPercentage - Double.valueOf(getState().scale)) > getState().minPercentage)) {
            getState().currentPercentage = getState().currentPercentage - Double.valueOf(getState().scale);
            getState().currentImageWidth = Double.valueOf(getState().currentPercentage - getState().scale)
                .intValue();
            getState().currentImageHeight = Double.valueOf(getState().currentPercentage - getState().scale)
                .intValue();
        }
    }

    public void rotateLeft() {
        getState().left = getState().left + 1;
        if (getState().left == 4) {
            getState().left = 0;
        }

    }

    public void rotateRight() {
        getState().right = getState().right + 1;
        if (getState().right == 4) {
            getState().right = 0;
        }
    }

    // Rotation Logic
    public void decisionMaker(final String changed, boolean isChanged) {
        getState().changeRotation = changed;

        if (changed.equalsIgnoreCase("right")) {
            switch (getState().left) {
                case 0:
                    if (getState().right == 0) {

                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 1) {
                        isChanged = false;
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    break;
                case 1:
                    if (getState().right == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    break;

                case 2:
                    if (getState().right == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    break;

                case 3:
                    if (getState().right == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    break;
            }
        }
        else if (changed.equalsIgnoreCase("left")) {
            switch (getState().right) {
                case 0:
                    if (getState().left == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        isChanged = false;
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    break;
                case 1:
                    if (getState().left == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    break;

                case 2:
                    if (getState().left == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    break;

                case 3:
                    if (getState().left == 0) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        this.addStyleName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    break;
            }
        }

        System.out.println("Applied Style : " + this.getStyleName());
    }

    public void removeRotateStyles() {
        this.removeStyleName("rotate-none-clock");
        this.removeStyleName("rotate-none-anticlock");
        this.removeStyleName("rotate-right-anticlock");
        this.removeStyleName("rotate-top-anticlock");
        this.removeStyleName("rotate-left-anticlock");
        this.removeStyleName("rotate-right-clock");
        this.removeStyleName("rotate-top-clock");
        this.removeStyleName("rotate-left-clock");
    }

    public String getCorrectStyle(final boolean isChanged, final String styleName, final String changed) {
        removeRotateStyles();

        System.out.println("isChanged : :" + isChanged + " transitionState : " + this.transitionState + " changed : " + changed + " left : " + getState().left
                + " right :" + getState().right + " goForReverse" + this.goForReverse);

        if (this.transitionState.equalsIgnoreCase("")) {
            this.transitionState = changed;
            if (this.goForReverse) {
                this.goForReverse = false;
            }
        }
        else if (!this.transitionState.equalsIgnoreCase(changed) && isChanged && this.goForReverse) {
            this.transitionState = changed;
            this.goForReverse = false;
        }
        else if (!this.transitionState.equalsIgnoreCase(changed) && isChanged
                && !this.goForReverse /* && Math.abs(left-right)==2 && (left==0 || right ==0) */) {
            this.transitionState = changed;
            this.goForReverse = true;
        }

        System.out.println("isChanged : :" + isChanged + " transitionState : " + this.transitionState + " changed : " + changed + " left : " + getState().left
                + " right :" + getState().right + " goForReverse" + this.goForReverse);

        if (styleName.contains("none")) {
            this.transitionState = "";
            getState().changeRotation = "";
        }

        if (!this.goForReverse) {
            return styleName;
        }
        else {

            if (styleName.contains("left-anticlock")) {
                return "rotate-left-clock";
            }
            if (styleName.contains("right-anticlock")) {
                return "rotate-right-clock";
            }
            if (styleName.contains("top-anticlock")) {
                return "rotate-top-clock";
            }
            if (styleName.contains("none-anticlock")) {
                return "rotate-none-clock";
            }
            if (styleName.contains("left-clock")) {
                return "rotate-left-anticlock";
            }
            if (styleName.contains("right-clock")) {
                return "rotate-right-anticlock";
            }
            if (styleName.contains("top-clock")) {
                return "rotate-top-anticlock";
            }
            if (styleName.contains("none-clock")) {
                return "rotate-none-anticlock";
            }

        }
        return null;
    }

}
