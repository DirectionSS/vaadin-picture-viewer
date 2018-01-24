package pictureviewer.client;

import com.vaadin.shared.communication.ClientRpc;

public interface PictureViewerClientRpc extends ClientRpc {

    /**
     * Performs action on Client Side : Not Used
     * 
     * @param message
     */
    public void alert(String message);

}