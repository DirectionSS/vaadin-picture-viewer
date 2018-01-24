package pictureviewer.client;

public class PictureViewerState extends com.vaadin.shared.AbstractComponentState {

    /** Percentage of total width the image would magnify */
    public int currentImageWidth = 10;

    /** Percentage of total height the image would magnify */
    public int currentImageHeight = 10;

    /** Default value for total width the image */
    public int defaultImageWidth = 10;

    /** Default value for total height the image */
    public int defaultImageHeight = 10;

    /** Maximum Percentage of total width the image would magnify */
    public int maxImageHeight = 500;

    /** Maximum Percentage of total height the image would magnify */
    public int maxImageWidth = 500;

    /** Value specifying the area width of the component */
    public int areaWidth = 500;

    /** Value specifying the area height of the component */
    public int areaHeight = 600;

    /** Value specifying whether the cursor_image has been over ridden by user or not */
    public boolean is_cursor_image = false;

    /** Percentage of the image magnified */
    public Double currentPercentage = Double.valueOf("100");

    /** Maximum allowed Percentage of the image magnified */
    public Double maxPercentage = Double.valueOf("500");

    /** Minimum allowed Percentage of the image magnified */
    public Double minPercentage = Double.valueOf("50");

    /** Default Percentage of the image magnified */
    public Double defaultPercentage = Double.valueOf("100");

    /** Scale by which the image gets incremented or decremented upon plus/minus */
    public Double scale = Double.valueOf("10");

    /** Tracker to keep the rotation position track */
    public int left = 0;

    /** Tracker to keep the rotation position track */
    public int right = 0;

    /** Tracker to keep the rotation position track */
    public String changeRotation = "";

}