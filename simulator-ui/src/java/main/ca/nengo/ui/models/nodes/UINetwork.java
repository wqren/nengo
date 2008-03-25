package ca.nengo.ui.models.nodes;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import ca.nengo.model.Network;
import ca.nengo.model.impl.NetworkImpl;
import ca.nengo.sim.Simulator;
import ca.nengo.ui.NengoGraphics;
import ca.nengo.ui.models.UINeoNode;
import ca.nengo.ui.models.icons.NetworkIcon;
import ca.nengo.ui.models.tooltips.TooltipBuilder;
import ca.nengo.ui.models.viewers.NetworkViewer;
import ca.nengo.ui.models.viewers.NetworkViewerConfig;
import ca.nengo.ui.models.viewers.NodeViewer;
import ca.nengo.util.VisiblyMutable;
import ca.nengo.util.VisiblyMutable.Event;
import ca.shu.ui.lib.util.UserMessages;
import ca.shu.ui.lib.world.WorldObject;

/**
 * UI Wrapper for a Network
 * 
 * @author Shu Wu
 */
public class UINetwork extends UINodeViewable {
	private static final String LAYOUT_MANAGER_KEY = "layout/manager";

	private static final long serialVersionUID = 1L;

	public static final String typeName = "Network";

	/**
	 * @param wo
	 *            WorldObject
	 * @return The closest parent Network to wo
	 */
	public static UINetwork getClosestNetwork(WorldObject wo) {
		if (wo == null) {
			return null;
		}

		if (wo instanceof UINetwork) {
			return (UINetwork) wo;
		} else if (wo instanceof NodeViewer) {
			return getClosestNetwork(((NodeViewer) wo).getViewerParent());
		} else if (wo instanceof UINeoNode) {
			return getClosestNetwork(((UINeoNode) wo).getNetworkParent());
		} else {
			return getClosestNetwork(wo.getParent());
		}

	}

	private MySimulatorListener mySimulatorListener;

	public UINetwork(Network model) {
		super(model);
		setIcon(new NetworkIcon(this));
	}

	private void simulatorUpdated() {
		if (getViewer() != null && !getViewer().isDestroyed()) {
			getViewer().updateSimulatorProbes();
		}
	}

	@Override
	protected void constructTooltips(TooltipBuilder tooltips) {
		super.constructTooltips(tooltips);
		tooltips.addProperty("# Projections", "" + getModel().getProjections().length);

		tooltips.addProperty("Simulator", "" + getSimulator().getClass().getSimpleName());
	}

	@Override
	protected void initialize() {
		mySimulatorListener = new MySimulatorListener();
		super.initialize();
	}

	@Override
	protected void modelUpdated() {
		super.modelUpdated();

		if (getViewer() != null && !getViewer().isDestroyed()) {
			getViewer().updateViewFromModel();
		}
	}

	@Override
	public void attachViewToModel() {
		super.attachViewToModel();
		getModel().getSimulator().addChangeListener(mySimulatorListener);
	}

	@Override
	public NetworkViewer createViewerInstance() {
		return new NetworkViewer(this);
	}

	@Override
	public void detachViewFromModel() {
		super.detachViewFromModel();

		getModel().getSimulator().removeChangeListener(mySimulatorListener);
	}

	@Override
	public String getFileName() {
		return getSavedConfig().getFileName();
	}

	@Override
	public NetworkImpl getModel() {
		return (NetworkImpl) super.getModel();
	}

	@Override
	public String getName() {
		if (getModel() == null) {
			return super.getName();
		} else {
			return getModel().getName();
		}
	}

	@Override
	public int getNodesCount() {
		if (getModel() != null)
			return getModel().getNodes().length;
		else
			return 0;
	}

	/**
	 * @return UI Configuration manager associated with this network
	 */
	public NetworkViewerConfig getSavedConfig() {
		NetworkViewerConfig layoutManager = null;
		try {
			Object obj = getModel().getMetaData(LAYOUT_MANAGER_KEY);

			if (obj != null)
				layoutManager = (NetworkViewerConfig) obj;
		} catch (Throwable e) {
			UserMessages.showError("Could not access layout manager, creating a new one");
		}

		if (layoutManager == null) {
			layoutManager = new NetworkViewerConfig(getName() + "."
					+ NengoGraphics.NEONODE_FILE_EXTENSION);
			setUICOnfig(layoutManager);
		}

		return layoutManager;
	}

	/**
	 * @return Simulator
	 */
	public Simulator getSimulator() {
		return getModel().getSimulator();
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public NetworkViewer getViewer() {
		return (NetworkViewer) super.getViewer();
	}

	@Override
	public void saveContainerConfig() {

		if (getViewer() != null) {
			getViewer().saveLayoutAsDefault();
		}

	}

	@Override
	public void saveModel(File file) throws IOException {
		getSavedConfig().setFileName(file.toString());
		super.saveModel(file);
	}

	/**
	 * @param config
	 *            UI Configuration manager
	 */
	public void setUICOnfig(NetworkViewerConfig config) {
		getModel().setMetaData(LAYOUT_MANAGER_KEY, config);
	}

	private class MySimulatorListener implements VisiblyMutable.Listener {
		private boolean simulatorUpdatePending = false;

		public void changed(Event e) {
			if (!simulatorUpdatePending) {
				simulatorUpdatePending = true;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						simulatorUpdatePending = false;
						simulatorUpdated();
					}
				});
			}
		}
	}

}