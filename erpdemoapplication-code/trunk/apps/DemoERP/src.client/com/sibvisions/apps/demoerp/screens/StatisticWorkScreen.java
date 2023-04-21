/*
 * Copyright 2013 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.sibvisions.apps.demoerp.screens;

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIInsets;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIRadioButton;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.control.UIChart;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.apps.vaadin.web.IExpandableView;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * Shows a statistic with different chart types.
 */
public class StatisticWorkScreen extends DataSourceWorkScreen implements IExpandableView
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * labelSelectedYear.
	 */
	private UILabel				labelSelectedYear					= new UILabel();

	/**
	 * formLayoutSelectedYear.
	 */
	private UIFormLayout		formLayoutSelectedYear				= new UIFormLayout();

	/**
	 * navigationTableYear.
	 */
	private NavigationTable		navigationTableYear					= new NavigationTable();

	/**
	 * navigationTableMonth.
	 */
	private NavigationTable		navigationTableMonth				= new NavigationTable();

	/**
	 * Its a v_statistic_order_offer_year remote databook.
	 */
	private RemoteDataBook		rdbV_statistic_order_offer_year		= new RemoteDataBook();

	/**
	 * Its a v_statistic_order_offer_month remote databook.
	 */
	private RemoteDataBook		rdbV_statistic_order_offer_month	= new RemoteDataBook();

	/**
	 * splitPanel1.
	 */
	private UISplitPanel		splitPanel1							= new UISplitPanel();

	/**
	 * splitPanel2.
	 */
	private UISplitPanel		splitPanel2							= new UISplitPanel();

	/**
	 * panel1.
	 */
	private UIPanel				panel1								= new UIPanel();

	/**
	 * splitPanel1First.
	 */
	private UIPanel				splitPanel1First					= new UIPanel();

	/**
	 * panel2.
	 */
	private UIPanel				panel2								= new UIPanel();

	/**
	 * splitPanel2First.
	 */
	private UIPanel				splitPanel2First					= new UIPanel();

	/**
	 * splitPanel2Second.
	 */
	private UIPanel				splitPanel2Second					= new UIPanel();

	/**
	 * panelSelectedYear.
	 */
	private UIPanel				panelSelectedYear					= new UIPanel();

	/**
	 * panelChartStyle.
	 */
	private UIPanel				panelChartStyle						= new UIPanel();

	/**
	 * panelAreaLineChart.
	 */
	private UIPanel				panelAreaLineChart					= new UIPanel();

	/**
	 * tabsetPanelMain.
	 */
	private UITabsetPanel		tabsetPanelMain						= new UITabsetPanel();

	/**
	 * borderLayoutAreaLineChart.
	 */
	private UIBorderLayout		borderLayoutAreaLineChart			= new UIBorderLayout();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout		borderLayoutMain					= new UIBorderLayout();

	/**
	 * borderLayout2.
	 */
	private UIBorderLayout		borderLayout2						= new UIBorderLayout();

	/**
	 * borderLayout1.
	 */
	private UIBorderLayout		borderLayout1						= new UIBorderLayout();

	/**
	 * borderLayoutYearTable.
	 */
	private UIBorderLayout		borderLayoutYearTable				= new UIBorderLayout();

	/**
	 * borderLayoutMonthTable.
	 */
	private UIBorderLayout		borderLayoutMonthTable				= new UIBorderLayout();

	/**
	 * Switch between chart styles lines and area.
	 */
	private UIRadioButton[]		rbaChartStyles						= new UIRadioButton[] { new UIRadioButton("Lines"), new UIRadioButton("Areas") };

	/**
	 * The line and area chart.
	 */
	private UIChart				chartLineArea						= new UIChart();

	/**
	 * The bar chart.
	 */
	private UIChart				chartBar							= new UIChart();

	/**
	 * The current view state.
	 */
	private ExpandableViewState	evsCurrent							= ExpandableViewState.Default;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>StatisticWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public StatisticWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		doChangeYear();
	}

	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		rdbV_statistic_order_offer_year.setName("v_statistic_order_offer_year");
		rdbV_statistic_order_offer_year.setDataSource(getDataSource());
		rdbV_statistic_order_offer_year.setReadOnly(true);
		rdbV_statistic_order_offer_year.open();

		rdbV_statistic_order_offer_month.setName("v_statistic_order_offer_month");
		rdbV_statistic_order_offer_month.setDataSource(getDataSource());
		rdbV_statistic_order_offer_month.setReadOnly(true);
		rdbV_statistic_order_offer_month.setMasterReference(new ReferenceDefinition(new String[] { "YEAR_" }, rdbV_statistic_order_offer_year, new String[] { "YEAR_" }));
		rdbV_statistic_order_offer_month.open();

		rdbV_statistic_order_offer_month.getRowDefinition().getColumnView(ITableControl.class)
				.setColumnNames(new String[] { "MONTH", "OFFER_GROSSTOTALPRICE", "ORDER_GROSSTOTALPRICE" });

		rdbV_statistic_order_offer_year.eventAfterRowSelected().addListener(this, "doChangeYear");

		rdbV_statistic_order_offer_month.getRowDefinition().getColumnDefinition("OFFER_GROSSTOTALPRICE").setLabel("Total offers");
		rdbV_statistic_order_offer_month.getRowDefinition().getColumnDefinition("ORDER_GROSSTOTALPRICE").setLabel("Total orders");

		rdbV_statistic_order_offer_year.getRowDefinition().getColumnDefinition("OFFER_GROSSTOTALPRICE").setLabel("Total offers");
		rdbV_statistic_order_offer_year.getRowDefinition().getColumnDefinition("ORDER_GROSSTOTALPRICE").setLabel("Total orders");
	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		chartBar.setChartStyle(UIChart.STYLE_BARS);
		chartBar.setTitle("Annual Statistics");
		chartBar.setXAxisTitle("Year");
		chartBar.setYAxisTitle("Grosstotalprice");
		chartBar.setDataBook(rdbV_statistic_order_offer_year);
		chartBar.setXColumnName("YEAR_");
		chartBar.setYColumnNames(new String[] { "OFFER_GROSSTOTALPRICE", "ORDER_GROSSTOTALPRICE" });
		chartBar.setPreferredSize(200, 200);

		chartLineArea.setChartStyle(UIChart.STYLE_LINES);
		chartLineArea.setTitle("Monthly Statistics");
		chartLineArea.setXAxisTitle("Month");
		chartLineArea.setYAxisTitle("Grosstotalprice");
		chartLineArea.setDataBook(rdbV_statistic_order_offer_month);
		chartLineArea.setXColumnName("MONTH");
		chartLineArea.setYColumnNames(new String[] { "OFFER_GROSSTOTALPRICE", "ORDER_GROSSTOTALPRICE" });
		chartLineArea.setPreferredSize(200, 200);

		rbaChartStyles[0].eventAction().addListener(this, "doSwitchStyle");
		rbaChartStyles[1].eventAction().addListener(this, "doSwitchStyle");

		rbaChartStyles[0].setSelected(true);

		panelChartStyle.setLayout(new UIFlowLayout());
		panelChartStyle.add(rbaChartStyles[0]);
		panelChartStyle.add(rbaChartStyles[1]);

		panelAreaLineChart.setLayout(borderLayoutAreaLineChart);
		borderLayoutAreaLineChart.setMargins(10, 10, 10, 10);
		borderLayoutAreaLineChart.setVerticalGap(10);
		panelAreaLineChart.add(panelChartStyle, UIBorderLayout.NORTH);
		panelAreaLineChart.add(chartLineArea, UIBorderLayout.CENTER);

		navigationTableYear.setMaximumSize(new UIDimension(450, 350));
		navigationTableYear.setDataBook(rdbV_statistic_order_offer_year);
		navigationTableYear.setToolBarVisible(false);
		navigationTableYear.setSortOnHeaderEnabled(false);

		navigationTableMonth.setMaximumSize(new UIDimension(450, 350));
		navigationTableMonth.setDataBook(rdbV_statistic_order_offer_month);
		navigationTableMonth.setAutoResize(false);
		navigationTableMonth.setToolBarVisible(false);
        navigationTableMonth.setSortOnHeaderEnabled(false);

		panelSelectedYear.setMinimumSize(new UIDimension(20, 20));
		panelSelectedYear.setLayout(formLayoutSelectedYear);
		panelSelectedYear.setPreferredSize(new UIDimension(10, 42));
		panelSelectedYear.add(labelSelectedYear, formLayoutSelectedYear.getConstraints(0, 0));

		splitPanel1First.setLayout(borderLayoutYearTable);
		borderLayoutYearTable.setMargins(10, 10, 10, 10);
		splitPanel1First.add(navigationTableYear, UIBorderLayout.CENTER);

		splitPanel1.setMinimumSize(new UIDimension(20, 20));
		splitPanel1.setDividerPosition(290);
		splitPanel1.add(splitPanel1First, UISplitPanel.FIRST_COMPONENT);
		splitPanel1.add(chartBar, UISplitPanel.SECOND_COMPONENT);

		panel1.setLayout(borderLayout2);
		panel1.add(splitPanel1, UIBorderLayout.CENTER);

		splitPanel2First.setLayout(borderLayoutMonthTable);
		borderLayoutMonthTable.setMargins(0, 10, 10, 10);

		borderLayoutMain.setMargins(new UIInsets(5, 5, 5, 5));

		labelSelectedYear.setFont(new UIFont("", UIFont.BOLD, 14));
		labelSelectedYear.setForeground(new UIColor(68, 105, 139));
		labelSelectedYear.setText("");

		splitPanel2First.add(panelSelectedYear, UIBorderLayout.NORTH);
		splitPanel2First.add(navigationTableMonth, UIBorderLayout.CENTER);

		splitPanel2Second.setLayout(new UIBorderLayout());

		splitPanel2.setMinimumSize(new UIDimension(20, 20));
		splitPanel2.setDividerPosition(310);
		splitPanel2.add(splitPanel2First, UISplitPanel.FIRST_COMPONENT);
		splitPanel2.add(panelAreaLineChart, UISplitPanel.SECOND_COMPONENT);

		panel2.setLayout(borderLayout1);
		panel2.add(splitPanel2, UIBorderLayout.CENTER);

		tabsetPanelMain.setMinimumSize(new UIDimension(20, 20));
		tabsetPanelMain.add(panel1, "Annual statistics");
		tabsetPanelMain.add(panel2, "Monthly statistics");

		setLayout(borderLayoutMain);
		add(tabsetPanelMain, UIBorderLayout.CENTER);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setViewState(ExpandableViewState pViewState)
	{
		evsCurrent = pViewState;

		if (pViewState == ExpandableViewState.Maximimum)
		{
			splitPanel2.setDividerPosition(0);
			splitPanel1.setDividerPosition(0);
		}
		else
		{
			splitPanel2.setDividerPosition(310);
			splitPanel1.setDividerPosition(290);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ExpandableViewState getViewState()
	{
		return evsCurrent;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Switches the chart style.
	 * 
	 * @param pEvent the Event.
	 */
	public void doSwitchStyle(UIActionEvent pEvent)
	{
		UIRadioButton source = (UIRadioButton)pEvent.getSource();

		rbaChartStyles[0].setSelected(false);
		rbaChartStyles[1].setSelected(false);

		if (source == rbaChartStyles[0])
		{
			chartLineArea.setChartStyle(0);

			rbaChartStyles[0].setSelected(true);
		}
		else
		{
			chartLineArea.setChartStyle(1);

			rbaChartStyles[1].setSelected(true);
		}
	}

	/**
	 * Changes the selected year.
	 * 
	 * @throws Throwable
	 */
	public void doChangeYear() throws Throwable
	{
		labelSelectedYear.setText(rdbV_statistic_order_offer_year.getValueAsString("YEAR_"));
		chartLineArea.setTitle("Monthly Statistic for year " + rdbV_statistic_order_offer_year.getValueAsString("YEAR_"));
	}

} // StatisticWorkScreen
