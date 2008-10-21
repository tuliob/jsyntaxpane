/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License 
 *       at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 */
package jsyntaxpane.actions;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import jsyntaxpane.Token;

/**
 * This class contains static utility methods to make highliting in text 
 * components easier.
 * 
 * @author Ayman Al-Sairafi
 */
public class Markers {

    // This subclass is used in our highlighting code
    public static class SimpleMarker extends DefaultHighlighter.DefaultHighlightPainter {

        public SimpleMarker(Color color) {
            super(color);
        }
    }

    /**
     * Removes only our private highlights
     * This is public so that we can remove the highlights when the editorKit
     * is unregistered.  SimpleMarker can be null, in which case all instances of
     * our Markers are removed.
     */
    public static void removeMarkers(JTextComponent component, SimpleMarker marker) {
        Highlighter hilite = component.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof SimpleMarker) {
                SimpleMarker hMarker = (SimpleMarker) hilites[i].getPainter();
                if (marker == null || hMarker.equals(marker)) {
                    hilite.removeHighlight(hilites[i]);
                }
            }
        }
    }

    /**
     * Remove all the markers from an editorpane
     * @param editorPane
     */
    public static void removeHighlights(JTextComponent editorPane) {
        removeMarkers(editorPane, null);
    }

    /**
     * add highlights for the given Token on the given pane
     * @param pane
     * @param token
     * @param marker
     */
    public static void markToken(JTextComponent pane, Token token, SimpleMarker marker) {
        markText(pane, token.start, token.end(), marker);
    }

    /**
     * add highlights for the given Token on the given pane
     * @param pane
     * @param start
     * @param end
     * @param marker
     */
    public static void markText(JTextComponent pane, int start, int end, SimpleMarker marker) {
        try {
            Highlighter hiliter = pane.getHighlighter();
            hiliter.addHighlight(start, end, marker);
        } catch (BadLocationException ex) {
            // nothing we can do if the request is out of bound
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    private static Logger LOG = Logger.getLogger(Markers.class.getName());
}
