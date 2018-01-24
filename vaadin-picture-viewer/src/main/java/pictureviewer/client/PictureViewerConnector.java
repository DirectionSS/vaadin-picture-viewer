package pictureviewer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;

import pictureviewer.PictureViewer;

@Connect(PictureViewer.class)
public class PictureViewerConnector extends AbstractComponentConnector {

    String transitionState = "";
    boolean goForReverse = false;

    PictureViewerServerRpc rpc = RpcProxy.create(PictureViewerServerRpc.class, this);

    public PictureViewerConnector() {
        registerRpc(PictureViewerClientRpc.class, new PictureViewerClientRpc() {
            @Override
            public void alert(final String message) {

                Window.alert(message);
            }
        });

        getWidget().addMouseWheelHandler(new MouseWheelHandler() {

            @Override
            public void onMouseWheel(final MouseWheelEvent event) {

                final MouseEventDetails mouseDetails = MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement());
                mouseDetails.setAltKey(event.isNorth());
                PictureViewerConnector.this.rpc.wheeled(mouseDetails);
            }
        });

        getWidget().addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(final DragStartEvent event) {

                event.getDataTransfer()
                    .setDragImage(getWidget().getCursor_image()
                        .getElement(), 0, 0);
                event.getNativeEvent()
                    .getDataTransfer()
                    .setDragImage(getWidget().getCursor_image()
                        .getElement(), 0, 0);
                getWidget().getElement()
                    .getStyle()
                    .setProperty("cursor", "url(" + getWidget().getCursor_image()
                        .getUrl() + ")");
            }
        });

        getWidget().addDropHandler(new DropHandler() {

            @Override
            public void onDrop(final DropEvent event) {

                event.preventDefault();
                event.stopPropagation();
            }
        });

        getWidget().addDragOverHandler(new DragOverHandler() {

            @Override
            public void onDragOver(final DragOverEvent event) {

                event.getNativeEvent()
                    .getDataTransfer()
                    .clearData();

                final MouseEventDetails mouseDetails = MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement());
                if (mouseDetails.getRelativeX() > getWidget().getScrollContainer()
                    .getOffsetWidth()) {
                    if (getWidget().getScrollContainer()
                        .getHorizontalScrollPosition() < getWidget().getScrollContainer()
                            .getMaximumHorizontalScrollPosition()) {
                        getWidget().getScrollContainer()
                            .setHorizontalScrollPosition(getWidget().getScrollContainer()
                                .getHorizontalScrollPosition() + 1);
                    }
                }
                else if (mouseDetails.getRelativeY() > getWidget().getScrollContainer()
                    .getOffsetHeight()) {
                    if (getWidget().getScrollContainer()
                        .getVerticalScrollPosition() < getWidget().getScrollContainer()
                            .getMaximumVerticalScrollPosition()) {
                        getWidget().getScrollContainer()
                            .setVerticalScrollPosition(getWidget().getScrollContainer()
                                .getVerticalScrollPosition() + 1);
                    }
                }
                else if (getWidget().getImage()
                    .getOffsetWidth() >= (getWidget().getScrollContainer()
                        .getOffsetWidth())) {
                    final Double hscale = Math.ceil(getWidget().getImage()
                        .getOffsetWidth()
                            / getWidget().getScrollContainer()
                                .getOffsetWidth());

                    final Double vscale = Math.ceil(getWidget().getImage()
                        .getOffsetHeight()
                            / getWidget().getScrollContainer()
                                .getOffsetHeight());
                    if (getState().left == 1 || getState().left == 3 || getState().right == 1 || getState().right == 3) {
                        getWidget().getScrollContainer()
                            .setVerticalScrollPosition(mouseDetails.getRelativeX() * vscale.intValue());
                        getWidget().getScrollContainer()
                            .setHorizontalScrollPosition(mouseDetails.getRelativeY() * hscale.intValue());
                    }
                    else {
                        getWidget().getScrollContainer()
                            .setHorizontalScrollPosition(mouseDetails.getRelativeX() * hscale.intValue());
                        getWidget().getScrollContainer()
                            .setVerticalScrollPosition(mouseDetails.getRelativeY() * vscale.intValue());
                    }
                }
                else {
                    getWidget().getScrollContainer()
                        .setHorizontalScrollPosition(mouseDetails.getRelativeX());
                    getWidget().getScrollContainer()
                        .setVerticalScrollPosition(mouseDetails.getRelativeY());
                }
            }
        });

    }

    @Override
    protected Widget createWidget() {
        return GWT.create(PictureViewerWidget.class);
    }

    @Override
    public PictureViewerWidget getWidget() {
        return (PictureViewerWidget) super.getWidget();
    }

    @Override
    public PictureViewerState getState() {
        return (PictureViewerState) super.getState();
    }

    @Override
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        /* Pass image URL to widget */
        getWidget().getImage()
            .setUrl(getResourceUrl("image"));

        /* Pass Cursor_Image URL to widget if specified */
        if (getState().is_cursor_image) {
            getWidget().getCursor_image()
                .setUrl(getResourceUrl("cursor_image"));
            getWidget().getCursor_image()
                .getElement()
                .getStyle()
                .setWidth(getState().defaultImageWidth, Unit.PX);
            getWidget().getCursor_image()
                .getElement()
                .getStyle()
                .setHeight(getState().defaultImageHeight, Unit.PX);
        }

        /* Pass the dimensions of the confining area to the Widget */
        getWidget().getScrollContainer()
            .getElement()
            .getStyle()
            .setWidth(getState().areaWidth, Unit.PX);
        getWidget().getScrollContainer()
            .getElement()
            .getStyle()
            .setHeight(getState().areaHeight, Unit.PX);

        /* Pass the dimensions of the Image area to the Widget */
        getWidget().getImage()
            .getElement()
            .getStyle()
            .setWidth(getState().currentImageWidth, Unit.PCT);
        getWidget().getImage()
            .getElement()
            .getStyle()
            .setHeight(getState().currentImageHeight, Unit.PCT);

        System.out.println("1" + stateChangeEvent.hasPropertyChanged("left"));
        System.out.println("2" + stateChangeEvent.hasPropertyChanged("right"));

        // Pass the Rotation Event to the Widget
        if (stateChangeEvent.hasPropertyChanged("left")) {

            System.out.println("" + stateChangeEvent.hasPropertyChanged("left"));
            if (getState().changeRotation.equalsIgnoreCase("left")) {
                decisionMaker("left", false);
            }
            else if (getState().changeRotation.equalsIgnoreCase("")) {
                decisionMaker("left", false);
            }
            else {
                decisionMaker("left", true);
            }
        }
        if (stateChangeEvent.hasPropertyChanged("right")) {
            if (getState().changeRotation.equalsIgnoreCase("right")) {
                decisionMaker("right", false);
            }
            else if (getState().changeRotation.equalsIgnoreCase("")) {
                decisionMaker("right", false);
            }
            else {
                decisionMaker("right", true);
            }
        }
    }

    // Rotation Logic
    public void decisionMaker(final String changed, boolean isChanged) {
        getState().changeRotation = changed;

        if (changed.equalsIgnoreCase("right")) {
            switch (getState().left) {
                case 0:
                    if (getState().right == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 1) {
                        isChanged = false;
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    break;
                case 1:
                    if (getState().right == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    break;

                case 2:
                    if (getState().right == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    else if (getState().right == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    break;

                case 3:
                    if (getState().right == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-clock", changed));
                    }
                    else if (getState().right == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-clock", changed));
                    }
                    else if (getState().right == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-clock", changed));
                    }
                    else if (getState().right == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-clock", changed));
                    }
                    break;
            }
        }
        else if (changed.equalsIgnoreCase("left")) {
            switch (getState().right) {
                case 0:
                    if (getState().left == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        isChanged = false;
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    break;
                case 1:
                    if (getState().left == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    break;

                case 2:
                    if (getState().left == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    break;

                case 3:
                    if (getState().left == 0) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-left-anticlock", changed));
                    }
                    else if (getState().left == 1) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-top-anticlock", changed));
                    }
                    else if (getState().left == 2) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-right-anticlock", changed));
                    }
                    else if (getState().left == 3) {
                        getWidget().getElement()
                            .addClassName(getCorrectStyle(isChanged, "rotate-none-anticlock", changed));
                    }
                    break;
            }
        }

        System.out.println("Applied Style : " + getWidget().getStyleName());
    }

    public void removeRotateStyles() {
        getWidget().getElement()
            .removeClassName("rotate-none-clock");
        getWidget().getElement()
            .removeClassName("rotate-none-anticlock");
        getWidget().getElement()
            .removeClassName("rotate-right-anticlock");
        getWidget().getElement()
            .removeClassName("rotate-top-anticlock");
        getWidget().getElement()
            .removeClassName("rotate-left-anticlock");
        getWidget().getElement()
            .removeClassName("rotate-right-clock");
        getWidget().getElement()
            .removeClassName("rotate-top-clock");
        getWidget().getElement()
            .removeClassName("rotate-left-clock");
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
