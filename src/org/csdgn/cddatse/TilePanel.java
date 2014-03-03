package org.csdgn.cddatse;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.csdgn.cddatse.data.InternalTile;
import org.csdgn.maru.listener.MagicCheckboxAdapter;
import org.csdgn.maru.listener.MagicDocumentAdapter;

public class TilePanel extends JPanel implements Scrollable {

	private static final long serialVersionUID = -3038838204602442661L;
	private InternalTile tse;
	private final MagicDocumentAdapter idAdapter;
	private final MagicCheckboxAdapter cornerAdapter;
	private final MagicCheckboxAdapter edgeAdapter;
	private final MagicCheckboxAdapter centerAdapter;
	private final MagicCheckboxAdapter tconnAdapter;
	private final MagicCheckboxAdapter endpAdapter;
	private final MagicCheckboxAdapter unconnAdapter;
	private final MagicCheckboxAdapter openAdapter;
	private final MagicCheckboxAdapter brokenAdapter;
	private final MagicCheckboxAdapter rotatesAdapter;
	private JTextField txtId;
	private JLabel lblId;
	private JCheckBox cboxRotates;
	private JCheckBox cboxCorner;
	private JCheckBox cboxEdge;
	private JCheckBox cboxCenter;
	private JCheckBox cboxTConnection;
	private JCheckBox cboxEndPiece;
	private JCheckBox cboxUnconnected;
	private JCheckBox cboxOpen;
	private JCheckBox cboxBroken;
	private ImageTuplePanel tileImages;
	private ImageTuplePanel cornerImages;
	private ImageTuplePanel edgeImages;
	private ImageTuplePanel centerImages;
	private ImageTuplePanel tConnectionImages;
	private ImageTuplePanel endPieceImages;
	private ImageTuplePanel unconnectedImages;
	private ImageTuplePanel openImages;
	private ImageTuplePanel brokenImages;
	
	private EditorFrame frame;
	private String lastId = null;
	private boolean doIdUpdate = false;
	
	private void updateId() {
		if(!doIdUpdate)
			return;
		String nId = txtId.getText();
		if(!frame.updateTileId(lastId,nId)) {
			//System.out.println("BAD ID: " + nId);
			lblId.setForeground(Color.RED);
			txtId.setForeground(Color.RED);
		} else {
			//System.out.println("GOOD ID: " + nId);
			lblId.setForeground(Color.BLACK);
			txtId.setForeground(Color.BLACK);
		}
		lastId = nId;
		frame.updateIDList();
	}
	
