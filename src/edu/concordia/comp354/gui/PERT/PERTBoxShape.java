package edu.concordia.comp354.gui.PERT;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxRectangleShape;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import edu.concordia.comp354.model.PERT.PERTEvent;
import edu.concordia.comp354.model.PERT.PERTNetwork;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by joao on 15.07.27.
 */
public class PERTBoxShape extends mxRectangleShape {
    public static final String SHAPE_PERTBOX = "pert_box";
    public static final int PERT_BOX_LENGTH = 80;
    public static DecimalFormat df = new DecimalFormat("##");


    public PERTBoxShape() {
        super();
    }

    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
        super.paintShape(canvas, state);

        Rectangle rect = state.getRectangle();

        // Paints the foreground
        if (configureGraphics(canvas, state, false)) {

            Object obj = ((mxCell) state.getCell()).getValue();

            if (obj instanceof PERTEvent) {

                PERTEvent event = (PERTEvent) obj;

                Graphics2D graphics = canvas.getGraphics();

                if (event.getT() != PERTNetwork.UNDEFINED) {
//                    System.out.println(rect);

                    double probability = event.getProbability();

                    int fillWidth = (int) (probability * rect.getWidth()/2);
                    Color oldColor = graphics.getColor();
                    graphics.setColor(new Color(210, 255, 199));
                    int radius = getArcSize((int)rect.getWidth(), (int)rect.getHeight());

                    graphics.fillRoundRect((int) rect.x + rect.width / 2 + 2, (int) rect.getY() + 1, fillWidth - 2, (int) rect.height / 2 - 1, radius,radius);

                    graphics.setColor(oldColor);

                    String eventInfo = event.getT() + "\n" + df.format(probability * 100) + "%";
                    drawInQuadrant(1, graphics, rect, eventInfo);
                }

                graphics.drawLine(rect.x + rect.width / 2, rect.y, rect.x + rect.width / 2, rect.y + rect.height);
                graphics.drawLine(rect.x, rect.y + rect.height / 2, rect.x + rect.width, rect.y + rect.height / 2);

                drawInQuadrant(2, graphics, rect, Integer.toString(event.getEventNo()));
                drawInQuadrant(3, graphics, rect, PERTTab.df.format(event.get_t()));
                drawInQuadrant(4, graphics, rect, PERTTab.df.format(event.get_s()));
            }
        }
    }

    private void drawInQuadrant(int quadrant, Graphics2D graphics, Rectangle rect, String label) {
        mxRectangle labelRect = mxUtils.getSizeForString(label, graphics.getFont(), 1);

        switch (quadrant) {
            case 1:
                if (label.contains("\n")) {
                    String[] lines = label.split("\n");

                    labelRect = mxUtils.getSizeForString(lines[0], graphics.getFont(), 1);

                    graphics.drawString(lines[0],
                            (float) (rect.x + rect.width - rect.width / 4 - labelRect.getWidth() / 2),
                            (float) (rect.y + rect.height / 4 /*+ labelRect.getHeight() / 4*/));

                    labelRect = mxUtils.getSizeForString(lines[1], graphics.getFont(), 1);
                    graphics.drawString(lines[1],
                            (float) (rect.x + rect.width - rect.width / 4 - labelRect.getWidth() / 2),
                            (float) (rect.y + rect.height / 4 + labelRect.getHeight()));
                }
                break;
            case 2:
                graphics.drawString(label,
                        (float) (rect.x + rect.width / 4 - labelRect.getWidth() / 2),
                        (float) (rect.y + rect.height / 4 + labelRect.getHeight() / 2));
                break;
            case 3:
                graphics.drawString(label,
                        (float) (rect.x + rect.width / 4 - labelRect.getWidth() / 2),
                        (float) (rect.y + rect.height - rect.height / 4 + labelRect.getHeight() / 2));
                break;
            case 4:
                graphics.drawString(label,
                        (float) (rect.x + rect.width - rect.width / 4 - labelRect.getWidth() / 2),
                        (float) (rect.y + rect.height - rect.height / 4 + labelRect.getHeight() / 2));
                break;
        }
    }

}
