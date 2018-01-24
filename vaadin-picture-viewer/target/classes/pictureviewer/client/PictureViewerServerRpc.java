package pictureviewer.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface PictureViewerServerRpc extends ServerRpc {

    public void wheeled(MouseEventDetails mouseDetails);

}
