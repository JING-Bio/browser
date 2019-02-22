package org.reactome.web.pwp.client.details.tabs.analysis.widgets.results;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.columns.*;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.EntitiesPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.InteractorsPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.EntitiesPathwaySelectedHandler;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.InteractorsPathwaySelectedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisResultTable extends DataGrid<PathwaySummary> {
    public final static Integer PAGE_SIZE = 20;

    private SingleSelectionModel<PathwaySummary> selectionModel;

    public AnalysisResultTable(List<String> expColumnNames, boolean interactors) {
        super(PAGE_SIZE, new ProvidesKey<PathwaySummary>() {
            @Override
            public Object getKey(PathwaySummary item) {
                return item == null ? null : item.getDbId();
            }
        });
        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);

        List<AbstractColumn<?>> columns = new LinkedList<>();
        columns.add(new PathwayNameColumn());

        if(interactors) {
            columns.add(new CuratedFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int i, PathwaySummary pathwaySummary, String s) {
                    if(pathwaySummary.getEntities().getCuratedFound() > 0) {
                        fireEvent(new EntitiesPathwaySelectedEvent(getSelectedObject()));
                    }
                }
            }));
            columns.add(new CuratedTotalColumn());

            columns.add(new InteractorsFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int i, PathwaySummary pathwaySummary, String s) {
                    if(pathwaySummary.getEntities().getInteractorsFound() > 0) {
                        fireEvent(new InteractorsPathwaySelectedEvent(getSelectedObject()));
                    }
                }
            }));
            columns.add(new InteractorsTotalColumn());

            columns.add(new EntitiesFoundColumn());
        } else {
            columns.add(new EntitiesFoundColumn(new FieldUpdater<PathwaySummary, String>() {
                @Override
                public void update(int index, PathwaySummary object, String value) {
                    fireEvent(new EntitiesPathwaySelectedEvent(getSelectedObject()));
                }
            }));
        }
        columns.add(new EntitiesTotalColumn(interactors));

//        if(!analysisResult.getSummary().getType().equals("EXPRESSION")){
            columns.add(new EntitiesRatioColumn());
            columns.add(new EntitiesPValueColumn());
            columns.add(new EntitiesFDRColumn());
            columns.add(new ReactionsFoundColumn());
            columns.add(new ReactionsTotalColumn());
            columns.add(new ReactionsRatioColumn());
//        }

        int i = 0;
        for (String columnName : expColumnNames) {
            columns.add(new ExpressionColumn(i++, columnName));
        }

        columns.add(new SpeciesColumn());

        for (AbstractColumn<?> column : columns) {
            this.addColumn(column, column.buildHeader());
            this.setColumnWidth(column, column.getWidth(), com.google.gwt.dom.client.Style.Unit.PX);
        }

        this.selectionModel = new SingleSelectionModel<>();
        this.setSelectionModel(selectionModel);

        this.sinkEvents(Event.ONMOUSEOUT);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler){
        return this.addHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler){
        return this.selectionModel.addSelectionChangeHandler(handler);
    }

    public HandlerRegistration addEntitiesPathwaySelectedHandler(EntitiesPathwaySelectedHandler handler){
        return this.addHandler(handler, EntitiesPathwaySelectedEvent.TYPE);
    }

    public HandlerRegistration addInteractorsPathwaySelectedHandler(InteractorsPathwaySelectedHandler handler){
        return this.addHandler(handler, InteractorsPathwaySelectedEvent.TYPE);
    }

    public PathwaySummary getSelectedObject(){
        return this.selectionModel.getSelectedObject();
    }

    public void scrollToItem(int index){
        this.getRowElement(index).scrollIntoView();
    }

    public void selectPathway(PathwaySummary pathwaySummary, Integer index){
        selectionModel.setSelected(pathwaySummary, true);
        this.getRowElement(index).scrollIntoView();
    }

    public void clearSelection(){
        this.selectionModel.clear();
    }

    private static DataGrid.Resources CUSTOM_STYLE;
    static {
        CUSTOM_STYLE = GWT.create(CustomTableResources.class);
        CUSTOM_STYLE.dataGridStyle().ensureInjected();
    }

    public interface CustomTableResources extends DataGrid.Resources {
        /**
         * The styles used in this widget.
         */
        @Source(CustomTableStyle.DEFAULT_CSS)
        Style dataGridStyle();
    }

    public interface CustomTableStyle extends DataGrid.Style {

        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/results/CustomResultsTable.css";
        /**
         * Applied to headers cells.
         */
        /**
         * Applied to every cell.
         */
        String dataGridCell();

        /**
         * Applied to even rows.
         */
        String dataGridEvenRow();

        /**
         * Applied to cells in even rows.
         */
        String dataGridEvenRowCell();

        /**
         * Applied to the first column.
         */
        String dataGridFirstColumn();

        /**
         * Applied to the first column footers.
         */
        String dataGridFirstColumnFooter();

        /**
         * Applied to the first column headers.
         */
        String dataGridFirstColumnHeader();

        /**
         * Applied to footers cells.
         */
        String dataGridFooter();

        /**
         * Applied to headers cells.
         */
        String dataGridHeader();

        /**
         * Applied to the hovered row.
         */
        String dataGridHoveredRow();

        /**
         * Applied to the cells in the hovered row.
         */
        String dataGridHoveredRowCell();

        /**
         * Applied to the keyboard selected cell.
         */
        String dataGridKeyboardSelectedCell();

        /**
         * Applied to the keyboard selected row.
         */
        String dataGridKeyboardSelectedRow();

        /**
         * Applied to the cells in the keyboard selected row.
         */
        String dataGridKeyboardSelectedRowCell();

        /**
         * Applied to the last column.
         */
        String dataGridLastColumn();

        /**
         * Applied to the last column footers.
         */
        String dataGridLastColumnFooter();

        /**
         * Applied to the last column headers.
         */
        String dataGridLastColumnHeader();

        /**
         * Applied to odd rows.
         */
        String dataGridOddRow();

        /**
         * Applied to cells in odd rows.
         */
        String dataGridOddRowCell();

        /**
         * Applied to selected rows.
         */
        String dataGridSelectedRow();

        /**
         * Applied to cells in selected rows.
         */
        String dataGridSelectedRowCell();

        /**
         * Applied to header cells that are sortable.
         */
        String dataGridSortableHeader();

        /**
         * Applied to header cells that are sorted in ascending order.
         */
        String dataGridSortedHeaderAscending();

        /**
         * Applied to header cells that are sorted in descending order.
         */
        String dataGridSortedHeaderDescending();

        /**
         * Applied to the table.
         */
        String dataGridWidget();

    }
}