	/**
	 * Create the panel.
	 */
	public TilePanel(final EditorFrame frame) {
		setAutoscrolls(true);
		this.frame = frame;
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		
		lblId = new JLabel("ID");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		add(lblId, gbc_lblId);
		
		txtId = new JTextField();
		idAdapter = new MagicDocumentAdapter(InternalTile.class, "setId");
		txtId.getDocument().addDocumentListener(idAdapter);
		txtId.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateId();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateId();
			}
			@Override
			public void changedUpdate(DocumentEvent e) { }
		});
		
		GridBagConstraints gbc_txtId = new GridBagConstraints();
		gbc_txtId.insets = new Insets(0, 0, 5, 0);
		gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtId.gridx = 1;
		gbc_txtId.gridy = 0;
		add(txtId, gbc_txtId);
		txtId.setColumns(10);
		
		tileImages = new ImageTuplePanel(frame);
		GridBagConstraints gbc_tileImages = new GridBagConstraints();
		gbc_tileImages.fill = GridBagConstraints.VERTICAL;
		gbc_tileImages.anchor = GridBagConstraints.WEST;
		gbc_tileImages.insets = new Insets(0, 0, 5, 0);
		gbc_tileImages.gridx = 1;
		gbc_tileImages.gridy = 2;
		add(tileImages, gbc_tileImages);
		
		JPanel multitilePanel = new JPanel();
		multitilePanel.setBorder(new TitledBorder(null, "Multitile", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_multitilePanel = new GridBagConstraints();
		gbc_multitilePanel.anchor = GridBagConstraints.WEST;
		gbc_multitilePanel.fill = GridBagConstraints.VERTICAL;
		gbc_multitilePanel.gridx = 1;
		gbc_multitilePanel.gridy = 3;
		add(multitilePanel, gbc_multitilePanel);
		GridBagLayout gbl_multitilePanel = new GridBagLayout();
		gbl_multitilePanel.columnWidths = new int[]{0, 0, 0};
		gbl_multitilePanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_multitilePanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_multitilePanel.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		multitilePanel.setLayout(gbl_multitilePanel);
		
		cboxCorner = new JCheckBox("Corner");
		cboxCorner.addActionListener(cornerAdapter = new MagicCheckboxAdapter(InternalTile.class, "setCorner"));
		GridBagConstraints gbc_chckbxCorner = new GridBagConstraints();
		gbc_chckbxCorner.anchor = GridBagConstraints.WEST;
		gbc_chckbxCorner.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCorner.gridx = 0;
		gbc_chckbxCorner.gridy = 0;
		multitilePanel.add(cboxCorner, gbc_chckbxCorner);
		
		cboxEdge = new JCheckBox("Edge");
		cboxEdge.addActionListener(edgeAdapter = new MagicCheckboxAdapter(InternalTile.class, "setEdge"));
		GridBagConstraints gbc_chckbxEdge = new GridBagConstraints();
		gbc_chckbxEdge.anchor = GridBagConstraints.WEST;
		gbc_chckbxEdge.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxEdge.gridx = 1;
		gbc_chckbxEdge.gridy = 0;
		multitilePanel.add(cboxEdge, gbc_chckbxEdge);
		
		cornerImages = new ImageTuplePanel(frame);
		cornerImages.setToolTipText("Opens to bottom and right.");
		GridBagLayout gbl_cornerImages = (GridBagLayout) cornerImages.getLayout();
		gbl_cornerImages.rowWeights = new double[]{0.0, 1.0};
		gbl_cornerImages.rowHeights = new int[]{0, 0};
		gbl_cornerImages.columnWeights = new double[]{1.0, 1.0};
		gbl_cornerImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_cornerImages = new GridBagConstraints();
		gbc_cornerImages.insets = new Insets(0, 0, 5, 5);
		gbc_cornerImages.fill = GridBagConstraints.BOTH;
		gbc_cornerImages.gridx = 0;
		gbc_cornerImages.gridy = 1;
		multitilePanel.add(cornerImages, gbc_cornerImages);
		
		edgeImages = new ImageTuplePanel(frame);
		edgeImages.setToolTipText("Opens to top and bottom.");
		GridBagLayout gbl_edgeImages = (GridBagLayout) edgeImages.getLayout();
		gbl_edgeImages.rowWeights = new double[]{0.0, 1.0};
		gbl_edgeImages.rowHeights = new int[]{0, 0};
		gbl_edgeImages.columnWeights = new double[]{1.0, 1.0};
		gbl_edgeImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_edgeImages = new GridBagConstraints();
		gbc_edgeImages.insets = new Insets(0, 0, 5, 0);
		gbc_edgeImages.fill = GridBagConstraints.BOTH;
		gbc_edgeImages.gridx = 1;
		gbc_edgeImages.gridy = 1;
		multitilePanel.add(edgeImages, gbc_edgeImages);
		
		cboxCenter = new JCheckBox("Center");
		cboxCenter.addActionListener(centerAdapter = new MagicCheckboxAdapter(InternalTile.class, "setCenter"));
		GridBagConstraints gbc_chckbxCenter = new GridBagConstraints();
		gbc_chckbxCenter.anchor = GridBagConstraints.WEST;
		gbc_chckbxCenter.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCenter.gridx = 0;
		gbc_chckbxCenter.gridy = 2;
		multitilePanel.add(cboxCenter, gbc_chckbxCenter);
		
		cboxTConnection = new JCheckBox("T Connection");
		cboxTConnection.addActionListener(tconnAdapter = new MagicCheckboxAdapter(InternalTile.class, "setTConnection"));
		GridBagConstraints gbc_chckbxTConnection = new GridBagConstraints();
		gbc_chckbxTConnection.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxTConnection.anchor = GridBagConstraints.WEST;
		gbc_chckbxTConnection.gridx = 1;
		gbc_chckbxTConnection.gridy = 2;
		multitilePanel.add(cboxTConnection, gbc_chckbxTConnection);
		
		centerImages = new ImageTuplePanel(frame);
		centerImages.setToolTipText("Opens to all 4 sides.");
		GridBagLayout gbl_centerImages = (GridBagLayout) centerImages.getLayout();
		gbl_centerImages.rowWeights = new double[]{0.0, 1.0};
		gbl_centerImages.rowHeights = new int[]{0, 0};
		gbl_centerImages.columnWeights = new double[]{1.0, 1.0};
		gbl_centerImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_centerImages = new GridBagConstraints();
		gbc_centerImages.insets = new Insets(0, 0, 5, 5);
		gbc_centerImages.fill = GridBagConstraints.BOTH;
		gbc_centerImages.gridx = 0;
		gbc_centerImages.gridy = 3;
		multitilePanel.add(centerImages, gbc_centerImages);
		
		tConnectionImages = new ImageTuplePanel(frame);
		tConnectionImages.setToolTipText("Opens to Left, Right and Bottom.");
		GridBagLayout gbl_tConnectionImages = (GridBagLayout) tConnectionImages.getLayout();
		gbl_tConnectionImages.rowWeights = new double[]{0.0, 1.0};
		gbl_tConnectionImages.rowHeights = new int[]{0, 0};
		gbl_tConnectionImages.columnWeights = new double[]{1.0, 1.0};
		gbl_tConnectionImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_tConnectionImages = new GridBagConstraints();
		gbc_tConnectionImages.insets = new Insets(0, 0, 5, 0);
		gbc_tConnectionImages.fill = GridBagConstraints.BOTH;
		gbc_tConnectionImages.gridx = 1;
		gbc_tConnectionImages.gridy = 3;
		multitilePanel.add(tConnectionImages, gbc_tConnectionImages);
		
		cboxEndPiece = new JCheckBox("End Piece");
		cboxEndPiece.addActionListener(endpAdapter = new MagicCheckboxAdapter(InternalTile.class, "setEndPiece"));
		GridBagConstraints gbc_chckbxEndPiece = new GridBagConstraints();
		gbc_chckbxEndPiece.anchor = GridBagConstraints.WEST;
		gbc_chckbxEndPiece.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxEndPiece.gridx = 0;
		gbc_chckbxEndPiece.gridy = 4;
		multitilePanel.add(cboxEndPiece, gbc_chckbxEndPiece);
		
		cboxUnconnected = new JCheckBox("Unconnected");
		cboxUnconnected.addActionListener(unconnAdapter = new MagicCheckboxAdapter(InternalTile.class, "setUnconnected"));
		GridBagConstraints gbc_chckbxUnconnected = new GridBagConstraints();
		gbc_chckbxUnconnected.anchor = GridBagConstraints.WEST;
		gbc_chckbxUnconnected.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUnconnected.gridx = 1;
		gbc_chckbxUnconnected.gridy = 4;
		multitilePanel.add(cboxUnconnected, gbc_chckbxUnconnected);
		
		endPieceImages = new ImageTuplePanel(frame);
		endPieceImages.setToolTipText("Opens to bottom.");
		GridBagLayout gbl_endPieceImages = (GridBagLayout) endPieceImages.getLayout();
		gbl_endPieceImages.rowWeights = new double[]{0.0, 1.0};
		gbl_endPieceImages.rowHeights = new int[]{0, 0};
		gbl_endPieceImages.columnWeights = new double[]{1.0, 1.0};
		gbl_endPieceImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_endPieceImages = new GridBagConstraints();
		gbc_endPieceImages.insets = new Insets(0, 0, 5, 5);
		gbc_endPieceImages.fill = GridBagConstraints.BOTH;
		gbc_endPieceImages.gridx = 0;
		gbc_endPieceImages.gridy = 5;
		multitilePanel.add(endPieceImages, gbc_endPieceImages);
		
		unconnectedImages = new ImageTuplePanel(frame);
		unconnectedImages.setToolTipText("Does not open, same as normal image.");
		GridBagLayout gbl_unconnectedImages = (GridBagLayout) unconnectedImages.getLayout();
		gbl_unconnectedImages.rowWeights = new double[]{0.0, 1.0};
		gbl_unconnectedImages.rowHeights = new int[]{0, 0};
		gbl_unconnectedImages.columnWeights = new double[]{1.0, 1.0};
		gbl_unconnectedImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_unconnectedImages = new GridBagConstraints();
		gbc_unconnectedImages.insets = new Insets(0, 0, 5, 0);
		gbc_unconnectedImages.fill = GridBagConstraints.BOTH;
		gbc_unconnectedImages.gridx = 1;
		gbc_unconnectedImages.gridy = 5;
		multitilePanel.add(unconnectedImages, gbc_unconnectedImages);
		
		cboxOpen = new JCheckBox("Open");
		cboxOpen.addActionListener(openAdapter = new MagicCheckboxAdapter(InternalTile.class, "setOpen"));
		GridBagConstraints gbc_chckbxOpen = new GridBagConstraints();
		gbc_chckbxOpen.anchor = GridBagConstraints.WEST;
		gbc_chckbxOpen.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOpen.gridx = 0;
		gbc_chckbxOpen.gridy = 6;
		multitilePanel.add(cboxOpen, gbc_chckbxOpen);
		
		cboxBroken = new JCheckBox("Broken");
		cboxBroken.addActionListener(brokenAdapter = new MagicCheckboxAdapter(InternalTile.class, "setBroken"));
		GridBagConstraints gbc_chckbxBroken = new GridBagConstraints();
		gbc_chckbxBroken.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxBroken.anchor = GridBagConstraints.WEST;
		gbc_chckbxBroken.gridx = 1;
		gbc_chckbxBroken.gridy = 6;
		multitilePanel.add(cboxBroken, gbc_chckbxBroken);
		
		openImages = new ImageTuplePanel(frame);
		openImages.setToolTipText("Open");
		GridBagLayout gbl_openImages = (GridBagLayout) openImages.getLayout();
		gbl_openImages.rowWeights = new double[]{0.0, 1.0};
		gbl_openImages.rowHeights = new int[]{0, 0};
		gbl_openImages.columnWeights = new double[]{1.0, 1.0};
		gbl_openImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_openImages = new GridBagConstraints();
		gbc_openImages.insets = new Insets(0, 0, 0, 5);
		gbc_openImages.fill = GridBagConstraints.BOTH;
		gbc_openImages.gridx = 0;
		gbc_openImages.gridy = 7;
		multitilePanel.add(openImages, gbc_openImages);
		
		brokenImages = new ImageTuplePanel(frame);
		brokenImages.setToolTipText("Broken");
		GridBagLayout gbl_brokenImages = (GridBagLayout) brokenImages.getLayout();
		gbl_brokenImages.rowWeights = new double[]{0.0, 1.0};
		gbl_brokenImages.rowHeights = new int[]{0, 0};
		gbl_brokenImages.columnWeights = new double[]{1.0, 1.0};
		gbl_brokenImages.columnWidths = new int[]{0, 0};
		GridBagConstraints gbc_brokenImages = new GridBagConstraints();
		gbc_brokenImages.fill = GridBagConstraints.BOTH;
		gbc_brokenImages.gridx = 1;
		gbc_brokenImages.gridy = 7;
		multitilePanel.add(brokenImages, gbc_brokenImages);
		
		cboxRotates = new JCheckBox("Rotates");
		cboxRotates.addActionListener(rotatesAdapter = new MagicCheckboxAdapter(InternalTile.class, "setRotates"));
		GridBagConstraints gbc_chckbxRotates = new GridBagConstraints();
		gbc_chckbxRotates.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxRotates.anchor = GridBagConstraints.WEST;
		gbc_chckbxRotates.gridx = 1;
		gbc_chckbxRotates.gridy = 1;
		add(cboxRotates, gbc_chckbxRotates);
	}
	
	public void setTilesetEntry(InternalTile tse) {
		this.tse = tse;
		
		doIdUpdate = false;
		lastId = null;
		
		idAdapter.setObject(this.tse);
		cornerAdapter.setObject(this.tse);
		edgeAdapter.setObject(this.tse);
		centerAdapter.setObject(this.tse);
		tconnAdapter.setObject(this.tse);
		endpAdapter.setObject(this.tse);
		unconnAdapter.setObject(this.tse);
		openAdapter.setObject(this.tse);
		brokenAdapter.setObject(this.tse);
		rotatesAdapter.setObject(this.tse);
		
		//update all the crap
		txtId.setText(tse.id);
		cboxRotates.setSelected(tse.rotates);
		cboxCorner.setSelected(tse.corner);
		cboxEdge.setSelected(tse.edge);
		cboxCenter.setSelected(tse.center);
		cboxTConnection.setSelected(tse.tConnection);
		cboxEndPiece.setSelected(tse.endPiece);
		cboxUnconnected.setSelected(tse.unconnected);
		cboxOpen.setSelected(tse.open);
		cboxBroken.setSelected(tse.broken);
		//update the image panels
		tileImages.setImageTuple(tse.image);
		cornerImages.setImageTuple(tse.cornerImage);
		edgeImages.setImageTuple(tse.edgeImage);
		centerImages.setImageTuple(tse.centerImage);
		tConnectionImages.setImageTuple(tse.tConnectionImage);
		endPieceImages.setImageTuple(tse.endPieceImage);
		unconnectedImages.setImageTuple(tse.unconnectedImage);
		openImages.setImageTuple(tse.openImage);
		brokenImages.setImageTuple(tse.brokenImage);
		
		doIdUpdate = true;
		updateId();
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if(orientation == SwingConstants.VERTICAL) {
			return visibleRect.height / 24;
		}
		return visibleRect.width / 24;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		if(orientation == SwingConstants.VERTICAL) {
			return visibleRect.height / 6;
		}
		return visibleRect.width / 6;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
