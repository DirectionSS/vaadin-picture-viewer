/*
 *
 */
package pictureviewer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("imagemagnifieronmousewheeladdon")
public class PictureViewerUI extends UI {

    VerticalLayout layout = new VerticalLayout();

    /**
     * @return the layout
     */
    public VerticalLayout getLayout() {
        return this.layout;
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(final VerticalLayout layout) {
        this.layout = layout;
    }

    @Override
    protected void init(final VaadinRequest request) {
        this.layout.setMargin(true);
        this.layout.setStyleName("image-editor");

        setContent(this.layout);

        // Must be a ThemeResource
        final PictureViewer pictureViewer = new PictureViewer(new ThemeResource("img/noddy.jpg"));
        this.layout.addComponent(pictureViewer);
        this.layout.setComponentAlignment(pictureViewer, Alignment.MIDDLE_CENTER);

        final GridLayout controlPanel = getControlPanel(pictureViewer);
        this.layout.addComponent(controlPanel);
        this.layout.setComponentAlignment(controlPanel, Alignment.MIDDLE_CENTER);
    }

    @SuppressWarnings("deprecation")
    public GridLayout getControlPanel(final PictureViewer pictureViewer) {

        // Magnifier Incremental
        final TextField percentageText = new TextField();
        percentageText.setImmediate(true);
        percentageText.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // TODO Auto-generated method stub
                final Timer t = new Timer();
                t.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(3);
                        }
                        catch (final InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, 0);

                System.out.println("" + PictureViewerUI.isValidNumber(percentageText.getValue()) + percentageText.getValue());
                if (PictureViewerUI.isValidNumber(percentageText.getValue())) {
                    pictureViewer.setPercentage(Double.valueOf(String.valueOf(percentageText.getValue())));
                }
            }
        });
        percentageText.setValue(String.valueOf(pictureViewer.getPercentage()));

        final GridLayout toolbar = new GridLayout(5, 1);
        toolbar.setStyleName("image-editor-toolbar");
        toolbar.setSpacing(true);

        final ArrayList<Double> percentageList = new ArrayList<Double>();
        percentageList.add(Double.valueOf("100"));
        percentageList.add(Double.valueOf("200"));
        percentageList.add(Double.valueOf("300"));
        percentageList.add(Double.valueOf("400"));
        percentageList.add(Double.valueOf("500"));

        // Magnifier Combo Box
        final ComboBox comboBox = new ComboBox("Percentage", percentageList);

        comboBox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // TODO Auto-generated method stub
                pictureViewer.setPercentage(Double.valueOf(String.valueOf(comboBox.getValue())));
            }
        });

        // Magnifier Slider
        final Slider slide = new Slider("Magnify", 100, 500);
        slide.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // TODO Auto-generated method stub
                pictureViewer.setPercentage(slide.getValue());
                percentageText.setValue(String.valueOf(slide.getValue()));
            }
        });

        // Image Rotator
        final Image left = new Image(null, new ThemeResource("img/shape_rotate_anticlockwise.png"));
        left.addClickListener(new MouseEvents.ClickListener() {

            @Override
            public void click(final com.vaadin.event.MouseEvents.ClickEvent event) {
                // TODO Auto-generated method stub
                System.out.println("Left Clicked" + pictureViewer.getState().left);
                pictureViewer.rotateLeft();
            }
        });

        final Image right = new Image(null, new ThemeResource("img/shape_rotate_clockwise.png"));
        right.addClickListener(new MouseEvents.ClickListener() {

            @Override
            public void click(final com.vaadin.event.MouseEvents.ClickEvent event) {
                // TODO Auto-generated method stub
                System.out.println("Right Clicked" + pictureViewer.getState().right);
                pictureViewer.rotateRight();
            }
        });
        final HorizontalLayout rotateBtnLayout = new HorizontalLayout(left, right);
        rotateBtnLayout.setSpacing(true);

        final Image magnify = new Image(null, new ThemeResource("img/magnifier_zoom_in.png"));
        magnify.addClickListener(new MouseEvents.ClickListener() {

            @Override
            public void click(final com.vaadin.event.MouseEvents.ClickEvent event) {
                // TODO Auto-generated method stub
                pictureViewer.increasePercentage();
                percentageText.setValue(String.valueOf(pictureViewer.getPercentage()));
            }
        });

        final Image shrink = new Image(null, new ThemeResource("img/magnifier_zoom_out.png"));
        shrink.addClickListener(new MouseEvents.ClickListener() {

            @Override
            public void click(final com.vaadin.event.MouseEvents.ClickEvent event) {
                // TODO Auto-generated method stub
                pictureViewer.decreasePercentage();
                percentageText.setValue(String.valueOf(pictureViewer.getPercentage()));
            }
        });
        final HorizontalLayout magnifyBtnLayout = new HorizontalLayout(magnify, percentageText, shrink);
        magnifyBtnLayout.setComponentAlignment(magnify, Alignment.BOTTOM_CENTER);
        magnifyBtnLayout.setComponentAlignment(percentageText, Alignment.BOTTOM_CENTER);
        magnifyBtnLayout.setComponentAlignment(shrink, Alignment.BOTTOM_CENTER);
        magnifyBtnLayout.setSpacing(true);

        // toolbar.addComponent(comboBox);
        // toolbar.setComponentAlignment(comboBox, Alignment.BOTTOM_CENTER);
        toolbar.addComponent(slide, 0, 0);
        toolbar.setComponentAlignment(slide, Alignment.BOTTOM_CENTER);
        toolbar.addComponent(rotateBtnLayout, 2, 0);
        toolbar.setComponentAlignment(rotateBtnLayout, Alignment.BOTTOM_CENTER);
        toolbar.addComponent(magnifyBtnLayout, 4, 0);
        toolbar.setComponentAlignment(magnifyBtnLayout, Alignment.BOTTOM_CENTER);

        return toolbar;
    }

    /**
     * Checks if is valid number.
     * 
     * @param value the value
     * @return true, if is valid number
     */
    public static boolean isValidNumber(final String value) {
        final boolean matchFound = value.matches("^[0-9].*[0-9]$");
        if (matchFound) {
            return true;
        }
        else {
            return false;
        }
    }

}