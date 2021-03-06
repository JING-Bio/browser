package org.reactome.web.pwp.client.viewport.diagram;

import org.reactome.web.diagram.events.AnalysisProfileChangedEvent;
import org.reactome.web.diagram.events.DiagramObjectsFlagResetEvent;
import org.reactome.web.diagram.events.DiagramProfileChangedEvent;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Diagram {

    interface Presenter extends BrowserModule.Presenter {
        void analysisReset();

        void databaseObjectSelected(Long dbId);

        void databaseObjectHovered(Long dbId);

        void diagramLoaded(Long dbId);

        void fireworksOpened(Long dbId);

        void diagramFlagPerformed(String term, Boolean includeInteractor);

        void resetFlag(DiagramObjectsFlagResetEvent event);

        void diagramProfileChanged(DiagramProfileChangedEvent event);

        void analysisProfileChanged(AnalysisProfileChangedEvent event);
    }

    interface Display extends BrowserModule.Display {
        boolean isVisible();

        void loadPathway(Pathway pathway);

        void flag(String flag, Boolean includeInteractors);

        void highlight(DatabaseObject databaseObject);

        void select(DatabaseObject databaseObject);

        void setAnalysisToken(AnalysisStatus analysisStatus);

        void setPresenter(Presenter presenter);

        void setVisible(boolean visible);
    }
}
