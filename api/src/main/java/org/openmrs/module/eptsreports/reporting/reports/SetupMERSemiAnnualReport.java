package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxMlDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxTBDataset;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupMERSemiAnnualReport extends EptsDataExportManager {

  @Autowired private TxMlDataset txMlDataset;

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private TxTBDataset txTBDataset;

  @Override
  public String getExcelDesignUuid() {
    return "61fea06a-472b-11e9-8b42-876961a472ef";
  }

  @Override
  public String getUuid() {
    return "6febad76-472b-11e9-a41e-db8c77c788cd";
  }

  @Override
  public String getName() {
    return "PEPFAR Semiannual Report";
  }

  @Override
  public String getDescription() {
    return "PEPFAR Semiannual Report";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.setParameters(txMlDataset.getParameters());
    rd.addDataSetDefinition("TXML", Mapped.mapStraightThrough(txMlDataset.constructtxMlDataset()));
    rd.addDataSetDefinition("T", Mapped.mapStraightThrough(txTBDataset.constructTxTBDataset()));
    // add a base cohort to the report
    rd.setBaseCohortDefinition(
        genericCohortQueries.getBaseCohort(),
        ParameterizableUtil.createParameterMappings("endDate=${endDate},location=${location}"));

    return rd;
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "PEPFAR_SEMIANNUAL_REPORT.xls",
              "PEPFAR SEMIANNUAL REPORT",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }
}