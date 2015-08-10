package edu.concordia.comp354.gui.PERT;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.*;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import edu.concordia.comp354.model.PERT.PERTEvent;
import edu.concordia.comp354.model.ProjectManager;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.EventObject;
import java.util.Map;

/**
 * Created by joao on 15.07.26.
 */
public class PERTPanel extends mxGraphComponent {
    private final ActivityEntry activityEntry;

    @Override
    public Object labelChanged(Object cell, Object value, EventObject evt) {
        mxIGraphModel model = graph.getModel();

        model.beginUpdate();
        try {

            PERTEvent event = (PERTEvent) ((mxCell) cell).getValue();
            event.setT(Float.parseFloat(StringUtils.defaultString((String) value, "")));
        } finally {
            model.endUpdate();
        }

        return cell;
    }

    public PERTPanel(ActivityEntry activityEntry, mxGraph graph) {
        super(graph);
        this.activityEntry = activityEntry;

        Map<String, Object> style = graph.getStylesheet().getDefaultVertexStyle();
        style.put(mxConstants.STYLE_SHAPE, PERTBoxShape.SHAPE_PERTBOX);

        setCellEditor(new cellEditor(this));
    }
}

class cellEditor extends mxCellEditor {

    /**
     * @param graphComponent
     */
    public cellEditor(mxGraphComponent graphComponent) {
        super(graphComponent);
    }

    @Override
    public Object getEditingCell() {
        return super.getEditingCell();
    }

    @Override
    public void startEditing(Object cell, EventObject evt) {
        if (editingCell != null) {
            stopEditing(true);
        }

        mxCellState state = graphComponent.getGraph().getView().getState(cell);

        if (state != null) {
            editingCell = cell;
            trigger = evt;

            PERTEvent event = (PERTEvent) ((mxCell) cell).getValue();
            event.setT(ProjectManager.UNDEFINED);
            double scale = Math.max(minimumEditorScale, graphComponent
                    .getGraph().getView().getScale());
            Rectangle bounds = getEditorBounds(state, scale);

            mxGeometry geometry = ((mxCell) cell).getGeometry();

            bounds.x = (int) (geometry.getX() + geometry.getWidth() / 2);
            bounds.width = (int) (geometry.getWidth() / 2);
            bounds.height = (int) (geometry.getHeight() / 2);


            scrollPane.setBounds(bounds);
            scrollPane.setVisible(true);

            String value = getInitialValue(state, evt);
            JTextComponent currentEditor = null;

            // Configures the style of the in-place editor
            if (graphComponent.getGraph().isHtmlLabel(cell)) {
                if (isExtractHtmlBody()) {
                    value = mxUtils.getBodyMarkup(value,
                            isReplaceHtmlLinefeeds());
                }

                editorPane.setDocument(mxUtils.createHtmlDocumentObject(
                        state.getStyle(), scale));
                editorPane.setText(value);

                // Workaround for wordwrapping in editor pane
                // FIXME: Cursor not visible at end of line
                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.setOpaque(false);
                wrapper.add(editorPane, BorderLayout.CENTER);
                scrollPane.setViewportView(wrapper);

                currentEditor = editorPane;
            } else {
                textArea.setFont(mxUtils.getFont(state.getStyle(), scale));
                Color fontColor = mxUtils.getColor(state.getStyle(),
                        mxConstants.STYLE_FONTCOLOR, Color.black);
                textArea.setForeground(fontColor);
                textArea.setText(value);

                scrollPane.setViewportView(textArea);
                currentEditor = textArea;
            }

            graphComponent.getGraphControl().add(scrollPane, 0);

            if (isHideLabel(state)) {
                graphComponent.redraw(state);
            }

            currentEditor.revalidate();
            currentEditor.requestFocusInWindow();
            currentEditor.selectAll();

            configureActionMaps();
        }
    }
}